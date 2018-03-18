package com.zes.squad.gmh.entity.po;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectPo extends Po {

    private static final long serialVersionUID = 1L;

    private Long              projectTypeId;
    private String            code;
    private String            name;
    private BigDecimal        unitPrice;
    private BigDecimal        integral;
    private BigDecimal        internIntegral;
    private String            remark;

}
