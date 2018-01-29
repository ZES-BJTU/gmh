package com.zes.squad.gmh.web.entity.param;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EmployeeWorkQueryParams extends QueryParams {

    private Date    startEntryTime;
    private Date    endEntryTime;
    private Boolean working;
    private Integer topType;
    private String  employeeKeyWord;

}
