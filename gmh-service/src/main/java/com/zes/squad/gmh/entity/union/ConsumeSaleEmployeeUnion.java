package com.zes.squad.gmh.entity.union;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ConsumeSaleEmployeeUnion {

    private Long       consumeRecordId;
    private Long       employeeId;
    private String     employeeName;
    private BigDecimal percent;
}
