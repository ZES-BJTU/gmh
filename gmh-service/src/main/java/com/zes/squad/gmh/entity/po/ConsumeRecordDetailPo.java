package com.zes.squad.gmh.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConsumeRecordDetailPo extends Po{
	
	private static final long serialVersionUID = 1L;
	
	private String tradeSerialNumber;
	private Long productId;
	private String productCode;
	private Long projectId;
	private String projectCode;
	private Long cardId;
	private String cardCode;
	private Integer amount;
	private Long operatorId;
	private Long consultantId;
	private Long salesManId;
}
