package com.zes.squad.gmh.web.entity.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression.DateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConsumeRecordQueryParams extends QueryParams{
	private Integer consumeType;
	private Long projectId;
	private DateTime beginTime;
	private DateTime endTime;
	private String keyWords;
}
