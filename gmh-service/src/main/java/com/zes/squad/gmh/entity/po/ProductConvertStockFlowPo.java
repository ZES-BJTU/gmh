package com.zes.squad.gmh.entity.po;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductConvertStockFlowPo extends Po {

    private static final long serialVersionUID = 1L;

    private Integer           type;
    private Long              productId;
    private BigDecimal        productAmount;
    private Long              stockId;
    private BigDecimal        stockAmount;
    private Long              storeId;
    private Integer           status;
    private String            remark;

}
