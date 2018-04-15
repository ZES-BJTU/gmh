package com.zes.squad.gmh.web.entity.param;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QueryOperatorTimeTableParams extends QueryParams{

	private Long operatorId;
	private Date beginTime;
	private Date endTime;
}
