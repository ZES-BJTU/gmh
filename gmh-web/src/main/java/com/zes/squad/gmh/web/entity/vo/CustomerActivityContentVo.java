package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CustomerActivityContentVo {
	
	private Long id;
	private String type;
	private String relatedName;
	private BigDecimal content;
	private BigDecimal number;
}
