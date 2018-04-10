package com.zes.squad.gmh.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ConsumeTypeEnum {

    CARD(1, "办卡"),
    PRODUCT(2, "买产品"),
    PROJECT(3, "做项目"),
	ACTIVITY(4,"参加活动"),
	RECHARGE(5,"充值");
    private int    key;
    private String desc;

}
