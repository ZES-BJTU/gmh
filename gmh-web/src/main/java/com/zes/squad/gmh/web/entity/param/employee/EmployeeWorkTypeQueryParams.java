package com.zes.squad.gmh.web.entity.param.employee;

import com.zes.squad.gmh.web.entity.param.QueryParams;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EmployeeWorkTypeQueryParams extends QueryParams {

    private Integer topType;
    private String  employeeWorkTypeKeyWord;

}
