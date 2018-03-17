package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MemberCardCreateOrModifyParams {

    private Long       id;
    private Integer    type;
    private String     name;
    private BigDecimal price;
    private Long       projectId;
    private Integer    times;
    private BigDecimal amount;
    private BigDecimal projectDiscount;
    private BigDecimal productDiscount;
    private String     remark;

}
