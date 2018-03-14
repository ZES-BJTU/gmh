package com.zes.squad.gmh.entity.po;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ActivityPo extends Po {

    private static final long serialVersionUID = 1L;

    private String            name;
    private BigDecimal        price;
    private String            code;
    private Date              deadline;
    private String            remark;

}
