package com.zes.squad.gmh.entity.union;

import com.zes.squad.gmh.entity.po.UserPo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserUnion extends Union {

    private static final long serialVersionUID = 1L;

    private UserPo            userPo;
    private String            token;
    private String            storeName;

}
