package com.zes.squad.gmh.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TopTypeEnum {

    BEAUTY(1, "美容"),
    NAIL(2, "美甲"),
    LIDS(3, "美睫"),
    CONSULTANT(4, "顾问");

    private int    key;
    private String desc;

}
