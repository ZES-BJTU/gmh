package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductAmountParams {

    private Long       id;
    private Long       productId;
    private BigDecimal amount;

}
