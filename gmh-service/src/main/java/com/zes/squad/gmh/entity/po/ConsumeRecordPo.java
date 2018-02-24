package com.zes.squad.gmh.entity.po;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression.DateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConsumeRecordPo extends Po{
	
	private static final long serialVersionUID = 1L;
	
	private String tradeSerialNumber;
	private Integer customerId;
	private Integer consumeType;
	private BigDecimal consumeMoney;
	private Integer paymentWay;
	private String activityName;
	private Integer isModified;
	private String remark;
	private DateTime consumeTime;
	private Integer storeId;
}
