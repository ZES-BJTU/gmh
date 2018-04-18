package com.zes.squad.gmh.entity.condition;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConsumeRecordQueryCondition extends QueryCondition{
	private Long storeId;
	private Integer consumeType;
	private Long projectId;
	private Date beginTime;
	private Date endTime;
	private String keyWords;
}
