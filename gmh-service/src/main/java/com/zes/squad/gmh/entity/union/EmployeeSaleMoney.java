package com.zes.squad.gmh.entity.union;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class EmployeeSaleMoney {
    private Long       employeeId;
    private String     employeeName;
    private BigDecimal money;
    private BigDecimal totalMoney;
}
