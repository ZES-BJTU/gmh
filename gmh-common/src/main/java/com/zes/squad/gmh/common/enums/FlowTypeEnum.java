package com.zes.squad.gmh.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum FlowTypeEnum {

    FIRST_BUYING_IN(1, "首次买入"),
    BUYING_IN(2, "买入"),
    SELLING_OUT(3, "卖出"),
    ADJUSTMENT(4, "调整");

    private int    key;
    private String desc;

}
