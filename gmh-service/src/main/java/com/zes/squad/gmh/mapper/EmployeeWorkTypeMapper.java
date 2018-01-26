package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.po.EmployeeWorkTypePo;

public interface EmployeeWorkTypeMapper {

    /**
     * 新建单个工种
     * 
     * @param po
     * @return
     */
    int insert(EmployeeWorkTypePo po);

    /**
     * 删除单个工种
     * 
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 批量删除工种
     * 
     * @param ids
     * @return
     */
    int batchDelete(List<Long> ids);

    /**
     * 更新工种
     * 
     * @param po
     * @return
     */
    int updateSelective(EmployeeWorkTypePo po);

}
