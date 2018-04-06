package com.zes.squad.gmh.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.entity.union.StockFlowUnion;

public interface StockFlowUnionMapper {

    /**
     * 查询所有流水
     * 
     * @param beginTime
     * @param endTime
     * @return
     */
    List<StockFlowUnion> selectAll(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

}
