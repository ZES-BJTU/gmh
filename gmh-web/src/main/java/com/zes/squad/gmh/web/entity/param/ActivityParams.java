package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ActivityParams {

    private Long                        id;
    private String                      name;
    private String                      code;
    private BigDecimal                  price;
    private Date                        deadline;
    private String                      remark;
    private List<ActivityContentParams> activityContentParams;

}
