package com.zes.squad.gmh.web.entity.param;

import lombok.Data;

@Data
public class StoreParams {

    private Long   id;
    private String name;
    private String address;
    /**
     * 门店座机
     */
    private String phone;
    private String remark;

}
