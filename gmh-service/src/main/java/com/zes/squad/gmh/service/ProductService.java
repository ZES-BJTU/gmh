package com.zes.squad.gmh.service;

import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.ProductQueryCondition;
import com.zes.squad.gmh.entity.condition.ProductTypeQueryCondition;
import com.zes.squad.gmh.entity.po.ProductAmountPo;
import com.zes.squad.gmh.entity.po.ProductFlowPo;
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
     * 查询所有产品分类
     * 
     * @return
     */
    List<ProductTypePo> listAllProductTypes();

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
     * 查询所有产品
     * 
     * @return
     */
    List<ProductPo> listAllProducts();

    /**
     * 查询本门店所有产品
     * 
     * @return
     */
    List<ProductPo> listStoreAllProducts();

    /**
     * 添加商品数量
     * 
     * @param po
     * @return
     */
    ProductAmountPo createProductAmount(ProductAmountPo po);

    /**
     * 删除单个商品数量
     * 
     * @param id
     */
    void removeProductAmount(Long id);

    /**
     * 删除多个商品数量
     * 
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
     * 进货
     * 
     * @param po
     */
    ProductAmountPo addProductAmount(ProductAmountPo po);

    /**
     * 卖货
     * 
     * @param flowPo
     */
    void reduceProductAmount(ProductFlowPo flowPo);

    /**
     * 根据消费记录id将产品流水置为无效
     * 
     * @param recordId
     */
    void modifyFlowInvalid(Long recordId);

    /**
     * 根据编码查询产品
     * 
     * @param code
     * @return
     */
    ProductAmountPo queryProductAmountByCode(String code);

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

    /**
     * 转出产品
     * 
     * @param po
     */
    void convertProductAmount(ProductAmountPo po);

    /**
     * 导出产品
     * 
     * @param beginTime
     * @param endTime
     * @return
     */
    Workbook exportProducts(Date beginTime, Date endTime);

}
