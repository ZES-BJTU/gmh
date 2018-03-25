package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class StockAmountParams {

    private Long       id;
    private Long       stockId;
    private BigDecimal amount;

}
