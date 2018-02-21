package com.zes.squad.gmh.mapper;

import com.zes.squad.gmh.entity.po.AppointmentPo;

public interface AppointmentMapper {

	int insert(AppointmentPo appointmentPo);
	
	int cancelAppointment(Long id);

	int finishAppointment(Long id);
	
}
