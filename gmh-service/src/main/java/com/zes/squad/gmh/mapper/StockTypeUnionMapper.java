package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.condition.StockTypeQueryCondition;
import com.zes.squad.gmh.entity.union.StockTypeUnion;

public interface StockTypeUnionMapper {

    /**
     * 根据库存分类id查询
     * 
     * @param id
     * @return
     */
    StockTypeUnion selectById(Long id);

    /**
     * 根据库存分类查询条件查询
     * 
     * @param condition
     * @return
     */
    List<StockTypeUnion> selectByCondition(StockTypeQueryCondition condition);

}
