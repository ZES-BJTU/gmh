package com.zes.squad.gmh.entity.po;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomerActivityContentPo extends Po{

	private static final long serialVersionUID = 1L;
	
	private Long customerActivityId;
	private Integer type;
	private Long relatedId;
	private String content;
	private BigDecimal number;
	private String remark;
}
