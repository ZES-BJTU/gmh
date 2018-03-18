package com.zes.squad.gmh.entity.po;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberCardPo extends Po {

    private static final long serialVersionUID = 1L;

    private Integer           type;
    private String            code;
    private String            name;
    private BigDecimal        price;
    private Long              projectId;
    private Integer           times;
    private BigDecimal        amount;
    private BigDecimal        projectDiscount;
    private BigDecimal        productDiscount;
    private Long              storeId;
    private String            remark;

}
