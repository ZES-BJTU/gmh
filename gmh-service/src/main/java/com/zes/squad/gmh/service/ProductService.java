package com.zes.squad.gmh.service;

import java.util.List;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.ProductQueryCondition;
import com.zes.squad.gmh.entity.condition.ProductTypeQueryCondition;
import com.zes.squad.gmh.entity.po.ProductAmountPo;
import com.zes.squad.gmh.entity.po.ProductPo;
import com.zes.squad.gmh.entity.po.ProductTypePo;
import com.zes.squad.gmh.entity.union.ProductUnion;

public interface ProductService {

    /**
     * 创建产品分类
     * 
     * @param po
     * @return
     */
    ProductTypePo createProductType(ProductTypePo po);

    /**
     * 删除单个产品分类
     * 
     * @param id
     */
    void removeProductType(Long id);

    /**
     * 删除多个产品分类
     * 
     * @param ids
     */
    void removeProductTypes(List<Long> ids);

    /**
     * 修改产品分类
     * 
     * @param po
     * @return
     */
    ProductTypePo modifyProductType(ProductTypePo po);

    /**
     * 查询单个产品分类详情
     * 
     * @param id
     * @return
     */
    ProductTypePo queryProductTypeDetail(Long id);

    /**
     * 根据条件查询多个产品分类详情
     * 
     * @param condition
     * @return
     */
    PagedList<ProductTypePo> listPagedProductTypes(ProductTypeQueryCondition condition);

    /**
     * 新建产品
     * 
     * @param po
     * @return
     */
    ProductPo createProduct(ProductPo po);

    /**
     * 删除单个产品
     * 
     * @param id
     */
    void removeProduct(Long id);

    /**
     * 删除多个产品
     * 
     * @param ids
     */
    void removeProducts(List<Long> ids);

    /**
     * 修改产品
     * 
     * @param po
     * @return
     */
    ProductPo modifyProduct(ProductPo po);

    /**
     * 查询产品详情
     * 
     * @param id
     * @return
     */
    ProductUnion queryProductDetail(Long id);

    /**
     * 根据搜索条件分页显示产品
     * 
     * @param condition
     * @return
     */
    PagedList<ProductUnion> listPagedProducts(ProductQueryCondition condition);

    /**
     * @param po
     * @return
     */
    ProductAmountPo createProductAmount(ProductAmountPo po);

    /**
     * @param id
     */
    void removeProductAmount(Long id);

    /**
     * @param ids
     */
    void removeProductAmounts(List<Long> ids);

    /**
     * 添加产品数量(进货)
     * 
     * @param po
     * @return
     */
    ProductAmountPo modifyProductAmount(ProductAmountPo po);

    /**
     * 查询产品详情(带数量)
     * 
     * @param id
     * @return
     */
    ProductUnion queryProductDetailWithAmount(Long id);

    /**
     * 分页查询产品详情(带数量)
     * 
     * @param condition
     * @return
     */
    PagedList<ProductUnion> listPagedProductsWithAmount(ProductQueryCondition condition);

}
