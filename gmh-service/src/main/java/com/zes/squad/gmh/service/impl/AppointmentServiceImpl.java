package com.zes.squad.gmh.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.AppointmentQueryCondition;
import com.zes.squad.gmh.entity.po.AppointmentPo;
import com.zes.squad.gmh.entity.po.AppointmentProjectPo;
import com.zes.squad.gmh.entity.union.AppointmentProjectParams;
import com.zes.squad.gmh.entity.union.AppointmentProjectUnion;
import com.zes.squad.gmh.entity.union.AppointmentUnion;
import com.zes.squad.gmh.entity.union.EmployeeUnion;
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
		appointmentMapper.cancleAppointment(appointmentId);
	}
	@Override
	public void finishAppointment(Long appointmentId) {
		appointmentMapper.finishAppointment(appointmentId);
		
	}
	 public PagedList<AppointmentUnion> listPagedAppointments(AppointmentQueryCondition condition) {
	        int pageNum = condition.getPageNum();
	        int pageSize = condition.getPageSize();
	        PageHelper.startPage(pageNum, pageSize);
	        AppointmentUnion appointmentUnion = new AppointmentUnion();
	        List<AppointmentUnion> appointmentUnions = new ArrayList<AppointmentUnion>();
	        List<AppointmentPo> appointmentPos = appointmentMapper.listAppointmentByCondition(condition);
	        if (CollectionUtils.isEmpty(appointmentPos)) {
	            return PagedLists.newPagedList(pageNum, pageSize);
	        }
	        for(AppointmentPo appointmentPo: appointmentPos){
	        	List<AppointmentProjectUnion> appointmentProjectUnions = new ArrayList<AppointmentProjectUnion>();
	        	appointmentProjectUnions = appointmentProjectMapper.getAppointmentProjectUnionByAppId(appointmentPo.getId());
	        	appointmentUnion.setAppointmentPo(appointmentPo);
	        	appointmentUnion.setAppointmentProjects(appointmentProjectUnions);
	        	appointmentUnions.add(CommonConverter.map(appointmentUnion, AppointmentUnion.class));
	        }
	        PageInfo<AppointmentUnion> info = new PageInfo<>(appointmentUnions);
	        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), appointmentUnions);
	    }

}
