package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProjectStockVo {

    private Long       stockId;
    private String     stockName;
    private BigDecimal stockConsumptionAmount;

}
