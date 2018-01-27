package com.zes.squad.gmh.web.entity.param.employee;

import java.util.Date;

import com.zes.squad.gmh.web.entity.param.QueryParams;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EmployeeQueryParams extends QueryParams {

    private Date    startEntryTime;
    private Date    endEntryTime;
    private Boolean working;
    private Integer topType;
    private String  employeeKeyWord;

}
