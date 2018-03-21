package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ActivityVo {

    private Long                    id;
    private String                  name;
    private BigDecimal              price;
    private String                  code;
    private Date                    deadline;
    private String                  remark;
    private List<ActivityContentVo> activityContentVos;

}
