package com.zes.squad.gmh.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.zes.squad.gmh.entity.po.AppointmentProjectPo;
import com.zes.squad.gmh.entity.union.AppointmentUnion;
import com.zes.squad.gmh.entity.union.EmployeeTimeTable;
import com.zes.squad.gmh.entity.union.OperatorTimeTableUnion;
import com.zes.squad.gmh.mapper.AppointmentProjectMapper;
import com.zes.squad.gmh.service.AppointmentService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.AppointmentCreateOrModifyParams;
import com.zes.squad.gmh.web.entity.param.AppointmentQueryParams;
import com.zes.squad.gmh.web.entity.param.QueryEmployeeTimeTableParams;
import com.zes.squad.gmh.web.entity.param.QueryOperatorTimeTableParams;
import com.zes.squad.gmh.web.entity.vo.AppointmentVo;
import com.zes.squad.gmh.web.helper.CheckHelper;

@RequestMapping(path = "/appointment")
@RestController
public class AppointmentController {

    @Autowired
    private AppointmentService       appointmentService;
    @Autowired
    private AppointmentProjectMapper appointmentProjectMapper;

    @RequestMapping(path = "/create", method = { RequestMethod.PUT })
    public JsonResult<Void> doCreateAppointment(@RequestBody AppointmentCreateOrModifyParams params) {
        if (params.getAppointmentProjectParams() == null) {
            return JsonResults.fail(1000, "未添加项目");
        }
        if (params.getAppointmentProjectParams().get(0).getProjectId() == null) {
            return JsonResults.fail(1000, "未添加项目");
        }
        boolean isAllFree = appointmentService.isAllFree(params.getAppointmentProjectParams());
        if (isAllFree) {
            AppointmentPo appointmentPo = CommonConverter.map(params, AppointmentPo.class);
            appointmentService.createAppointment(appointmentPo, params.getAppointmentProjectParams());
            return JsonResults.success();
        } else
            return JsonResults.fail(1000, "预约冲突");
    }

    @RequestMapping(path = "/modify", method = { RequestMethod.PUT })
    public JsonResult<Void> doModifyAppointment(@RequestBody AppointmentCreateOrModifyParams params) {
        List<AppointmentProjectPo> apList = appointmentProjectMapper.getListByAppointmentId(params.getId());
        appointmentProjectMapper.delByAppointmentId(params.getId());
        boolean isAllFree = appointmentService.isAllFree(params.getAppointmentProjectParams());
        if (isAllFree) {
            AppointmentPo appointmentPo = CommonConverter.map(params, AppointmentPo.class);
            appointmentService.modifyAppointment(appointmentPo, params.getAppointmentProjectParams());
            return JsonResults.success();
        } else
            for (AppointmentProjectPo po : apList) {
                appointmentProjectMapper.insert(po);
            }
        return JsonResults.fail(1000, "预约冲突");
    }

    @RequestMapping(path = "/remind", method = { RequestMethod.PUT })
    public JsonResult<List<AppointmentUnion>> doRemindAppointment() {
        List<AppointmentUnion> unionList = appointmentService.getRemindAppointment();
        return JsonResults.success(unionList);
    }

    @RequestMapping(path = "/cancel/{appointmentId}", method = { RequestMethod.PUT })
    public JsonResult<Void> doCancleAppointment(@PathVariable("appointmentId") Long appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        return JsonResults.success();
    }

    @RequestMapping(path = "/finish/{appointmentId}", method = { RequestMethod.PUT })
    public JsonResult<Void> doFinishAppointment(@PathVariable("appointmentId") Long appointmentId) {
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
        List<EmployeeTimeTable> timeTableList = appointmentService.queryEmployeeTimeTable(params.getEmployeeId(),
                params.getDate());
        return JsonResults.success(timeTableList);
    }

    @RequestMapping(path = "/operatorTimeTableList", method = { RequestMethod.PUT })
    public JsonResult<List<OperatorTimeTableUnion>> queryOperatorList(@RequestBody QueryOperatorTimeTableParams params) {
        List<OperatorTimeTableUnion> operatorTimeTableList = appointmentService
                .queryOperatorTimeTable(params.getOperatorId(), params.getBeginTime(), params.getEndTime());
        return JsonResults.success(operatorTimeTableList);
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
