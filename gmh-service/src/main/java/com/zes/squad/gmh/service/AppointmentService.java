package com.zes.squad.gmh.service;

import java.util.Date;
import java.util.List;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.AppointmentQueryCondition;
import com.zes.squad.gmh.entity.po.AppointmentPo;
import com.zes.squad.gmh.entity.union.AppointmentProjectParams;
import com.zes.squad.gmh.entity.union.AppointmentUnion;
import com.zes.squad.gmh.entity.union.EmployeeTimeTable;
import com.zes.squad.gmh.entity.union.OperatorTimeTableUnion;

public interface AppointmentService {

    void createAppointment(AppointmentPo appointmentPo, List<AppointmentProjectParams> appointmentPorjectParams);

    void modifyAppointment(AppointmentPo appointmentPo, List<AppointmentProjectParams> appointmentPorjectParams);

    void cancelAppointment(Long appointmentId);

    void finishAppointment(Long appointmentId);

    PagedList<AppointmentUnion> listPagedAppointments(AppointmentQueryCondition condition);

    List<EmployeeTimeTable> queryEmployeeTimeTable(Long employeeId, Date date);

    boolean isFree(Long employeeId, Date beginTime, Date endTime);

    boolean isAllFree(List<AppointmentProjectParams> apList);

    /**
     * 查询需要短信提醒的预约
     * 
     * @param
     * @return
     */
    List<AppointmentUnion> getRemindAppointment();

    List<OperatorTimeTableUnion> queryOperatorTimeTable(Long operatorId, Date beginTime, Date endTime);
}
