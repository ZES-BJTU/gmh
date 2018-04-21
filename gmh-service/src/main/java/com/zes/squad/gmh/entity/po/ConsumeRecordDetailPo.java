package com.zes.squad.gmh.entity.po;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConsumeRecordDetailPo extends Po {

    private static final long serialVersionUID = 1L;

    private Long              consumeRecordId;
    private String            tradeSerialNumber;
    private Long              productId;
    private String            productCode;
    private Long              projectId;
    private String            projectCode;
    private BigDecimal        projectDiscount;
    private BigDecimal        productDiscount;
    private Long              cardId;
    private Date              validDate;
    //	private String cardCode;
    private BigDecimal        amount;
    private Long              operatorId;
    private Long              consultantId;
    private Long              salesManId;
}
