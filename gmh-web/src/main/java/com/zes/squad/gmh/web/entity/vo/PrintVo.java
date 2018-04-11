package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class PrintVo {
	
	private String name;
	private Integer number;
	private String paymentWay;
	private BigDecimal orgPrice;		//原价
	private BigDecimal actPrice;		//实付金额
	
	
}
