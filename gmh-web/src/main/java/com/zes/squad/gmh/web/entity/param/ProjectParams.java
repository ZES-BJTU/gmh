package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class ProjectParams {

    private Long                     id;
    private Long                     projectTypeId;
    private String                   code;
    private String                   name;
    private BigDecimal               unitPrice;
    private BigDecimal               integral;
    private BigDecimal               internIntegral;
    private String                   remark;
    private List<ProjectStockParams> projectStockParams;

}
