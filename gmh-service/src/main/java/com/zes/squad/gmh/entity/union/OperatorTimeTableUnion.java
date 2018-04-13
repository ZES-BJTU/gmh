package com.zes.squad.gmh.entity.union;

import java.util.Date;

import lombok.Data;
@Data
public class OperatorTimeTableUnion {
	private Long operatorId;
	private String operatorName;
	private Long projectId;
	private String projectName;
	private Date beginTime;
	private Date endTime;
}
