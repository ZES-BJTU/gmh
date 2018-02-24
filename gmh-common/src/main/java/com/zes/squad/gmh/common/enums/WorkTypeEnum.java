package com.zes.squad.gmh.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum WorkTypeEnum {

    BEAUTICIAN(1, "美容师"),
    NAIL_ARTIST(2, "美甲师"),
    BEAUTY_TEACHER(3, "美睫师"),
    BEAUTY_CONSULTANT(4, "美容咨询师"),
    DIRECTOR(5, "总监"),
    TRAINEE_BEAUTICIAN(6, "实习美容师"),
    TRAINEE_CONSULTANT(7, "实习咨询师");

    private int    key;
    private String desc;

}
