package com.zes.squad.gmh.web.param.user;

import lombok.Data;

@Data
public class UserCreateOrModifyParams {

    private Long    id;
    private Integer role;
    private String  account;
    private String  email;
    private String  mobile;
    private String  name;
    private Integer gender;
    private Long    storeId;
    private String  remark;

}
