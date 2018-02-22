package com.zes.squad.gmh.entity.union;

import java.util.Date;

import lombok.Data;
@Data
public class AppointmentProjectUnion {
	
	private String projectName;
	private String employeeName;
	private Date beginTime;
	private Date endTime;
	
}
