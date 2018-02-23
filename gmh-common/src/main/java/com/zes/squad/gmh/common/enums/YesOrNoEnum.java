package com.zes.squad.gmh.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum YesOrNoEnum {

    YES(1, "是"),
    NO(0, "否");

    private int    key;
    private String desc;
}
