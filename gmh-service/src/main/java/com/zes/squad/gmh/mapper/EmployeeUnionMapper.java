package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.union.EmployeeUnion;

public interface EmployeeUnionMapper {

    /**
     * 根据id查询员工信息
     * 
     * @param id
     * @return
     */
    EmployeeUnion selectById(Long id);

    /**
     * 根据id批量查询员工
     * 
     * @param ids
     * @return
     */
    List<EmployeeUnion> selectByIds(List<Long> ids);

}
