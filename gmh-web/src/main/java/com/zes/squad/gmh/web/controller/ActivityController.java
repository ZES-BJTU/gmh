package com.zes.squad.gmh.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zes.squad.gmh.service.ActivityService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.vo.ActivityVo;

@RequestMapping(path = "/activities")
@RestController
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @RequestMapping(path = "/activities", method = { RequestMethod.POST })
    public JsonResult<ActivityVo> doCreateActivity() {
        activityService.createActivity(null);
        return JsonResults.success();
    }

}
