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
import com.zes.squad.gmh.entity.po.CustomerPo;
import com.zes.squad.gmh.entity.union.AppointmentProjectParams;
import com.zes.squad.gmh.entity.union.AppointmentProjectUnion;
import com.zes.squad.gmh.entity.union.AppointmentUnion;
import com.zes.squad.gmh.entity.union.EmployeeTimeTable;
import com.zes.squad.gmh.mapper.AppointmentMapper;
import com.zes.squad.gmh.mapper.AppointmentProjectMapper;
import com.zes.squad.gmh.mapper.CustomerMapper;
import com.zes.squad.gmh.service.AppointmentService;
import com.zes.squad.gmh.service.ProjectService;
import com.zes.squad.gmh.service.StoreService;

@Service("appointmentService")
public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
	private AppointmentMapper appointmentMapper;
	@Autowired
	private AppointmentProjectMapper appointmentProjectMapper;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private CustomerMapper customerMapper;
	@Override
	public void createAppointment(AppointmentPo appointmentPo,
			List<AppointmentProjectParams> appointmentPorjectParams) {
		
		CustomerPo customer = customerMapper.getByMobile(appointmentPo.getCustomerMobile());
		if(customer!=null){
			appointmentPo.setCustomerId(customer.getId());
		}
		appointmentPo.setStoreId(ThreadContext.getUserStoreId());
		appointmentPo.setStatus(1);
		appointmentMapper.insert(appointmentPo);
		for (AppointmentProjectParams ap : appointmentPorjectParams) {
			AppointmentProjectPo po = new AppointmentProjectPo();
			po = CommonConverter.map(ap, AppointmentProjectPo.class);
			Long projectId = projectService.queryProjectByCode(ap.getProjectCode());
			if (projectId != null) {
				po.setProjectId(projectId);
			}
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
		condition.setStoreId(ThreadContext.getUserStoreId());
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

	public Double calPercentage(Date end, Date begin) {
		Double total = (double) (16 * 60 * 60 * 1000);
		Double tmpResult = (end.getTime() - begin.getTime()) * 100 / total;
		DecimalFormat df = new DecimalFormat("######0.00");
		String tmp = df.format(tmpResult);
		Double result = Double.parseDouble(tmp);
		return result;
	}

	// 判断单个时间段是否为空闲
	public boolean isFree(Long employeeId, Date beginTime, Date endTime) {
		List<EmployeeTimeTable> timeList = queryEmployeeTimeTable(employeeId, beginTime);
		boolean isFree = false;
		if (timeList.size() == 1)
			isFree = true;
		else {
			for (int i = 0; i < timeList.size();) {
				if (beginTime.getTime() >= timeList.get(i).getBeginTime().getTime()
						&& endTime.getTime() <= timeList.get(i).getEndTime().getTime()) {
					isFree = true;
				}
				i = i + 2;
			}
		}
		return isFree;
	}

	// 判断要插入的list是否均为空闲
	public boolean isAllFree(List<AppointmentProjectParams> apList) {
		List<AppointmentProjectPo> tmpApList = new ArrayList<AppointmentProjectPo>();
		boolean isFree = true;
		for (AppointmentProjectParams ap : apList) {
			isFree = isFree(ap.getEmployeeId(), ap.getBeginTime(), ap.getEndTime());
			if (isFree) {
				AppointmentProjectPo po = CommonConverter.map(ap, AppointmentProjectPo.class);
				appointmentProjectMapper.insert(po);
				tmpApList.add(CommonConverter.map(po, AppointmentProjectPo.class));
			} else
				isFree = false;
			break;
		}
		for (AppointmentProjectPo apPo : tmpApList) {
			appointmentProjectMapper.delById(apPo.getId());
		}
		return isFree;
	}

	@Override
	public void modifyAppointment(AppointmentPo appointmentPo,
			List<AppointmentProjectParams> appointmentPorjectParams) {

		appointmentMapper.modify(appointmentPo);
		for (AppointmentProjectParams ap : appointmentPorjectParams) {
			AppointmentProjectPo po = new AppointmentProjectPo();
			po = CommonConverter.map(ap, AppointmentProjectPo.class);
//			Long projectId = projectService.queryProjectByCode(ap.getProjectCode());
//			if (projectId != null) {
//				po.setProjectId(projectId);
//			}
			po.setAppointmentId(appointmentPo.getId());
			appointmentProjectMapper.insert(po);
		}

	}

	@Override
	public List<AppointmentUnion> getRemindAppointment() {
		List<AppointmentUnion> appointmentUnions = new ArrayList<AppointmentUnion>();
		List<AppointmentPo> appointmentPos = appointmentMapper.listRemindAppointment();
		AppointmentUnion appointmentUnion = new AppointmentUnion();

		for (AppointmentPo appointmentPo : appointmentPos) {
			List<AppointmentProjectUnion> appointmentProjectUnions = new ArrayList<AppointmentProjectUnion>();
			appointmentProjectUnions = appointmentProjectMapper
					.getAppointmentProjectUnionByAppId(appointmentPo.getId());
			appointmentUnion.setAppointmentPo(appointmentPo);
			appointmentUnion.setAppointmentProjects(appointmentProjectUnions);
			appointmentUnion.setStoreUnion(storeService.queryStoreDetail(appointmentPo.getStoreId()));
			appointmentUnions.add(CommonConverter.map(appointmentUnion, AppointmentUnion.class));
		}
		return appointmentUnions;
	}

}
