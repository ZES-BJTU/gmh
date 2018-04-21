package com.zes.squad.gmh.entity.union;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class EmployeeIntegralUnion {

    private Long       employeeId;
    private String     employeeName;
    private BigDecimal integral;
    private BigDecimal totalIntegral;
}
