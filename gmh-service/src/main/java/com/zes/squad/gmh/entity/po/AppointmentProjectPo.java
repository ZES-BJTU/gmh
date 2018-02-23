package com.zes.squad.gmh.entity.po;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppointmentProjectPo extends Po {

    private static final long serialVersionUID = 1L;

    private Long              appointmentId;
    private Long              projectId;
    private Long              employeeId;
    private Date              beginTime;
    private Date              endTime;
}
