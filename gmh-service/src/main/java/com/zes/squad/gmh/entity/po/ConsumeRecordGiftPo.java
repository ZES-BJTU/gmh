package com.zes.squad.gmh.entity.po;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConsumeRecordGiftPo extends Po{
	
	private static final long serialVersionUID = 1L;
	
	private String tradeSerialNumber;;
	private Integer type;
	private Integer projectId;
	private Integer projectAmount;
	private Integer productId;
	private Integer productAmount;
	private BigDecimal couponMoney;
	private Integer couponAmount;
	
}