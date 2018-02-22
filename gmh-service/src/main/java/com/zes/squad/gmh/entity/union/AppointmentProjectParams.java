package com.zes.squad.gmh.entity.union;

import java.util.Date;

import lombok.Data;
@Data
public class AppointmentProjectParams {

	private Long projectId;
	private String projectCode;
	private Long employeeId;
	private Date beginTime;
	private Date endTime;
}
