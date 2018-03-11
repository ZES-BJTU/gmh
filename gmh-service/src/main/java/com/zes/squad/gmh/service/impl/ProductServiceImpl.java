package com.zes.squad.gmh.service.impl;

import static com.zes.squad.gmh.common.helper.LogicHelper.*;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.ProductQueryCondition;
import com.zes.squad.gmh.entity.condition.ProductTypeQueryCondition;
import com.zes.squad.gmh.entity.po.ProductPo;
import com.zes.squad.gmh.entity.po.ProductTypePo;
import com.zes.squad.gmh.entity.union.ProductUnion;
import com.zes.squad.gmh.mapper.ProductMapper;
import com.zes.squad.gmh.mapper.ProductTypeMapper;
import com.zes.squad.gmh.mapper.ProductUnionMapper;
import com.zes.squad.gmh.service.ProductService;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductTypeMapper  productTypeMapper;
    @Autowired
    private ProductMapper      productMapper;
    @Autowired
    private ProductUnionMapper productUnionMapper;

    @Override
    public ProductTypePo createProductType(ProductTypePo po) {
        ProductTypePo existedTypePo = productTypeMapper.selectByName(po.getName());
        ensureEntityNotExist(existedTypePo, "产品分类已存在");
        productTypeMapper.insert(po);
        return po;
    }

    @Override
    public void removeProductType(Long id) {
        productTypeMapper.deleteById(id);
    }

    @Override
    public void removeProductTypes(List<Long> ids) {
        productTypeMapper.batchDelete(ids);
    }

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

    @Override
    public ProductPo createProduct(ProductPo po) {
        po.setStoreId(ThreadContext.getUserStoreId());
        ProductTypePo typePo = productTypeMapper.selectById(po.getProductTypeId());
        ensureEntityExist(typePo, "产品分类不存在");
        productMapper.insert(po);
        return po;
    }

    @Override
    public void removeProduct(Long id) {
        productMapper.deleteById(id);
    }

    @Override
    public void removeProducts(List<Long> ids) {
        productMapper.batchDelete(ids);
    }

    @Override
    public ProductPo modifyProduct(ProductPo po) {
        productMapper.updateSelective(po);
        ProductPo newPo = productMapper.selectById(po.getId());
        ensureEntityExist(newPo, "产品不存在");
        return newPo;
    }

    @Override
    public ProductUnion queryProductDetail(Long id) {
        return productUnionMapper.selectById(id);
    }

    @Override
    public PagedList<ProductUnion> listPagedProducts(ProductQueryCondition condition) {
        return null;
    }

}
