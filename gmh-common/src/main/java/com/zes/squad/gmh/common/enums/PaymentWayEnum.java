package com.zes.squad.gmh.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PaymentWayEnum {

	 	MEMBER_CARD(1, "会员卡"),
	    ACTIVITY(2, "活动"),
		CASH(3,"现金"),
		CASHCARDCOUPON(31,"现金+会员卡代金券"),
		CASHACTIVITYCOUPON(32,"现金+活动代金券"),
		FREE(4,"赠送");
	
	    private int    key;
	    private String desc;
}
