package com.zes.squad.gmh.service;

import java.util.List;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.ProductTypeQueryCondition;
import com.zes.squad.gmh.entity.po.ProductTypePo;

public interface ProductService {

    /**
     * 创建库存分类
     * 
     * @param po
     * @return
     */
    ProductTypePo createProductType(ProductTypePo po);

    /**
     * 删除单个库存分类
     * 
     * @param id
     */
    void removeProductType(Long id);

    /**
     * 删除多个库存分类
     * 
     * @param ids
     */
    void removeProductTypes(List<Long> ids);

    /**
     * 修改库存分类
     * 
     * @param po
     * @return
     */
    ProductTypePo modifyProductType(ProductTypePo po);

    /**
     * 查询单个库存分类详情
     * 
     * @param id
     * @return
     */
    ProductTypePo queryProductTypeDetail(Long id);

    /**
     * 根据条件查询多个库存分类详情
     * 
     * @param condition
     * @return
     */
    PagedList<ProductTypePo> listPagedProductTypes(ProductTypeQueryCondition condition);

}
