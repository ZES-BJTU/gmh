package com.zes.squad.gmh.web.entity.param;

import java.util.List;

import com.zes.squad.gmh.entity.union.AppointmentProjectParams;

import lombok.Data;

@Data
public class AppointmentCreateOrModifyParams {
    private Long                           id;
    private String                         customerName;
    private String                         customerMobile;
    private Integer                        customerGender;
    private Integer                        isVip;
    private Long                           customerId;
    private Integer                        isLine;
    private Integer                        status;
    private String                         remarks;
    private Integer                        storeId;
    private List<AppointmentProjectParams> appointmentProjectParams;
}
