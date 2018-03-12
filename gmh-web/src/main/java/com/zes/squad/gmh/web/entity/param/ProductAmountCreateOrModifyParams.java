package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductAmountCreateOrModifyParams {

    private Long       id;
    private Long       productId;
    private BigDecimal amount;

}
