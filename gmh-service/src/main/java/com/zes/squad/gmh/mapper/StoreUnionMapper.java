package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.condition.StoreQueryCondition;
import com.zes.squad.gmh.entity.union.StoreUnion;

public interface StoreUnionMapper {

    /**
     * 查询单个门店信息
     * 
     * @param id
     * @return
     */
    StoreUnion selectById(Long id);

    /**
     * 根据条件查询门店信息
     * 
     * @param condition
     * @return
     */
    List<StoreUnion> selectByCondition(StoreQueryCondition condition);

}
