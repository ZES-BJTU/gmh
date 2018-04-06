package com.zes.squad.gmh.entity.union;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class CustomerActivityContentUnion {
	private Integer type;
	private Long relatedId;
	private String relatedName;
	private BigDecimal content;
	private BigDecimal number;
}
