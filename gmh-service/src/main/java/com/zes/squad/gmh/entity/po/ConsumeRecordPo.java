package com.zes.squad.gmh.entity.po;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConsumeRecordPo extends Po {

	private static final long serialVersionUID = 1L;

	private String tradeSerialNumber;
	private Long customerId;
	private String customerMobile;
	private Integer consumeType;
	private BigDecimal consumeMoney;
	private Integer paymentWay;
	private Integer payWayId;
	private Long activityId;
	private Integer isModified;
	private String remark;
	private Date consumeTime;
	private Long storeId;
}
