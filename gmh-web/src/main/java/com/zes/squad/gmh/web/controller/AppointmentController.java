package com.zes.squad.gmh.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.OnMessage;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.enums.AppointmentStatusEnum;
import com.zes.squad.gmh.common.enums.GenderEnum;
import com.zes.squad.gmh.common.enums.YesOrNoEnum;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.entity.condition.AppointmentQueryCondition;
import com.zes.squad.gmh.entity.po.AppointmentPo;
import com.zes.squad.gmh.entity.union.AppointmentUnion;
import com.zes.squad.gmh.entity.union.EmployeeTimeTable;
import com.zes.squad.gmh.service.AppointmentService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.AppointmentCreateOrModifyParams;
import com.zes.squad.gmh.web.entity.param.AppointmentQueryParams;
import com.zes.squad.gmh.web.entity.param.QueryEmployeeTimeTableParams;
import com.zes.squad.gmh.web.entity.vo.AppointmentVo;
import com.zes.squad.gmh.web.helper.CheckHelper;

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
        appointmentService.createAppointment(appointmentPo, params.getAppointmentProjectParams());
        ;
        return JsonResults.success();
    }

    @RequestMapping(path = "/cancel", method = { RequestMethod.POST })
    public JsonResult<Void> doCancleAppointment(@RequestBody Long appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        return JsonResults.success();
    }

    @RequestMapping(path = "/finish", method = { RequestMethod.POST })
    public JsonResult<Void> doFinishAppointment(@RequestBody Long appointmentId) {
        appointmentService.finishAppointment(appointmentId);
        return JsonResults.success();
    }

    @RequestMapping(path = "/list", method = { RequestMethod.PUT })
    public JsonResult<PagedList<AppointmentVo>> doListPagedAppointment(@RequestBody AppointmentQueryParams params) {
        CheckHelper.checkPageParams(params);
        AppointmentQueryCondition appointmentQueryCondition = CommonConverter.map(params,
                AppointmentQueryCondition.class);
        PagedList<AppointmentUnion> pagedUnions = appointmentService.listPagedAppointments(appointmentQueryCondition);
        if (CollectionUtils.isEmpty(pagedUnions.getData())) {
            return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize()));
        }
        List<AppointmentVo> appointmentVos = new ArrayList<AppointmentVo>();
        for (AppointmentUnion appointmentUnion : pagedUnions.getData()) {
            AppointmentVo appontmentVo = buildAppointmentVoByUnion(appointmentUnion);
            appointmentVos.add(appontmentVo);
        }
        return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
                pagedUnions.getTotalCount(), appointmentVos));
    }
    
    @RequestMapping(path = "/queryEmployeeTimeTable", method = { RequestMethod.POST })
    public JsonResult<List<EmployeeTimeTable>> queryEmployeeTimeTable(@RequestBody QueryEmployeeTimeTableParams params) {
    	List<EmployeeTimeTable> timeTableList = appointmentService.queryEmployeeTimeTable(params.getEmployeeId(), params.getDate());
        return JsonResults.success(timeTableList);
    }

    private AppointmentVo buildAppointmentVoByUnion(AppointmentUnion appointmentUnion) {
        AppointmentVo vo = CommonConverter.map(appointmentUnion.getAppointmentPo(), AppointmentVo.class);
        vo.setCustomerGender(
                EnumUtils.getDescByKey(appointmentUnion.getAppointmentPo().getCustomerGender(), GenderEnum.class));
        vo.setIsVip(EnumUtils.getDescByKey(appointmentUnion.getAppointmentPo().getIsVip(), YesOrNoEnum.class));
        vo.setIsLine(EnumUtils.getDescByKey(appointmentUnion.getAppointmentPo().getIsLine(), YesOrNoEnum.class));
        vo.setStatus(
                EnumUtils.getDescByKey(appointmentUnion.getAppointmentPo().getStatus(), AppointmentStatusEnum.class));
        vo.setAppointmentProjects(appointmentUnion.getAppointmentProjects());
        return vo;
    }
}
