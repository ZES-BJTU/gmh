package com.zes.squad.gmh.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
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
import com.zes.squad.gmh.entity.union.EmployeeTimeTable;
import com.zes.squad.gmh.mapper.AppointmentMapper;
import com.zes.squad.gmh.mapper.AppointmentProjectMapper;
import com.zes.squad.gmh.service.AppointmentService;

@Service("appointmentService")
public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
	private AppointmentMapper appointmentMapper;
	@Autowired
	private AppointmentProjectMapper appointmentProjectMapper;

	@Override
	public void createAppointment(AppointmentPo appointmentPo,
			List<AppointmentProjectParams> appointmentPorjectParams) {
		appointmentPo.setStoreId(ThreadContext.getUserStoreId());
		appointmentPo.setStatus(1);
		appointmentMapper.insert(appointmentPo);
		for (AppointmentProjectParams ap : appointmentPorjectParams) {
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
		for (AppointmentPo appointmentPo : appointmentPos) {
			List<AppointmentProjectUnion> appointmentProjectUnions = new ArrayList<AppointmentProjectUnion>();
			appointmentProjectUnions = appointmentProjectMapper
					.getAppointmentProjectUnionByAppId(appointmentPo.getId());
			appointmentUnion.setAppointmentPo(appointmentPo);
			appointmentUnion.setAppointmentProjects(appointmentProjectUnions);
			appointmentUnions.add(CommonConverter.map(appointmentUnion, AppointmentUnion.class));
		}
		PageInfo<AppointmentUnion> info = new PageInfo<>(appointmentUnions);
		return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), appointmentUnions);
	}

	@Override
	public List<EmployeeTimeTable> queryEmployeeTimeTable(Long employeeId, Date date) {

		DateTime startTmp = new DateTime(date.getTime());
		startTmp = startTmp.withHourOfDay(6);
		startTmp = startTmp.withMinuteOfHour(0);
		startTmp = startTmp.withSecondOfMinute(0);
		startTmp = startTmp.withMillisOfSecond(0);
		Date startTime = startTmp.toDate();
		DateTime endTmp = new DateTime(date.getTime());
		endTmp = endTmp.withHourOfDay(22);
		endTmp = endTmp.withMinuteOfHour(0);
		endTmp = endTmp.withSecondOfMinute(0);
		endTmp = endTmp.withMillisOfSecond(0);
		Date endTime = endTmp.toDate();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("employeeId", employeeId);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		List<EmployeeTimeTable> busyTableList = appointmentMapper.queryEmployeeTimeTable(map);
		List<EmployeeTimeTable> freeTableList = new ArrayList<EmployeeTimeTable>();
		EmployeeTimeTable timeTable = new EmployeeTimeTable();
		for (int i = 0; i <= busyTableList.size(); i++) {
			if (busyTableList.isEmpty()) {
				timeTable.setBeginTime(startTime);
				timeTable.setEndTime(endTime);
				timeTable.setIsFree(1);
				timeTable.setPercent((double) 100);
				freeTableList.add(CommonConverter.map(timeTable, EmployeeTimeTable.class));
				break;
			}
			if (i == 0) {
				timeTable.setBeginTime(startTime);
				timeTable.setEndTime(busyTableList.get(i).getBeginTime());
				timeTable.setIsFree(1);
				timeTable.setPercent(calPercentage(busyTableList.get(i).getBeginTime(), startTime));

				busyTableList.get(i).setIsFree(0);
				busyTableList.get(i).setPercent(
						calPercentage(busyTableList.get(i).getEndTime(), busyTableList.get(i).getBeginTime()));

			} else if (i == busyTableList.size()) {
				timeTable.setBeginTime(busyTableList.get(i - 1).getEndTime());
				timeTable.setEndTime(endTime);
				timeTable.setIsFree(1);
				timeTable.setPercent(calPercentage(endTime, busyTableList.get(i - 1).getEndTime()));
			} else {
				timeTable.setBeginTime(busyTableList.get(i - 1).getEndTime());
				timeTable.setEndTime(busyTableList.get(i).getBeginTime());
				timeTable.setIsFree(1);
				timeTable.setPercent(
						calPercentage(busyTableList.get(i).getBeginTime(), busyTableList.get(i - 1).getEndTime()));

				busyTableList.get(i).setIsFree(0);
				busyTableList.get(i).setPercent(
						calPercentage(busyTableList.get(i).getEndTime(), busyTableList.get(i).getBeginTime()));
			}

			freeTableList.add(CommonConverter.map(timeTable, EmployeeTimeTable.class));
		}
		List<EmployeeTimeTable> tableList = new ArrayList<EmployeeTimeTable>();
		for (int i = 0; i < busyTableList.size(); i++) {
			if (busyTableList.isEmpty()) {
				tableList.add(freeTableList.get(i));
				break;
			}
			tableList.add(freeTableList.get(i));
			tableList.add(busyTableList.get(i));
		}
		tableList.add(freeTableList.get(freeTableList.size() - 1));
		Double test = 0.0;
		for (EmployeeTimeTable ett : tableList) {
			test = test + ett.getPercent();
		}
		if (!test.equals(100.00)) {
			DecimalFormat df = new DecimalFormat("######0.00");
			Double fin = tableList.get(tableList.size() - 1).getPercent();
			Double difference = 100.00 - test;
			String tmp = "0.0";
			if (100.00 > test) {
				tmp = df.format(fin + difference);
			} else {
				tmp = df.format(fin - difference);
			}

			tableList.get(tableList.size() - 1).setPercent(Double.parseDouble(tmp));
		}
		return tableList;
	}

	Double calPercentage(Date end, Date begin) {
		Double total = (double) (16 * 60 * 60 * 1000);
		Double tmpResult = (end.getTime() - begin.getTime()) * 100 / total;
		DecimalFormat df = new DecimalFormat("######0.00");
		String tmp = df.format(tmpResult);
		Double result = Double.parseDouble(tmp);
		return result;
	}

}
