package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductConvertStockFlowVo {

    private Long       id;
    private Integer    type;
    private Long       productId;
    private BigDecimal productAmount;
    private Long       stockId;
    private BigDecimal stockAmount;
    private Long       storeId;
    private Integer    status;
    private String     remark;

}
