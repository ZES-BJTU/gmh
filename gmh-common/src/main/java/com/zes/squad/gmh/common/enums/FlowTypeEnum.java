package com.zes.squad.gmh.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum FlowTypeEnum {

    FIRST_BUYING_IN(1, "首次进货"),
    BUYING_IN(2, "补货"),
    SELLING_OUT(3, "卖出"),
    ADJUSTMENT(4, "人工调整"),
    ADJUSTMENT_OUT(5, "跨店调货转出"),
    ADJUSTMENT_IN(6, "跨店调货转入"),;

    private int    key;
    private String desc;

}
