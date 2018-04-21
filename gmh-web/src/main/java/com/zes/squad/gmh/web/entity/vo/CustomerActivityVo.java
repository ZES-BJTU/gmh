package com.zes.squad.gmh.web.entity.vo;

import java.util.List;

import lombok.Data;

@Data
public class CustomerActivityVo {

    private Long                            id;
    private String                          customerName;
    private String                          activityName;
    private List<CustomerActivityContentVo> customerActivityContents;
}
