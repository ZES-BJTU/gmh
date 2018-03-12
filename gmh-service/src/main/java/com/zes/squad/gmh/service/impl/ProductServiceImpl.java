package com.zes.squad.gmh.service.impl;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureConditionValid;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityExist;
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
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.ProductAmountQueryCondition;
import com.zes.squad.gmh.entity.condition.ProductQueryCondition;
import com.zes.squad.gmh.entity.condition.ProductTypeQueryCondition;
import com.zes.squad.gmh.entity.po.ProductAmountPo;
import com.zes.squad.gmh.entity.po.ProductPo;
import com.zes.squad.gmh.entity.po.ProductTypePo;
import com.zes.squad.gmh.entity.union.ProductUnion;
import com.zes.squad.gmh.mapper.ProductAmountMapper;
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

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProductTypePo createProductType(ProductTypePo po) {
        ProductTypePo existingTypePo = productTypeMapper.selectByName(po.getName());
        ensureEntityNotExist(existingTypePo, "产品分类已存在");
        productTypeMapper.insert(po);
        return po;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProductType(Long id) {
        int row = productTypeMapper.deleteById(id);
        ensureConditionValid(row == 1, "产品分类删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProductTypes(List<Long> ids) {
        int rows = productTypeMapper.batchDelete(ids);
        ensureConditionValid(rows == ids.size(), "产品分类删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProductTypePo modifyProductType(ProductTypePo po) {
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

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProductPo createProduct(ProductPo po) {
        ProductPo existingPo = productMapper.selectByName(po.getName());
        ensureEntityNotExist(existingPo, "产品已存在");
        ProductTypePo typePo = productTypeMapper.selectById(po.getProductTypeId());
        ensureEntityExist(typePo, "产品分类不存在");
        productMapper.insert(po);
        return po;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProduct(Long id) {
        int row = productMapper.deleteById(id);
        ensureConditionValid(row == 1, "产品删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProducts(List<Long> ids) {
        int rows = productMapper.batchDelete(ids);
        ensureConditionValid(rows == ids.size(), "产品删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProductPo modifyProduct(ProductPo po) {
        if (po.getProductTypeId() != null) {
            ProductTypePo typePo = productTypeMapper.selectById(po.getProductTypeId());
            ensureEntityExist(typePo, "产品分类不存在");
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

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProductAmountPo createProductAmount(ProductAmountPo po) {
        ProductAmountQueryCondition condition = new ProductAmountQueryCondition();
        condition.setProductId(po.getProductId());
        condition.setStoreId(ThreadContext.getUserStoreId());
        ProductAmountPo existingPo = productAmountMapper.selectByCondition(condition);
        ensureEntityNotExist(existingPo, "产品数量已存在");
        productAmountMapper.insert(po);
        return po;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProductAmount(Long id) {
        int row = productAmountMapper.deleteById(id);
        ensureConditionValid(row == 1, "产品数量删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProductAmounts(List<Long> ids) {
        int rows = productAmountMapper.batchDelete(ids);
        ensureConditionValid(rows == ids.size(), "产品数量删除失败");
    }

    @Override
    public ProductUnion queryProductDetailWithAmount(Long id) {
        ProductUnion union = productUnionMapper.selectWithAmountByAmountId(id);
        ensureEntityExist(union, "产品不存在");
        ensureEntityExist(union.getId(), "产品不存在");
        ensureEntityExist(union.getProductTypePo(), "产品分类不存在");
        ensureEntityExist(union.getProductPo(), "产品不存在");
        ensureEntityExist(union.getProductAmountPo(), "产品数量不存在");
        ensureEntityExist(union.getStorePo(), "产品所属门店不存在");
        return union;
    }

    @Override
    public PagedList<ProductUnion> listPagedProductsWithAmount(ProductQueryCondition condition) {
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
