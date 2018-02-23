package com.zes.squad.gmh.web.entity.vo;

import java.util.List;

import com.zes.squad.gmh.entity.union.AppointmentProjectUnion;

import lombok.Data;

@Data
public class AppointmentVo {

    private Long                          id;
    private String                        customerName;
    private String                        customerMobile;
    private String                        customerGender;
    private String                        isVip;
    private String                        isLine;
    private String                        status;
    private String                        remarks;
    private List<AppointmentProjectUnion> appointmentProjects;

}
