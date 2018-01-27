package com.zes.squad.gmh.web.entity.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class EmployeeVo {

    private Long                 id;
    private String               name;
    private String               gender;
    private String               mobile;
    private Date                 entryTime;
    private String               working;
    private String               storeName;
    private String               remark;
    private List<EmployeeWorkVo> employeeWorkVos;

}
