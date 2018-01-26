package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.condition.EmployeeWorkTypeQueryCondition;
import com.zes.squad.gmh.entity.union.EmployeeWorkTypeUnion;

public interface EmployeeWorkTypeUnionMapper {

    /**
     * 查询单个工种
     * 
     * @param id
     * @return
     */
    EmployeeWorkTypeUnion selectById(Long id);

    /**
     * 条件查询多个工种
     * 
     * @param condition
     * @return
     */
    List<EmployeeWorkTypeUnion> selectByCondition(EmployeeWorkTypeQueryCondition condition);

}
