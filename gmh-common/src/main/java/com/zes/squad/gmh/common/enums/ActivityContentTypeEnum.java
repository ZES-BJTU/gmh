package com.zes.squad.gmh.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ActivityContentTypeEnum {

    PROJECT(1, "项目"),
    MEMBER_CARD(2, "会员卡"),
    PRODUCT(3, "产品"),
    CASH_COUPON(4, "代金券");

    private int    key;
    private String desc;

}
