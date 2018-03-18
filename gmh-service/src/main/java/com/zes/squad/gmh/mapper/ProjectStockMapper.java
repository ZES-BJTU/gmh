package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.po.ProjectStockPo;

public interface ProjectStockMapper {

    /**
     * 批量插入项目需要库存
     * 
     * @param pos
     * @return
     */
    int batchInsert(List<ProjectStockPo> pos);

    /**
     * 根据项目id批量删除
     * 
     * @param projectIds
     * @return
     */
    int batchDeleteByProjectIds(List<Long> projectIds);

    /**
     * 根据项目id查询
     * 
     * @param projectId
     * @return
     */
    List<ProjectStockPo> selectByProjectId(Long projectId);

    /**
     * 根据项目id查找所需消耗库存
     * 
     * @param projectIds
     * @return
     */
    List<ProjectStockPo> getProjectStockByProId(Long projectId);
}
