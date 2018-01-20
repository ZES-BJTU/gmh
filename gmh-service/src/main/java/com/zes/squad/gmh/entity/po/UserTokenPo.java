package com.zes.squad.gmh.entity.po;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserTokenPo extends Po {

    private static final long serialVersionUID = 1L;

    private Long              userId;
    private String            token;
    private Date              loginTime;

}
