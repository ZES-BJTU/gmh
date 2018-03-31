package com.zes.squad.gmh.entity.po;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ActivityContentPo extends Po {

    private static final long serialVersionUID = 1L;

    private Long              activityId;
    private Integer           type;
    private Long              relatedId;
    private BigDecimal        content;
    private BigDecimal        number;
    private String            remark;

}
