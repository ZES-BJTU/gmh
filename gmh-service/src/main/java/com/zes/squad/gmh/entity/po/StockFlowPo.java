package com.zes.squad.gmh.entity.po;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StockFlowPo extends Po {

    private static final long serialVersionUID = 1L;

    private Long              stockId;
    private Long              recordId;
    private Integer           type;
    private BigDecimal        amount;
    private Long              storeId;
    private Integer           status;

}
