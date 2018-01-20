package com.zes.squad.gmh.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserPo extends Po {

    private static final long serialVersionUID = 1L;

    private String            account;
    private String            email;
    private String            mobile;
    private String            password;
    private String            salt;
    private String            name;
    private Integer           gender;
    private Long              storeId;
    private String            remark;

}
