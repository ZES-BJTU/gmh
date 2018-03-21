package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductParams {

    private Long       id;
    private Long       productTypeId;
    private String     name;
    private String     unitName;
    private BigDecimal unitPrice;

}
