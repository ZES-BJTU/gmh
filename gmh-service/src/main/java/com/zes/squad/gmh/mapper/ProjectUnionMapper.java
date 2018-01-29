package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.union.ProjectUnion;

public interface ProjectUnionMapper {

    /**
     * 根据id查询单个项目
     * 
     * @param id
     * @return
     */
    ProjectUnion selectById(Long id);

    /**
     * 根据id集合查询多个项目
     * 
     * @param ids
     * @return
     */
    List<ProjectUnion> selectByIds(List<Long> ids);

}
