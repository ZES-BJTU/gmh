package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class ProjectVo {

    private Long                 id;
    private Integer              topType;
    private String               topTypeDesc;
    private Long                 projectTypeId;
    private String               projectTypeName;
    private String               code;
    private String               name;
    private BigDecimal           unitPrice;
    private BigDecimal           integral;
    private BigDecimal           internIntegral;
    private String               remark;
    private List<ProjectStockVo> projectStockVos;

}
