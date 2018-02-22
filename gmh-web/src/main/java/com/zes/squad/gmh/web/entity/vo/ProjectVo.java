package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class ProjectVo {

    private Long                 id;
    private String               code;
    private String               topTypeDesc;
    private String               projectTypeName;
    private BigDecimal           unitPrice;
    private BigDecimal           integral;
    private BigDecimal           internIntegral;
    private String               storeName;
    private String               remark;
    private List<ProjectStockVo> projectStockVos;

}
