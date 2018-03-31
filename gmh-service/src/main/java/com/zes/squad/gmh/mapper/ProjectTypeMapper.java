package com.zes.squad.gmh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.entity.condition.ProjectTypeQueryCondition;
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
     * 根据id查询
     * 
     * @param id
     * @return
     */
    ProjectTypePo selectById(Long id);

    /**
     * 根据查询条件查询
     * 
     * @param condition
     * @return
     */
    List<ProjectTypePo> selectByCondition(ProjectTypeQueryCondition condition);

    /**
     * 根据顶层分类和名称查询
     * 
     * @param topType
     * @param name
     * @return
     */
    ProjectTypePo selectByTopTypeAndName(@Param("topType") Integer topType, @Param("name") String name);

    /**
     * 查询所有项目分类
     * 
     * @return
     */
    List<ProjectTypePo> selectAll();

}
