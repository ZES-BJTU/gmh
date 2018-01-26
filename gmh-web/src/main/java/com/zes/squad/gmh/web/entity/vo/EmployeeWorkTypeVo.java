package com.zes.squad.gmh.web.entity.vo;

import lombok.Data;

@Data
public class EmployeeWorkTypeVo {

    private Long    id;
    private Integer topType;
    private String  topTypeDesc;
    private String  name;
    private Long    storeId;
    private String  storeName;

}
