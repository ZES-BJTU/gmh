package com.zes.squad.gmh.entity.union;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
@Data
public class CustomerMemberCardUnion {
	
	private Long id;
	private String customerName;
	private String memberCardName;
	private BigDecimal remainingMoney;
	private Integer remainingTimes;
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
}
