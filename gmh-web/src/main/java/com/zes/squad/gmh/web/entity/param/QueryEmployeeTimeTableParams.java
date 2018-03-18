package com.zes.squad.gmh.web.entity.param;

import java.util.Date;

import lombok.Data;

@Data
public class QueryEmployeeTimeTableParams {
	
	private Long employeeId;
	private Date date;
}
