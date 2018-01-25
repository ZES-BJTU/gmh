package com.zes.squad.gmh.entity.po;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StockPo extends Po {

    private static final long serialVersionUID = 1L;

    private Long              stockTypeId;
    private String            name;
    private String            unitName;
    private BigDecimal        totalAmount;

}
