package com.zes.squad.gmh.web.entity.param;

import lombok.Data;

@Data
public class StoreCreateOrModifyParams {

    private Long   id;
    private String name;
    private String address;
    private Long   principalId;
    /**
     * 门店座机
     */
    private String phone;
    private String remark;

}
