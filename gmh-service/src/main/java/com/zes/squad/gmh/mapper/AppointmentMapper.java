package com.zes.squad.gmh.mapper;

import java.util.List;
import java.util.Map;

import com.zes.squad.gmh.entity.condition.AppointmentQueryCondition;
import com.zes.squad.gmh.entity.po.AppointmentPo;
import com.zes.squad.gmh.entity.union.EmployeeTimeTable;

public interface AppointmentMapper {

    int insert(AppointmentPo appointmentPo);

    int cancelAppointment(Long id);

    int finishAppointment(Long id);

    List<AppointmentPo> listAppointmentByCondition(AppointmentQueryCondition condition);
    
    List<EmployeeTimeTable> queryEmployeeTimeTable(Map<String,Object> map);
}
