package com.zes.squad.gmh.web.entity.param;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class EmployeeCreateOrModifyParams {

    private Long       id;
    private String     name;
    private Integer    gender;
    private String     mobile;
    private Date       entryTime;
    private Boolean    working;
    private String     remark;
    private List<Long> employeeWorkTypeIds;

}
