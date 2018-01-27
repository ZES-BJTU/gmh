package com.zes.squad.gmh.service;

import java.util.List;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.EmployeeQueryCondition;
import com.zes.squad.gmh.entity.condition.EmployeeWorkTypeQueryCondition;
import com.zes.squad.gmh.entity.po.EmployeeWorkTypePo;
import com.zes.squad.gmh.entity.union.EmployeeUnion;
import com.zes.squad.gmh.entity.union.EmployeeWorkTypeUnion;

public interface EmployeeService {

    /**
     * 新建员工工种
     * 
     * @param po
     */
    void createEmployeeWorkType(EmployeeWorkTypePo po);

    /**
     * 删除单个员工工种
     * 
     * @param id
     */
    void removeEmployeeWrokType(Long id);

    /**
     * 删除多个员工工种
     * 
     * @param ids
     */
    void removeEmployeeWorkTypes(List<Long> ids);

    /**
     * 修改员工工种
     * 
     * @param po
     */
    void modifyEmployeeWorkType(EmployeeWorkTypePo po);

    /**
     * 查询员工工种详情
     * 
     * @param id
     * @return
     */
    EmployeeWorkTypeUnion queryEmployeeWorkTypeDetail(Long id);

    /**
     * 模糊搜索员工工种
     * 
     * @param condition
     * @return
     */
    PagedList<EmployeeWorkTypeUnion> listPagedEmployeeWorkTypes(EmployeeWorkTypeQueryCondition condition);

    /**
     * 新建员工
     * 
     * @param union
     */
    void createEmployee(EmployeeUnion union);

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
    void modifyEmployee(EmployeeUnion union);

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
    PagedList<EmployeeUnion> listPagedEmployees(EmployeeQueryCondition condition);

}
