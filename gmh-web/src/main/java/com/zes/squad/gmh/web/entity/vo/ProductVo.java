package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductVo {

    private Long       id;
    private Long       productTypeId;
    private String     name;
    private String     unitName;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private Long       storeId;

}
