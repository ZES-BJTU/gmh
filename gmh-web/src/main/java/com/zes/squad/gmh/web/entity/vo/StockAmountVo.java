package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class StockAmountVo {

    private Long       id;
    private Long       stockId;
    private BigDecimal amount;
    private Long       storeId;

}
