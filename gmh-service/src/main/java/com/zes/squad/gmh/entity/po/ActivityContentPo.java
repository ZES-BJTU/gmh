package com.zes.squad.gmh.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ActivityContentPo extends Po {

    private static final long serialVersionUID = 1L;

    private Integer           type;
    private Long              relatedId;
    private Integer           status;

}
