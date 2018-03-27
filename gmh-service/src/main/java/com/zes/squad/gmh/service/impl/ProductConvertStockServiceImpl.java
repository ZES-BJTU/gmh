package com.zes.squad.gmh.service.impl;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureConditionValid;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityNotExist;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.ProductConvertStockQueryCondition;
import com.zes.squad.gmh.entity.po.ProductConvertStockPo;
import com.zes.squad.gmh.entity.union.ProductConvertStockUnion;
import com.zes.squad.gmh.mapper.ProductConvertStockMapper;
import com.zes.squad.gmh.mapper.ProductConvertStockUnionMapper;
import com.zes.squad.gmh.service.ProductConvertStockService;

@Service
public class ProductConvertStockServiceImpl implements ProductConvertStockService {

    @Autowired
    private ProductConvertStockMapper      productConvertStockMapper;
    @Autowired
    private ProductConvertStockUnionMapper productConvertStockUnionMapper;

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProductConvertStockPo createProductConvertStock(ProductConvertStockPo po) {
        ProductConvertStockPo existingPo = productConvertStockMapper.selectByProductAndStock(po.getProductId(),
                po.getStockId());
        ensureEntityNotExist(existingPo, "产品库存转换关系已存在");
        int record = productConvertStockMapper.insert(po);
        ensureConditionValid(record == 1, "新增产品库存转换关系失败");
        return po;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProductConvertStock(Long id) {
        int record = productConvertStockMapper.deleteById(id);
        ensureConditionValid(record == 1, "产品库存转换关系删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProductConvertStockPo modifyProductConvertStock(ProductConvertStockPo po) {
        int record = productConvertStockMapper.updateSelective(po);
        ensureConditionValid(record == 1, "产品库存转换更新失败");
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

}
