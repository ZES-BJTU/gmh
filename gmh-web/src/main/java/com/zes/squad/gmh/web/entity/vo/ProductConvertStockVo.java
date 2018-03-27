package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductConvertStockVo {

    private Long       id;
    private Long       productTypeId;
    private String     productTypeName;
    private Long       productId;
    private String     productCode;
    private String     productName;
    private String     productUnitName;
    private BigDecimal productUnitPrice;
    private BigDecimal productAmount;
    private Long       stockTypeId;
    private String     stockTypeName;
    private Long       stockId;
    private String     stockCode;
    private String     stockName;
    private String     stockUnitName;
    private BigDecimal stockAmount;

}
