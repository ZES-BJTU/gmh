package com.zes.squad.gmh.web.entity.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberCardQueryParams extends QueryParams {

    private Integer type;
    private String  search;

}
