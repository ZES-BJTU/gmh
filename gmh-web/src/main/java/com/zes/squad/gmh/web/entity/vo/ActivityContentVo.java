package com.zes.squad.gmh.web.entity.vo;

import lombok.Data;

@Data
public class ActivityContentVo {

    private Long    id;
    private String  type;
    private String  relatedId;
    private String  content;
    private Integer number;
    private String  remark;

}
