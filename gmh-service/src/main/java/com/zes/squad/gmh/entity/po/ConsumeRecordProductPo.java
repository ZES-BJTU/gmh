package com.zes.squad.gmh.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConsumeRecordProductPo extends Po{
	
	private static final long serialVersionUID = 1L;
	
	private String tradeSerialNumber;
	private Long productId;
	private Integer productAmount;
	private Long operatorId;
	private Long consultantId;
	private Long salesManId;
}
