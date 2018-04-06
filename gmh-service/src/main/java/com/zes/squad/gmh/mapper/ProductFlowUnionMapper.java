package com.zes.squad.gmh.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.entity.union.ProductFlowUnion;

public interface ProductFlowUnionMapper {

    /**
     * 查询所有流水
     * 
     * @param storeId
     * @param beginTime
     * @param endTime
     * @return
     */
    List<ProductFlowUnion> selectAll(@Param("storeId") Long storeId, @Param("beginTime") Date beginTime,
                                     @Param("endTime") Date endTime);

}
