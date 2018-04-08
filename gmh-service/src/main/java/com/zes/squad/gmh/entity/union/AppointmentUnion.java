package com.zes.squad.gmh.entity.union;

import java.util.List;

import com.zes.squad.gmh.entity.po.AppointmentPo;

import lombok.Data;

@Data
public class AppointmentUnion {

    private AppointmentPo                 appointmentPo;
    private StoreUnion                    storeUnion;
    private List<AppointmentProjectUnion> appointmentProjects;
}
