package com.zes.squad.gmh.web.entity.param.store;

import lombok.Data;

@Data
public class StoreCreateOrModifyParams {

    private Long   id;
    private String name;
    private String address;
    private Long   principalId;
    private String mobile;
    private String phone;
    private String remark;

}
