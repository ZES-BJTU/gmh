package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.condition.EmployeeQueryCondition;
import com.zes.squad.gmh.entity.po.EmployeePo;

public interface EmployeeMapper {

    /**
     * 新增单个员工
     * 
     * @param po
     * @return
     */
    int insert(EmployeePo po);

    /**
     * 根据id删除单个员工
     * 
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 根据id集合批量删除员工
     * 
     * @param ids
     * @return
     */
    int batchDelete(List<Long> ids);

    /**
     * 修改单个员工信息
     * 
     * @param po
     * @return
     */
    int updateSelective(EmployeePo po);

    /**
     * 根据查询条件查询员工id集合
     * 
     * @param condition
     * @return
     */
    List<Long> selectIdsByCondition(EmployeeQueryCondition condition);

}
