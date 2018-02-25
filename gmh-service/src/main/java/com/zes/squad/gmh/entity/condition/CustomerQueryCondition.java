package com.zes.squad.gmh.entity.condition;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomerQueryCondition extends QueryCondition{

	private Long storeId;
	private Integer gender;
	private Integer source;
	private String keyWords;
}
