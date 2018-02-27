package com.zes.squad.gmh.web.entity.vo;

import lombok.Data;

@Data
public class StoreVo {

    private Long   id;
    private String name;
    private String address;
    private Long   principalId;
    private String principalName;
    private String phone;
    private String remark;

}
