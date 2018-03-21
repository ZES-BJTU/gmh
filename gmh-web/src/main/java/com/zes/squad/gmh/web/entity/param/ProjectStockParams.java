package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProjectStockParams {

    private Long       stockId;
    private BigDecimal stockConsumptionAmount;

}
