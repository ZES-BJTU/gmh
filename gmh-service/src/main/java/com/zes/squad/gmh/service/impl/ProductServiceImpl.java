package com.zes.squad.gmh.service.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.zes.squad.gmh.common.helper.LogicHelper.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.ProductTypeQueryCondition;
import com.zes.squad.gmh.entity.po.ProductTypePo;
import com.zes.squad.gmh.mapper.ProductTypeMapper;
import com.zes.squad.gmh.service.ProductService;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductTypeMapper productTypeMapper;

    @Override
    public ProductTypePo createProductType(ProductTypePo po) {
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

}
