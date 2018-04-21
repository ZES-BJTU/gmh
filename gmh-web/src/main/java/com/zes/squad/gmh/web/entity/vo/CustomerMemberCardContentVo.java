package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CustomerMemberCardContentVo {
    private Long       id;
    private Long       relatedId;
    private String     relatedName;
    private BigDecimal content;
    private Integer    amount;
}
