package com.zes.squad.gmh.entity.union;

import com.zes.squad.gmh.entity.po.ProjectStockPo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectStockUnion extends Union {

    private static final long serialVersionUID = 1L;

    private ProjectStockPo    projectStockPo;
    private StockUnion        stockUnion;

}
