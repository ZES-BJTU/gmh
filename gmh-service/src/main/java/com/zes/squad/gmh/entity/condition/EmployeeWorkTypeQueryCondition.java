package com.zes.squad.gmh.entity.condition;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EmployeeWorkTypeQueryCondition extends QueryCondition {

    private Long    storeId;
    private Integer topType;
    private String  employeeWorkTypeKeyWord;

}
