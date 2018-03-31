package com.zes.squad.gmh.service.impl;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionNotEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureConditionSatisfied;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityNotExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.enums.FlowTypeEnum;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.ProductAmountQueryCondition;
import com.zes.squad.gmh.entity.condition.ProductQueryCondition;
import com.zes.squad.gmh.entity.condition.ProductTypeQueryCondition;
import com.zes.squad.gmh.entity.po.ProductAmountPo;
import com.zes.squad.gmh.entity.po.ProductFlowPo;
import com.zes.squad.gmh.entity.po.ProductPo;
import com.zes.squad.gmh.entity.po.ProductTypePo;
import com.zes.squad.gmh.entity.union.ProductUnion;
import com.zes.squad.gmh.mapper.ProductAmountMapper;
import com.zes.squad.gmh.mapper.ProductFlowMapper;
import com.zes.squad.gmh.mapper.ProductMapper;
import com.zes.squad.gmh.mapper.ProductTypeMapper;
import com.zes.squad.gmh.mapper.ProductUnionMapper;
import com.zes.squad.gmh.service.ProductService;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductTypeMapper   productTypeMapper;
    @Autowired
    private ProductMapper       productMapper;
    @Autowired
    private ProductUnionMapper  productUnionMapper;
    @Autowired
    private ProductAmountMapper productAmountMapper;
    @Autowired
    private ProductFlowMapper   productFlowMapper;

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProductTypePo createProductType(ProductTypePo po) {
        ProductTypePo existingTypePo = productTypeMapper.selectByName(po.getName());
        ensureEntityNotExist(existingTypePo, "产品分类已存在");
        int record = productTypeMapper.insert(po);
        ensureConditionSatisfied(record == 1, "添加产品失败");
        return po;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProductType(Long id) {
        List<ProductPo> pos = productMapper.selectByTypeId(id);
        ensureCollectionEmpty(pos, "产品类型已被使用,无法删除");
        int row = productTypeMapper.deleteById(id);
        ensureConditionSatisfied(row == 1, "产品分类删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProductTypePo modifyProductType(ProductTypePo po) {
        ProductTypePo existingPo = productTypeMapper.selectByName(po.getName());
        if (existingPo != null) {
            ensureConditionSatisfied(existingPo.getId().equals(po.getId()), "产品分类已存在");
        }
        productTypeMapper.updateSelective(po);
        ProductTypePo newTypePo = productTypeMapper.selectById(po.getId());
        ensureEntityExist(newTypePo, "产品分类不存在");
        return newTypePo;
    }

    @Override
    public ProductTypePo queryProductTypeDetail(Long id) {
        return productTypeMapper.selectById(id);
    }

    @Override
    public PagedList<ProductTypePo> listPagedProductTypes(ProductTypeQueryCondition condition) {
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<ProductTypePo> pos = productTypeMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(pos)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<ProductTypePo> info = new PageInfo<>(pos);
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), pos);
    }

    @Override
    public List<ProductTypePo> listAllProductTypes() {
        List<ProductTypePo> pos = productTypeMapper.selectAll();
        ensureCollectionNotEmpty(pos, "请先新建产品分类");
        return pos;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProductPo createProduct(ProductPo po) {
        ProductPo existingPo = productMapper.selectByCode(po.getCode());
        ensureEntityNotExist(existingPo, "产品代码已被占用");
        ProductTypePo typePo = productTypeMapper.selectById(po.getProductTypeId());
        ensureEntityExist(typePo, "产品分类不存在");
        int record = productMapper.insert(po);
        ensureConditionSatisfied(record == 1, "添加产品失败");
        return po;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProduct(Long id) {
        List<ProductAmountPo> pos = productAmountMapper.selectByProductId(id);
        ensureCollectionEmpty(pos, "产品已被使用,无法删除");
        int row = productMapper.deleteById(id);
        ensureConditionSatisfied(row == 1, "产品删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProducts(List<Long> ids) {
        int rows = productMapper.batchDelete(ids);
        ensureConditionSatisfied(rows == ids.size(), "产品删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProductPo modifyProduct(ProductPo po) {
        if (po.getProductTypeId() != null) {
            ProductTypePo typePo = productTypeMapper.selectById(po.getProductTypeId());
            ensureEntityExist(typePo, "产品分类不存在");
        }
        ProductPo existingPo = productMapper.selectByCode(po.getCode());
        if (existingPo != null) {
            ensureConditionSatisfied(po.getId().equals(existingPo.getId()), "产品代码已被占用");
        }
        productMapper.updateSelective(po);
        ProductPo newPo = productMapper.selectById(po.getId());
        ensureEntityExist(newPo, "产品不存在");
        return newPo;
    }

    @Override
    public ProductUnion queryProductDetail(Long id) {
        ProductUnion union = productUnionMapper.selectById(id);
        ensureEntityExist(union, "产品不存在");
        ensureEntityExist(union.getId(), "产品不存在");
        ensureEntityExist(union.getProductTypePo(), "产品分类不存在");
        ensureEntityExist(union.getProductPo(), "产品不存在");
        return union;
    }

    @Override
    public PagedList<ProductUnion> listPagedProducts(ProductQueryCondition condition) {
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<ProductUnion> unions = productUnionMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<ProductUnion> info = new PageInfo<>(unions);
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), unions);
    }

    @Override
    public List<ProductPo> listAllProducts() {
        List<ProductPo> pos = productMapper.selectAll();
        ensureCollectionNotEmpty(pos, "请先新建产品");
        return pos;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProductAmountPo createProductAmount(ProductAmountPo po) {
        Long storeId = ThreadContext.getUserStoreId();
        ensureEntityExist(storeId, "当前用户不属于任何一家门店");
        ProductAmountQueryCondition condition = new ProductAmountQueryCondition();
        condition.setProductId(po.getProductId());
        condition.setStoreId(storeId);
        ProductAmountPo existingPo = productAmountMapper.selectByCondition(condition);
        ensureEntityNotExist(existingPo, "产品数量已存在");
        po.setStoreId(storeId);
        productAmountMapper.insert(po);
        ProductFlowPo flowPo = new ProductFlowPo();
        flowPo.setProductId(po.getProductId());
        flowPo.setType(FlowTypeEnum.FIRST_BUYING_IN.getKey());
        flowPo.setAmount(po.getAmount());
        flowPo.setStoreId(storeId);
        int record = productFlowMapper.insert(flowPo);
        ensureConditionSatisfied(record == 1, "产品流水生成失败");
        return po;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProductAmount(Long id) {
        int row = productAmountMapper.deleteById(id);
        ensureConditionSatisfied(row == 1, "产品数量删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProductAmounts(List<Long> ids) {
        int rows = productAmountMapper.batchDelete(ids);
        ensureConditionSatisfied(rows == ids.size(), "产品数量删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProductAmountPo modifyProductAmount(ProductAmountPo po) {
        int record = productAmountMapper.updateAmount(po);
        ensureConditionSatisfied(record == 1, "产品数量修改失败");
        ProductAmountPo newPo = productAmountMapper.selectById(po.getId());
        ensureEntityExist(newPo, "产品数量不存在");
        ProductFlowPo flowPo = new ProductFlowPo();
        flowPo.setProductId(newPo.getProductId());
        flowPo.setType(FlowTypeEnum.ADJUSTMENT.getKey());
        flowPo.setAmount(po.getAmount());
        Long storeId = ThreadContext.getUserStoreId();
        ensureEntityExist(storeId, "当前用户不属于任何店铺");
        flowPo.setStoreId(storeId);
        record = productFlowMapper.insert(flowPo);
        ensureConditionSatisfied(record == 1, "产品流水生成失败");
        return newPo;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProductAmountPo addProductAmount(ProductAmountPo po) {
        ProductAmountPo amountPo = productAmountMapper.selectById(po.getId());
        ensureEntityExist(amountPo, "产品不存在");
        Long storeId = ThreadContext.getUserStoreId();
        ensureParameterExist(storeId, "当前用户不属于任何店铺");
        po.setStoreId(storeId);
        po.setProductId(amountPo.getProductId());
        int record = productAmountMapper.addAmount(po);
        ensureConditionSatisfied(record == 1, "产品数量修改失败");
        ProductFlowPo flowPo = new ProductFlowPo();
        flowPo.setProductId(amountPo.getProductId());
        flowPo.setType(FlowTypeEnum.BUYING_IN.getKey());
        flowPo.setAmount(po.getAmount());
        flowPo.setStoreId(storeId);
        record = productFlowMapper.insert(flowPo);
        ensureConditionSatisfied(record == 1, "产品流水生成失败");
        return productAmountMapper.selectById(po.getId());
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void reduceProductAmount(ProductFlowPo flowPo) {
        ensureParameterExist(flowPo.getProductId(), "产品不存在");
        ensureParameterExist(flowPo.getRecordId(), "消费记录id为空");
        ensureParameterExist(flowPo.getAmount(), "产品消耗为空");
        Long storeId = ThreadContext.getUserStoreId();
        ensureParameterExist(storeId, "当前用户不属于任何店铺");
        flowPo.setStoreId(storeId);
        flowPo.setType(FlowTypeEnum.SELLING_OUT.getKey());
        int record = productFlowMapper.insert(flowPo);
        ensureConditionSatisfied(record == 1, "产品流水生成失败");
        ProductAmountPo amountPo = productAmountMapper.selectByProductAndStore(flowPo.getProductId(), storeId);
        ensureEntityExist(amountPo, "无对应产品");
        ensureConditionSatisfied(amountPo.getAmount().compareTo(flowPo.getAmount()) == 0
                || amountPo.getAmount().compareTo(flowPo.getAmount()) == 1, "产品余量不足,请及时补充");
        ProductAmountPo po = new ProductAmountPo();
        po.setId(amountPo.getId());
        po.setAmount(flowPo.getAmount());
        record = productAmountMapper.reduceAmount(po);
        ensureConditionSatisfied(record == 1, "库存数量修改失败");

    }
    
    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void modifyFlowInvalid(Long recordId) {
        ensureParameterExist(recordId, "消费记录不存在");
        Long storeId = ThreadContext.getUserStoreId();
        ensureEntityExist(storeId, "当前用户不属于任何店铺");
        List<ProductFlowPo> pos = productFlowMapper.selectByRecordId(recordId, storeId);
        ensureCollectionNotEmpty(pos, "消费记录没有产品消耗记录");
        List<ProductAmountPo> amountPos = Lists.newArrayListWithCapacity(pos.size());
        for (ProductFlowPo po : pos) {
            ProductAmountPo amountPo = new ProductAmountPo();
            amountPo.setProductId(po.getProductId());
            amountPo.setStoreId(po.getStoreId());
            amountPo.setAmount(po.getAmount());
            amountPos.add(amountPo);
        }
        int records = productAmountMapper.batchAddAmountByProductAndStore(amountPos);
        ensureConditionSatisfied(records == amountPos.size(), "产品数量修改失败");
        int record = productFlowMapper.updateStatusByRecordId(recordId);
        ensureConditionSatisfied(record == 1, "库存流水置为无效失败");
    }

    @Override
    public ProductAmountPo queryProductAmountByCode(String code) {
        ensureParameterExist(code, "产品编码为空");
        ProductAmountPo po = productAmountMapper.selectByCode(code);
        return po;
    }

    @Override
    public ProductUnion queryProductDetailWithAmount(Long id) {
        ProductUnion union = productUnionMapper.selectWithAmountByAmountId(id);
        ensureEntityExist(union, "产品不存在");
        ensureEntityExist(union.getId(), "产品不存在");
        ensureEntityExist(union.getProductTypePo(), "产品分类不存在");
        ensureEntityExist(union.getProductPo(), "产品不存在");
        ensureEntityExist(union.getProductAmountPo(), "产品数量不存在");
        return union;
    }

    @Override
    public PagedList<ProductUnion> listPagedProductsWithAmount(ProductQueryCondition condition) {
        condition.setStoreId(ThreadContext.getUserStoreId());
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<ProductUnion> unions = productUnionMapper.selectWithAmountByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<ProductUnion> info = new PageInfo<>(unions);
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), unions);
    }

}
