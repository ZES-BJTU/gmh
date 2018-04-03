package com.zes.squad.gmh.entity.union;

import lombok.Data;

@Data
public class ConsumeRecordDetailUnion {

	private Long productId;
	private String productName;
	private Long projectId;
	private String projectName;
	private Long cardId;
	private String cardName;
	private Integer amount;
	private String operatorName;
	private String consultantName;
	private String salesManName;
}
