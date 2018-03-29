package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ChangeCardStoreParams {
	private Long id;
	private String turnedReason;
	private BigDecimal turnedMoney;
	private Long newStoreId;
}
