package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.condition.StockQueryCondition;
import com.zes.squad.gmh.entity.union.StockUnion;

public interface StockUnionMapper {

    /**
     * 根据id查询
     * 
     * @param id
     * @return
     */
    StockUnion selectById(Long id);

    /**
     * 根据条件模糊查询
     * 
     * @param condition
     * @return
     */
    List<StockUnion> selectByCondition(StockQueryCondition condition);

}
