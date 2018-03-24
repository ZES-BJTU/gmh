package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ActivityContentParams {

    private Integer    type;
    private Long       relatedId;
    private BigDecimal content;
    private BigDecimal number;
    private String     remark;

}
