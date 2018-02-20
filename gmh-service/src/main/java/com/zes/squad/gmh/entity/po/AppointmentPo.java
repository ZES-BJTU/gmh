package com.zes.squad.gmh.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppointmentPo extends Po{
	
	private static final long serialVersionUID = 1L;
	
	private String customerName;
	private String customerMobile;
	private Integer customerGender;
	private Integer isVip;
	private Long customerId;
	private Integer isLine;
	private Integer status;
	private String remarks;
	private Long storeId;
}
