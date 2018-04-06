package com.zes.squad.gmh.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.ProductConvertStockQueryCondition;
import com.zes.squad.gmh.entity.po.ProductConvertStockFlowPo;
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

    /**
     * 根据转换关系计算数量
     * 
     * @param po
     * @return
     */
    BigDecimal calculateAmount(ProductConvertStockPo po);

    /**
     * 新建产品库存转化流水
     * 
     * @param po
     * @return
     */
    ProductConvertStockFlowPo createProductConvertStockFlow(ProductConvertStockFlowPo po);

    /**
     * 导出产品库存转换流水
     * 
     * @param beginTime
     * @param endTime
     * @return
     */
    Workbook exportProductsStocks(Date beginTime, Date endTime);

}
