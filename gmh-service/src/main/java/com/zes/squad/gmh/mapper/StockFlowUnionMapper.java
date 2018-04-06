package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.union.StockFlowUnion;

public interface StockFlowUnionMapper {

    /**
     * 查询所有流水
     * 
     * @return
     */
    List<StockFlowUnion> selectAll();

}
