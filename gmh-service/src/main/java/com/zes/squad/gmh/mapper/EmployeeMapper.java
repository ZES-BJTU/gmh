package com.zes.squad.gmh.mapper;

import java.util.List;

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
     * 根据id删除单个员工(逻辑删除,实际设置为离职)
     * 
     * @param id
     * @return
     */
    int updateWorkingById(Long id);

    /**
     * 根据id集合批量删除员工(逻辑删除,实际设置为离职)
     * 
     * @param ids
     * @return
     */
    int batchUpdateWorkingByIds(List<Long> ids);

    /**
     * 修改单个员工信息
     * 
     * @param po
     * @return
     */
    int updateSelective(EmployeePo po);

    /**
     * 根据手机号查询员工
     * 
     * @param mobile
     * @return
     */
    EmployeePo selectByMobile(String mobile);

}
