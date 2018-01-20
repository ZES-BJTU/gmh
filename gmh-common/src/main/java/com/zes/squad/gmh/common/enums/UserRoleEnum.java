package com.zes.squad.gmh.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserRoleEnum {

    ADMINISTRATOR(1, "管理员"),
    MANAGER(2, "店长"),
    RECEPTION(3, "前台"),
    OPERATOR(4, "操作员");

    private int    key;
    private String desc;

}
