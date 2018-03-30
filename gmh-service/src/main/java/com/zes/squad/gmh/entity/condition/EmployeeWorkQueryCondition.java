package com.zes.squad.gmh.entity.condition;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EmployeeWorkQueryCondition extends QueryCondition {

    private Date    startEntryTime;
    private Date    endEntryTime;
    private Boolean working;
    private Integer workType;
    private Long    storeId;
    private String  search;

}
