package com.zes.squad.gmh.web.entity.param.user;

import com.zes.squad.gmh.web.entity.param.QueryParams;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserQueryParams extends QueryParams {
    
    private String userKeyWord;

}
