package com.zes.squad.gmh.service;

import java.util.List;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.ProjectTypeQueryCondition;
import com.zes.squad.gmh.entity.po.ProjectTypePo;
import com.zes.squad.gmh.entity.union.ProjectTypeUnion;

public interface ProjectService {

    /**
     * 新建项目分类
     * 
     * @param po
     */
    void createProjectType(ProjectTypePo po);

    /**
     * 删除单个项目分类
     * 
     * @param id
     */
    void removeProjectType(Long id);

    /**
     * 删除多个项目分类
     * 
     * @param ids
     */
    void removeProjectTypes(List<Long> ids);

    /**
     * 修改项目分类
     * 
     * @param po
     */
    void modifyProjectType(ProjectTypePo po);

    /**
     * 查询项目分类详情
     * 
     * @param id
     * @return
     */
    ProjectTypeUnion queryProjectTypeDetail(Long id);

    /**
     * 分页模糊搜索项目分类
     * 
     * @param condition
     * @return
     */
    PagedList<ProjectTypeUnion> listPagedProjectTypes(ProjectTypeQueryCondition condition);

}
