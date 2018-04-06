package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductConvertStockParams {

    private Long       id;
    private Long       productId;
    private BigDecimal productAmount;
    private Long       stockId;
    private BigDecimal stockAmount;

}
