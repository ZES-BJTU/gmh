package com.zes.squad.gmh.entity.po;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductPo extends Po {

    private static final long serialVersionUID = 1L;

    private Long              productTypeId;
    private String            name;
    private String            unitName;
    private BigDecimal        unitPrice;
    private BigDecimal        totalAmount;
    private Long              storeId;

}
