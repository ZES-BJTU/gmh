package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.condition.EmployeeWorkQueryCondition;
import com.zes.squad.gmh.entity.po.EmployeeWorkPo;

public interface EmployeeWorkMapper {

    /**
     * 批量新增员工工种
     * 
     * @param pos
     * @return
     */
    int batchInsert(List<EmployeeWorkPo> pos);

    /**
     * 根据员工id批量删除
     * 
     * @param employeeId
     * @return
     */
    int batchDelete(Long employeeId);

    /**
     * 根据条件分页模糊查询员工信息
     * 
     * @param condition
     * @return
     */
    List<Long> selectEmployeeIdsByCondition(EmployeeWorkQueryCondition condition);

}
