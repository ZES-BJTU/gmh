package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression.DateTime;
@Data
public class CustomerMemberCardVo {
	
	private Long id;
	private String customerName;
	private String memberCardName;
	private BigDecimal remainingMoney;
//	private Integer remainingTimes;
	private List<CustomerMemberCardContentVo> customerMemberCardContent;
	private String isValid;
	private String isReturned;
	private DateTime returnedTime;
	private String returnedReason;
	private BigDecimal returnedMoney;
	private BigDecimal projectDiscount;
	private BigDecimal productDiscount;
	private String isTurned;
	private String turnedReason;
	private DateTime turnedTime;
	private BigDecimal turnedMoney;
	private String uniqueIdentifier;
	
}
