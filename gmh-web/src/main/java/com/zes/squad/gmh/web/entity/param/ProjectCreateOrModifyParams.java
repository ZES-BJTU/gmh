package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;
import java.util.List;

import com.zes.squad.gmh.entity.union.ProjectStockParams;

import lombok.Data;

@Data
public class ProjectCreateOrModifyParams {

    private Long                     id;
    private Long                     projectTypeId;
    private String                   name;
    private BigDecimal               unitPrice;
    private BigDecimal               integral;
    private BigDecimal               internIntegral;
    private String                   remark;
    private List<ProjectStockParams> projectStockParams;

}
