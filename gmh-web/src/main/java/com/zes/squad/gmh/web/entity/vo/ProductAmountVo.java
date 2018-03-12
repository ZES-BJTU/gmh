package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductAmountVo {

    private Long       id;
    private Long       productId;
    private BigDecimal amount;
    private Long       storeId;

}
