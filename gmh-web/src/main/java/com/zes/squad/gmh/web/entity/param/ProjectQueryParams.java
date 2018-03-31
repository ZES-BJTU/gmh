package com.zes.squad.gmh.web.entity.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectQueryParams extends QueryParams {

    private Integer topType;
    private String  search;

}
