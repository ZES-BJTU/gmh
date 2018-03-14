package com.zes.squad.gmh.service;

import com.zes.squad.gmh.entity.union.ActivityUnion;

public interface ActivityService {

    /**
     * 创建活动
     * 
     * @param union
     * @return
     */
    ActivityUnion createActivity(ActivityUnion union);

}
