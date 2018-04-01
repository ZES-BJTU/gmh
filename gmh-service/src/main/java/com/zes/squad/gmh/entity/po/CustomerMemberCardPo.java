package com.zes.squad.gmh.entity.po;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomerMemberCardPo extends Po{

	private static final long serialVersionUID = 1L;
	
	private Long customerId;
	private Long memberCardId;
	private BigDecimal remainingMoney;
	private Integer remainingTimes;
	private BigDecimal projectDiscount;
	private BigDecimal productDiscount;
	private Integer isValid;
	private Integer isReturned;
	private String returnedReason;
	private Date returnedTime;
	private BigDecimal returnedMoney;
	private Integer isTurned;
	private Date turnedTime;
	private BigDecimal turnedMoney;
	private String turnedReason;
	private String uniqueIdentifier;
	private Long storeId;
}
