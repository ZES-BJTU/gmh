package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MemberCardVo {

    private Long       id;
    private Integer    type;
    private String     typeDesc;
    private String     code;
    private String     name;
    private BigDecimal price;
    private Long       projectId;
    private String     projectName;
    private Integer    times;
    private BigDecimal amount;
    private BigDecimal projectDiscount;
    private BigDecimal productDiscount;
    private String     remark;
    private String     status;

}
