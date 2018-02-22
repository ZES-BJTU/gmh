package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.condition.AppointmentQueryCondition;
import com.zes.squad.gmh.entity.po.AppointmentPo;

public interface AppointmentMapper {

	int insert(AppointmentPo appointmentPo);
	
	int cancleAppointment(Long id);

	int finishAppointment(Long id);
	
	List<AppointmentPo> listAppointmentByCondition(AppointmentQueryCondition condition);
}
