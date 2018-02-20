package com.zes.squad.gmh.web.entity.param;

import lombok.Data;

@Data
public class AppointmentQueryParams {

	private Integer customerGender;
	private Integer isVip;
	private Integer isLine;
	private Integer status;
	private String  appointmentKeyWord;
}
