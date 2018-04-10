package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RechargeOrBuyProjectParams {
	
	private Long cardId;
	private Long projectId;
	private Integer projectTimes;
	private BigDecimal useRemainMoney;
	private BigDecimal rechargeMoney;
	private Long consultantId;
	private Long salesManId;
	
}
