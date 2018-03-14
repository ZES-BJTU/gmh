package com.zes.squad.gmh.entity.po;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductFlowPo extends Po {

    private static final long serialVersionUID = 1L;

    private Long              productId;
    private Integer           type;
    private BigDecimal        amount;
    private Long              storeId;
    private Integer           status;

}
