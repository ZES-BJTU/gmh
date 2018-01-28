package com.zes.squad.gmh.entity.condition;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EmployeeWorkQueryCondition extends QueryCondition {

    private Long    storeId;
    private Date    startEntryTime;
    private Date    endEntryTime;
    private Boolean working;
    private Integer topType;
    private String  employeeKeyWord;

}
