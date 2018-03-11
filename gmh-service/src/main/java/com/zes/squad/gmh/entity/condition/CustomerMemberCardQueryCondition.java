package com.zes.squad.gmh.entity.condition;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomerMemberCardQueryCondition extends QueryCondition{
	private Long storeId;
	private String keyWords;
}
