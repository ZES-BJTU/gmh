package com.zes.squad.gmh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.entity.condition.EmployeeWorkQueryCondition;
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
     * 根据id查询员工
     * 
     * @param id
     * @return
     */
    EmployeePo selectById(Long id);

    /**
     * 根据手机号查询员工
     * 
     * @param mobile
     * @return
     */
    EmployeePo selectByMobile(String mobile);

    /**
     * 根据条件分页模糊查询员工信息
     * 
     * @param condition
     * @return
     */
    List<Long> selectEmployeeIdsByCondition(EmployeeWorkQueryCondition condition);

    /**
     * 根据工种和门店查询
     * 
     * @param workType
     * @param storeId
     * @return
     */
    List<EmployeePo> selectByWorkType(@Param("workType") Integer workType, @Param("storeId") Long storeId);

    /**
     * 根据工种和门店查询
     * 
     * @param workType
     * @param storeId
     * @return
     */
    List<EmployeePo> selectByWorkTypes(@Param("workTypes") List<Integer> workTypes, @Param("storeId") Long storeId);

    List<Long> getOfficialOperatorId(Long storeId);

    List<Long> getInternOperatorId(Long storeId);

}
