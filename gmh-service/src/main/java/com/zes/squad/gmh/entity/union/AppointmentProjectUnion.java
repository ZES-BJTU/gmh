package com.zes.squad.gmh.entity.union;

import java.util.Date;

import lombok.Data;

@Data
public class AppointmentProjectUnion {

	private Long projectId;
	private String projectCode;
    private String projectName;
    private Long employeeId;
    private String employeeName;
    private Date   beginTime;
    private Date   endTime;

}
