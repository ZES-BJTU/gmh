package com.zes.squad.gmh.entity.po;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomerMemberCardFlowPo extends Po {
    private static final long serialVersionUID = 1L;
    private Long              customerMemberCardId;
    private Long              consumeRecordId;
    private BigDecimal        money;
    private Long              customerMemberCardContentId;
    private Integer           amount;
}
