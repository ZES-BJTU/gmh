package com.zes.squad.gmh.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.po.AppointmentPo;
import com.zes.squad.gmh.entity.po.AppointmentProjectPo;
import com.zes.squad.gmh.entity.union.AppointmentProjectParams;
import com.zes.squad.gmh.mapper.AppointmentMapper;
import com.zes.squad.gmh.mapper.AppointmentProjectMapper;
import com.zes.squad.gmh.service.AppointmentService;

@Service("appointmentService")
public class AppointmentServiceImpl implements AppointmentService{
	@Autowired
	private AppointmentMapper appointmentMapper;
	@Autowired
	private AppointmentProjectMapper appointmentProjectMapper;
	@Override
	public void createAppointment(AppointmentPo appointmentPo,List<AppointmentProjectParams> appointmentPorjectParams) {
		appointmentPo.setStoreId(ThreadContext.getUserStoreId());
		appointmentPo.setStatus(1);
		appointmentMapper.insert(appointmentPo);
		for(AppointmentProjectParams ap:appointmentPorjectParams){
			AppointmentProjectPo po = CommonConverter.map(ap, AppointmentProjectPo.class);
			po.setAppointmentId(appointmentPo.getId());
			appointmentProjectMapper.insert(po);
		}
	}
	@Override
	public void cancelAppointment(Long appointmentId) {
		appointmentMapper.cancelAppointment(appointmentId);
	}
	@Override
	public void finishAppointment(Long appointmentId) {
		appointmentMapper.finishAppointment(appointmentId);
		
	}
	

}
