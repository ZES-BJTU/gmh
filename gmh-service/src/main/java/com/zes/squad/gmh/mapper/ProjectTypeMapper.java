package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.po.ProjectTypePo;

public interface ProjectTypeMapper {

    /**
     * 插入单个项目类型
     * 
     * @param po
     * @return
     */
    int insert(ProjectTypePo po);

    /**
     * 根据id删除
     * 
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 批量删除项目类型
     * 
     * @param ids
     * @return
     */
    int batchDelete(List<Long> ids);

    /**
     * 更新项目类型
     * 
     * @param po
     * @return
     */
    int updateSelective(ProjectTypePo po);

    /**
     * 根据id集合查询
     * 
     * @param ids
     * @return
     */
    int selectByIds(List<Long> ids);

}
