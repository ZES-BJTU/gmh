package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductCreateOrModifyParams {

    private Long       id;
    private Long       productTypeId;
    private String     name;
    private String     unitName;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;

}
