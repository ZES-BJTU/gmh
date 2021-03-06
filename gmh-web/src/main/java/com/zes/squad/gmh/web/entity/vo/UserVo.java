package com.zes.squad.gmh.web.entity.vo;

import lombok.Data;

@Data
public class UserVo {

    private Long   id;
    private String role;
    private String account;
    private String email;
    private String mobile;
    private String name;
    private String gender;
    private Long   storeId;
    private String storeName;
    private String remark;
    private String token;

}
