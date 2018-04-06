package com.zes.squad.gmh.service.impl;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureConditionSatisfied;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityNotExist;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zes.squad.gmh.common.enums.ProductStockConvertFlowTypeEnum;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.ProductConvertStockQueryCondition;
import com.zes.squad.gmh.entity.po.ProductAmountPo;
import com.zes.squad.gmh.entity.po.ProductConvertStockFlowPo;
import com.zes.squad.gmh.entity.po.ProductConvertStockPo;
import com.zes.squad.gmh.entity.po.StockAmountPo;
import com.zes.squad.gmh.entity.union.ProductConvertStockUnion;
import com.zes.squad.gmh.mapper.ProductAmountMapper;
import com.zes.squad.gmh.mapper.ProductConvertStockFlowMapper;
import com.zes.squad.gmh.mapper.ProductConvertStockMapper;
import com.zes.squad.gmh.mapper.ProductConvertStockUnionMapper;
import com.zes.squad.gmh.mapper.StockAmountMapper;
import com.zes.squad.gmh.service.ProductConvertStockService;

@Service
public class ProductConvertStockServiceImpl implements ProductConvertStockService {

    @Autowired
    private ProductConvertStockMapper      productConvertStockMapper;
    @Autowired
    private ProductConvertStockUnionMapper productConvertStockUnionMapper;
    @Autowired
    private ProductConvertStockFlowMapper  productConvertStockFlowMapper;
    @Autowired
    private ProductAmountMapper            productAmountMapper;
    @Autowired
    private StockAmountMapper              stockAmountMapper;

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProductConvertStockPo createProductConvertStock(ProductConvertStockPo po) {
        ProductConvertStockPo existingPo = productConvertStockMapper.selectByProductAndStock(po.getProductId(),
                po.getStockId());
        ensureEntityNotExist(existingPo, "产品库存转换关系已存在");
        int record = productConvertStockMapper.insert(po);
        ensureConditionSatisfied(record == 1, "新增产品库存转换关系失败");
        return po;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProductConvertStock(Long id) {
        int record = productConvertStockMapper.deleteById(id);
        ensureConditionSatisfied(record == 1, "产品库存转换关系删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProductConvertStockPo modifyProductConvertStock(ProductConvertStockPo po) {
        int record = productConvertStockMapper.updateSelective(po);
        ensureConditionSatisfied(record == 1, "产品库存转换更新失败");
        return productConvertStockMapper.selectById(po.getId());
    }

    @Override
    public PagedList<ProductConvertStockUnion> listPagedProductConvertStocks(ProductConvertStockQueryCondition condition) {
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<ProductConvertStockUnion> unions = productConvertStockUnionMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<ProductConvertStockUnion> info = new PageInfo<>(unions);
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), unions);
    }

    @Override
    public List<ProductConvertStockUnion> listAllProductConvertStocks() {
        return productConvertStockUnionMapper.selectAll();
    }

    @Override
    public BigDecimal calculateAmount(ProductConvertStockPo po) {
        ProductConvertStockPo productConvertStockPo = productConvertStockMapper
                .selectByProductAndStock(po.getProductId(), po.getStockId());
        BigDecimal amount = null;
        if (po.getProductAmount() != null) {
            //根据产品计算库存数量
            amount = po.getProductAmount().multiply(productConvertStockPo.getStockAmount())
                    .divide(productConvertStockPo.getProductAmount(), 2, BigDecimal.ROUND_HALF_EVEN);
        } else {
            //根据库存计算产品数量
            amount = po.getStockAmount().multiply(productConvertStockPo.getProductAmount())
                    .divide(productConvertStockPo.getStockAmount(), 2, BigDecimal.ROUND_HALF_EVEN);
        }
        return amount;
    }

    @Transactional(rollbackFor = {Throwable.class})
    @Override
    public ProductConvertStockFlowPo createProductConvertStockFlow(ProductConvertStockFlowPo po) {
        Long storeId = ThreadContext.getUserStoreId();
        ensureEntityExist(storeId, "当前用户不属于任何店铺");
        po.setStoreId(storeId);
        int record = productConvertStockFlowMapper.insert(po);
        ensureConditionSatisfied(record == 1, "生成产品库存转化流水失败");
        if (po.getType() == ProductStockConvertFlowTypeEnum.PRODUCT_TO_STOCK.getKey()) {
            //产品转化为库存(产品减少，库存增加)
            ProductAmountPo existingProductAmountPo = productAmountMapper.selectByProductAndStore(po.getProductId(), storeId);
            ensureEntityExist(existingProductAmountPo, "该门店无此产品");
            ProductAmountPo productAmountPo = new ProductAmountPo();
            productAmountPo.setId(existingProductAmountPo.getId());
            productAmountPo.setAmount(po.getProductAmount());
            record = productAmountMapper.reduceAmount(productAmountPo);
            ensureConditionSatisfied(record == 1, "修改产品余量失败");
            StockAmountPo existingStockAmountPo = stockAmountMapper.selectByStockAndStore(po.getStockId(), storeId);
            ensureEntityExist(existingProductAmountPo, "该门店无此库存");
            StockAmountPo stockAmountPo = new StockAmountPo();
            stockAmountPo.setId(existingStockAmountPo.getId());
            stockAmountPo.setAmount(po.getStockAmount());
            record = stockAmountMapper.addAmount(stockAmountPo);
            ensureConditionSatisfied(record == 1, "修改库存余量失败");
        }
        if (po.getType() == ProductStockConvertFlowTypeEnum.STOCK_TO_PRODUCT.getKey()) {
            //库存转化为产品(库存减少，产品增加)
            ProductAmountPo existingProductAmountPo = productAmountMapper.selectByProductAndStore(po.getProductId(), storeId);
            ensureEntityExist(existingProductAmountPo, "该门店无此产品");
            ProductAmountPo productAmountPo = new ProductAmountPo();
            productAmountPo.setId(existingProductAmountPo.getId());
            productAmountPo.setAmount(po.getProductAmount());
            record = productAmountMapper.addAmount(productAmountPo);
            ensureConditionSatisfied(record == 1, "修改产品余量失败");
            StockAmountPo existingStockAmountPo = stockAmountMapper.selectByStockAndStore(po.getStockId(), storeId);
            ensureEntityExist(existingProductAmountPo, "该门店无此库存");
            StockAmountPo stockAmountPo = new StockAmountPo();
            stockAmountPo.setId(existingStockAmountPo.getId());
            stockAmountPo.setAmount(po.getStockAmount());
            record = stockAmountMapper.reduceAmount(stockAmountPo);
            ensureConditionSatisfied(record == 1, "修改库存余量失败");
        }
        return po;
    }

}
