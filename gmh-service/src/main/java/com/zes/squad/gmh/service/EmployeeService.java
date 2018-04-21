package com.zes.squad.gmh.service;

import java.util.List;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.EmployeeWorkQueryCondition;
import com.zes.squad.gmh.entity.po.EmployeePo;
import com.zes.squad.gmh.entity.union.EmployeeUnion;

public interface EmployeeService {

    /**
     * 新建员工
     * 
     * @param union
     */
    EmployeeUnion createEmployee(EmployeeUnion union);

    /**
     * 删除单个员工
     * 
     * @param id
     */
    void removeEmployee(Long id);

    /**
     * 删除多个员工
     * 
     * @param ids
     */
    void removeEmployees(List<Long> ids);

    /**
     * 修改员工信息
     * 
     * @param union
     */
    EmployeeUnion modifyEmployee(EmployeeUnion union);

    /**
     * 查询员工详情
     * 
     * @param id
     * @return
     */
    EmployeeUnion queryEmployeeDetail(Long id);

    /**
     * 根据条件分页模糊查询员工
     * 
     * @param condition
     * @return
     */
    PagedList<EmployeeUnion> listPagedEmployees(EmployeeWorkQueryCondition condition);

    /**
     * 根据工种查询员工
     * 
     * @param workType
     * @return
     */
    List<EmployeePo> listEmployeesByWorkType(Integer workType);

    /**
     * 根据工种查询员工
     * 
     * @param workType
     * @return
     */
    List<EmployeePo> listEmployeesByWorkTypes(List<Integer> workTypes);

}
