package com.zes.squad.gmh.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别枚举
 * 
 * @author yuhuan.zhou 2018年1月14日 下午8:25:28
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserGenderEnum {

    FEMALE(0, "女"),
    MALE(1, "男");

    private int    key;
    private String desc;

}
