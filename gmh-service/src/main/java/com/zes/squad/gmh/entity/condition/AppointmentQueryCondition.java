package com.zes.squad.gmh.entity.condition;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppointmentQueryCondition extends QueryCondition{

	private Long storeId;
	private Integer customerGender;
	private Integer isVip;
	private Integer isLine;
	private Integer status;
	private String appointmentKeyWords;
}
