package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductConvertStockFlowParams {

    private Long       id;
    private Integer    type;
    private Long       productId;
    private BigDecimal productAmount;
    private Long       stockId;
    private BigDecimal stockAmount;
    private Long       fromStoreId;
    private Long       toStoreId;
    private String     remark;

}
