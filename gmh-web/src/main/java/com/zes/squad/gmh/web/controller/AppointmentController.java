package com.zes.squad.gmh.web.controller;

import java.util.List;

import javax.websocket.OnMessage;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.EmployeeWorkQueryCondition;
import com.zes.squad.gmh.entity.po.AppointmentPo;
import com.zes.squad.gmh.entity.union.EmployeeUnion;
import com.zes.squad.gmh.service.AppointmentService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.AppointmentCreateOrModifyParams;
import com.zes.squad.gmh.web.entity.param.AppointmentQueryParams;
import com.zes.squad.gmh.web.entity.param.EmployeeWorkQueryParams;
import com.zes.squad.gmh.web.entity.vo.AppointmentVo;
import com.zes.squad.gmh.web.entity.vo.EmployeeVo;

@RequestMapping(path = "/appointment")
@RestController
public class AppointmentController {
	@Autowired
    private AppointmentService appointmentService;
	
    @OnMessage
    public JsonResult<AppointmentVo> doRemindReception() {
        return JsonResults.success();
    }

    @RequestMapping(path = "/create", method = { RequestMethod.PUT })
    public JsonResult<Void> doCreateAppointment(@RequestBody AppointmentCreateOrModifyParams params) {
    	AppointmentPo appointmentPo = CommonConverter.map(params, AppointmentPo.class);
    	appointmentService.createAppointment(appointmentPo, params.getAppointmentProjectParams());;
        return JsonResults.success();
    }
    
    @RequestMapping(path = "/cancel", method = {RequestMethod.POST})
    public JsonResult<Void> doCancleAppointment(@RequestBody Long appointmentId){
    	appointmentService.cancelAppointment(appointmentId);
    	return JsonResults.success();
    }
    @RequestMapping(path = "/finish", method = {RequestMethod.POST})
    public JsonResult<Void> doFinishAppointment(@RequestBody Long appointmentId){
    	appointmentService.finishAppointment(appointmentId);
    	return JsonResults.success();
    }
    @RequestMapping(path = "/list", method = { RequestMethod.GET })
    public JsonResult<PagedList<AppointmentVo>> doListPagedAppointment(@RequestBody AppointmentQueryParams params) {
       
        return JsonResults.success();
    }
}
