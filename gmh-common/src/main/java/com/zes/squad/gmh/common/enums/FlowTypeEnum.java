package com.zes.squad.gmh.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum FlowTypeEnum {
    
    BUYING_IN(1,"买入"),
    SELLING_OUT(2,"卖出");

    private int key;
    private String desc;

}
