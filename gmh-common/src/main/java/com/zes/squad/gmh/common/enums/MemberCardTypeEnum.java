package com.zes.squad.gmh.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MemberCardTypeEnum {

    TIMES_CARD(1, "次卡"),
    PREPAID_CARD(2, "储值卡"),
    MIXED_CARD(3, "混合卡");

    private int    key;
    private String desc;

}
