package com.zes.squad.gmh.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum WorkingEnum {

    OFF(0, "离职"),
    ON(1, "在职");

    private int    key;
    private String desc;

}
