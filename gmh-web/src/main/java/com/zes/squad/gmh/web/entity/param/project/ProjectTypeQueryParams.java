package com.zes.squad.gmh.web.entity.param.project;

import com.zes.squad.gmh.web.entity.param.QueryParams;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectTypeQueryParams extends QueryParams {

    private Integer topType;
    private String  projectTypeKeyWord;

}
