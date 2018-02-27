package com.zes.squad.gmh.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ConsumeTypeEnum {

    BUYPRODUCT(0, "买产品"),
    DOPROJECT(1, "做美容"),
    BUYCARD(2, "办卡");

    private int    key;
    private String desc;

}
