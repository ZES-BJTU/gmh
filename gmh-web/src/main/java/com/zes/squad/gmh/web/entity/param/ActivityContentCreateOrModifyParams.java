package com.zes.squad.gmh.web.entity.param;

import lombok.Data;

@Data
public class ActivityContentCreateOrModifyParams {

    private Long    id;
    private Integer type;
    private Long    relatedId;
    private Integer status;

}
