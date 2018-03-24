package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.po.ActivityContentPo;

public interface ActivityContentMapper {

    /**
     * 批量插入活动内容
     * 
     * @param pos
     * @return
     */
    int batchInsert(List<ActivityContentPo> pos);

    /**
     * 批量删除活动内容
     * 
     * @param activityIds
     * @return
     */
    int batchDeleteByActivityId(List<Long> activityIds);

    /**
     * 根据活动id查询
     * 
     * @param activityId
     * @return
     */
    List<ActivityContentPo> selectByActivityId(Long activityId);

}
