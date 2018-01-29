package com.zes.squad.gmh.web.entity.param;

import lombok.Data;

@Data
public class EmployeeWorkTypeCreateOrModifyParams {

    private Long    id;
    private Integer topType;
    private String  name;
    private Long    storeId;

}
