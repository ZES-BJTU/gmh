package com.zes.squad.gmh.entity.union;

import lombok.Data;

@Data
public class ConsumeRecordDetailUnion {

	private String productName;
	private String projectName;
	private String cardName;
	private Integer amount;
	private String operatorName;
	private String consultantName;
	private String salesManName;
}
