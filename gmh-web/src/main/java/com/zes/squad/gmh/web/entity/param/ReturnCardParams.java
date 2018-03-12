package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ReturnCardParams {
	private Long id;
	private String returnReason;
	private BigDecimal returnMoney;
}
