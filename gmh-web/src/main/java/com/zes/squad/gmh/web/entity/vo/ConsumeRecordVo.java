package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;
import java.util.List;

import com.zes.squad.gmh.entity.union.ConsumeRecordDetailUnion;
import com.zes.squad.gmh.entity.union.ConsumeRecordGiftUnion;

import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression.DateTime;

@Data
public class ConsumeRecordVo {

	private Long id;
	private String tradeSerialNumber;
	private String customerName;
	private String customerMobile;
	private String consumeType;
	private BigDecimal consumeMoney;
	private Integer paymentWay;
	private String activityName;
	private Integer isModified;
	private String remark;
	private DateTime consumeTime;
	private List<ConsumeRecordDetailUnion> consumeRecordDetailUnions;
	private List<ConsumeRecordGiftUnion> consumeRecordGiftUnions;
}
