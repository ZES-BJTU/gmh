package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.condition.ProjectQueryCondition;
import com.zes.squad.gmh.entity.po.ProjectPo;

public interface ProjectMapper {

    /**
     * 新增单个项目
     * 
     * @param po
     * @return
     */
    int insert(ProjectPo po);

    /**
     * 删除单个项目
     * 
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 批量删除项目
     * 
     * @param ids
     * @return
     */
    int batchDelete(List<Long> ids);

    /**
     * 修改项目
     * 
     * @param po
     * @return
     */
    int updateSelective(ProjectPo po);

    /**
     * 根据条件查询id集合
     * 
     * @param condition
     * @return
     */
    List<Long> selectIdsByCondition(ProjectQueryCondition condition);

}
