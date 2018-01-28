package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.condition.ProjectTypeQueryCondition;
import com.zes.squad.gmh.entity.union.ProjectTypeUnion;

public interface ProjectTypeUnionMapper {

    /**
     * 根据id查询项目分类详情
     * 
     * @param id
     * @return
     */
    ProjectTypeUnion selectById(Long id);

    /**
     * 根绝条件查询项目分类详情
     * 
     * @param condition
     * @return
     */
    List<ProjectTypeUnion> selectByCondition(ProjectTypeQueryCondition condition);

}
