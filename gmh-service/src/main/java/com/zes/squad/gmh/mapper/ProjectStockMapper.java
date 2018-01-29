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

}
