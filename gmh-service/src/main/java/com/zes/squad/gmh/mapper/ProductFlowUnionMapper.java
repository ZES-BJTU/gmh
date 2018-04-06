package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.union.ProductFlowUnion;

public interface ProductFlowUnionMapper {

    /**
     * 查询所有产品流水
     * 
     * @return
     */
    List<ProductFlowUnion> selectAll();

}
