package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class TurnCardParams {
	private Long id;
	private Long newCardId;
	private String turnedReason;
	private BigDecimal turnedMoney;
}
