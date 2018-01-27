package com.zes.squad.gmh.web.entity.param.employee;

import com.zes.squad.gmh.web.entity.param.QueryParams;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EmployeeQueryParams extends QueryParams {
    
    private String employeeKeyWord;

}
