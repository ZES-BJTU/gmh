package com.zes.squad.gmh.entity.union;

import java.util.Date;

import lombok.Data;

@Data
public class EmployeeTimeTable {

    private Date    beginTime;
    private Date    endTime;
    private Integer isFree;
    private Double  percent;
}
