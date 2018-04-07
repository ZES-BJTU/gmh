package com.zes.squad.gmh.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ConsumeGiftEnum {
	 	
		PROJECT(1, "项目"),
	    PRODUCT(2, "产品"),
		COUPON(3,"代金券");
		
	    private int    key;
	    private String desc;
}
