package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductConvertParams {

    private Long       productId;
    private BigDecimal amount;
    private Long       toStoreId;

}
