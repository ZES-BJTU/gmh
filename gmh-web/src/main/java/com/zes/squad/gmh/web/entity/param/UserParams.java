package com.zes.squad.gmh.web.entity.param;

import lombok.Data;

@Data
public class UserParams {

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
