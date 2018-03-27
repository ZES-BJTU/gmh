package com.zes.squad.gmh.service;

import java.util.List;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.ProductConvertStockQueryCondition;
import com.zes.squad.gmh.entity.po.ProductConvertStockPo;
import com.zes.squad.gmh.entity.union.ProductConvertStockUnion;

public interface ProductConvertStockService {

    /**
     * 新建产品库存转换
     * 
     * @param po
     * @return
     */
    ProductConvertStockPo createProductConvertStock(ProductConvertStockPo po);

    /**
     * 删除产品库存转换
     * 
     * @param id
     */
    void removeProductConvertStock(Long id);

    /**
     * 修改产品库存转换
     * 
     * @param id
     */
    ProductConvertStockPo modifyProductConvertStock(ProductConvertStockPo po);

    /**
     * 按条件分页查询产品库存转换
     * 
     * @param condition
     * @return
     */
    PagedList<ProductConvertStockUnion> listPagedProductConvertStocks(ProductConvertStockQueryCondition condition);

    /**
     * 查询所有产品库存转换
     * 
     * @param condition
     * @return
     */
    List<ProductConvertStockUnion> listAllProductConvertStocks();

}
