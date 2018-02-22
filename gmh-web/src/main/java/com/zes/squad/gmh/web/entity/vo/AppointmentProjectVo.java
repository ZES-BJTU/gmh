package com.zes.squad.gmh.web.entity.vo;

import java.util.Date;

import lombok.Data;

@Data
public class AppointmentProjectVo {
	private String projectName;
	private String employeeName;
	private Date beginTime;
	private Date endTime;
}
