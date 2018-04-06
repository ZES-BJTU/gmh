package com.zes.squad.gmh.entity.po;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomerMemberCardContentPo extends Po{

	private static final long serialVersionUID = 1L;
	
	private Long customerMemberCardId;
	private Long relatedId;
	private BigDecimal content;
	private Integer amount;
	
}
