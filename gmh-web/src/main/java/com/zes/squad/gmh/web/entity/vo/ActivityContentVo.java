package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ActivityContentVo {

    private Long       id;
    private String     type;
    private String     relatedId;
    private String     content;
    private BigDecimal number;
    private String     remark;

}
