package com.zes.squad.gmh.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.ActivityQueryCondition;
import com.zes.squad.gmh.entity.union.ActivityUnion;
import com.zes.squad.gmh.mapper.ActivityContentMapper;
import com.zes.squad.gmh.mapper.ActivityMapper;
import com.zes.squad.gmh.service.ActivityService;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper        activityMapper;
    @Autowired
    private ActivityContentMapper activityContentMapper;

    @Override
    public ActivityUnion createActivity(ActivityUnion union) {
        return null;
    }

    @Override
    public void removeActivity(Long id) {

    }

    @Override
    public void removeActivities(List<Long> ids) {

    }

    @Override
    public ActivityUnion modifyActivity(ActivityUnion union) {
        return null;
    }

    @Override
    public ActivityUnion queryActivityDetail(Long id) {
        return null;
    }

    @Override
    public PagedList<ActivityUnion> listPagedActivities(ActivityQueryCondition condition) {
        return null;
    }

}
