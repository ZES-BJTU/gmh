package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class StockConvertParams {

    private Long       stockId;
    private BigDecimal amount;
    private Long       toStoreId;

}
