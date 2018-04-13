package com.zes.squad.gmh.web.entity.param;

import java.util.Date;

import lombok.Data;

@Data
public class QueryOperatorTimeTableParams {

	private Long operatorId;
	private Date beginTime;
	private Date endTime;
}
