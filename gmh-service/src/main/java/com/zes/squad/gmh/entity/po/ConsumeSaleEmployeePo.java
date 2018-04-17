package com.zes.squad.gmh.entity.po;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConsumeSaleEmployeePo extends Po{
	private static final long serialVersionUID = 1L;
	
	private Long consumeRecordId;
	private Long employeeId;
	private BigDecimal percent;

}
