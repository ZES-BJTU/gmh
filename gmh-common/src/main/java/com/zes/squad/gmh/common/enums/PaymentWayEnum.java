package com.zes.squad.gmh.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PaymentWayEnum {

	 	MEMBER_CARD(1, "女"),
	    ACTIVITY(2, "男"),
		CASH(3,"现金"),
		FREE(4,"赠送");
	
	    private int    key;
	    private String desc;
}
