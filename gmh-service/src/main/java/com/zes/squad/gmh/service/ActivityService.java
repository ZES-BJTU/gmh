package com.zes.squad.gmh.service;

import java.util.List;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.ActivityQueryCondition;
import com.zes.squad.gmh.entity.union.ActivityUnion;

public interface ActivityService {

    /**
     * 创建活动
     * 
     * @param union
     * @return
     */
    ActivityUnion createActivity(ActivityUnion union);

    /**
     * 删除活动
     * 
     * @param id
     */
    void removeActivity(Long id);

    /**
     * 删除活动(多个)
     * 
     * @param ids
     */
    void removeActivities(List<Long> ids);

    /**
     * 修改活动
     * 
     * @param union
     * @return
     */
    ActivityUnion modifyActivity(ActivityUnion union);

    /**
     * 查询活动详情
     * 
     * @param id
     * @return
     */
    ActivityUnion queryActivityDetail(Long id);

    /**
     * 分页查询活动信息
     * 
     * @param condition
     * @return
     */
    PagedList<ActivityUnion> listPagedActivities(ActivityQueryCondition condition);

}
