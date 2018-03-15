package com.zes.squad.gmh.service;

import java.util.Date;
import java.util.List;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.AppointmentQueryCondition;
import com.zes.squad.gmh.entity.po.AppointmentPo;
import com.zes.squad.gmh.entity.union.AppointmentProjectParams;
import com.zes.squad.gmh.entity.union.AppointmentUnion;
import com.zes.squad.gmh.entity.union.EmployeeTimeTable;

public interface AppointmentService {

    void createAppointment(AppointmentPo appointmentPo, List<AppointmentProjectParams> appointmentPorjectParams);

    void cancelAppointment(Long appointmentId);

    void finishAppointment(Long appointmentId);

    PagedList<AppointmentUnion> listPagedAppointments(AppointmentQueryCondition condition);

    List<EmployeeTimeTable> QueryEmployeeTimeTable(Long employeeId,Date date);
}
