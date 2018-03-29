package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductVo {

    private Long       id;
    private Long       productTypeId;
    private String     productTypeName;
    private String     code;
    private String     name;
    private String     unitName;
    private BigDecimal unitPrice;
    private Long       productAmountId;
    private BigDecimal amount;
    private Long       storeId;
    private String     storeName;

}
