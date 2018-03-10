package com.zes.squad.gmh.entity.condition;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression.DateTime;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConsumeRecordQueryCondition extends QueryCondition{
	private Long storeId;
	private Integer consumeType;
	private DateTime begainTime;
	private DateTime endTime;
	private String keyWords;
}
