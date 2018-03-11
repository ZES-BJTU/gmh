package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;

import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression.DateTime;
@Data
public class CustomerMemberCardVo {
	
	private String customerName;
	private String memberCardName;
	private BigDecimal remainingMoney;
	private Integer remainingTimes;
	private String isValid;
	private String isReturned;
	private DateTime returnedTime;
	private String returnedReason;
	private BigDecimal returnedMoney;
	private String isTurned;
	private String turnedReason;
	private DateTime turnedTime;
	private BigDecimal turnedMoney;
	private String uniqueIdentifier;
	
}
