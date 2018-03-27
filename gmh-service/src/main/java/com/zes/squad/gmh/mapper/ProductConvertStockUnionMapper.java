package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.condition.ProductConvertStockQueryCondition;
import com.zes.squad.gmh.entity.union.ProductConvertStockUnion;

public interface ProductConvertStockUnionMapper {

    /**
     * 条件查询
     * 
     * @param condition
     * @return
     */
    List<ProductConvertStockUnion> selectByCondition(ProductConvertStockQueryCondition condition);

    /**
     * 查询所有
     * 
     * @return
     */
    List<ProductConvertStockUnion> selectAll();

}
