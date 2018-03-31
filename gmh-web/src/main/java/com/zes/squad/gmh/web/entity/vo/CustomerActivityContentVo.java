package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CustomerActivityContentVo {
	
	private String type;
	private String relatedName;
	private String content;
	private BigDecimal number;
}
