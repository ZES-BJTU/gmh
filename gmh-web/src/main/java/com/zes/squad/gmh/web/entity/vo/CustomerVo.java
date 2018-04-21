package com.zes.squad.gmh.web.entity.vo;

import java.util.Date;

import lombok.Data;

@Data
public class CustomerVo {

    private Long   id;
    private String name;
    private String gender;
    private String mobile;
    private Date   birthday;
    private Date   inputTime;
    private String source;
    private String remark;
}
