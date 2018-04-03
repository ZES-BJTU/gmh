package com.zes.squad.gmh.entity.union;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class ConsumeRecordGiftUnion {
	private Integer type;
	private Long projectId;
	private String projectName;
	private Integer projectAmount;
	private Long productId;
	private String productName;
	private Integer productAmount;
	private BigDecimal couponMoney;
	private Integer couponAmount;
}
