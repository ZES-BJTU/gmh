package com.zes.squad.gmh.entity.union;

import java.util.List;

import com.zes.squad.gmh.entity.po.ProjectPo;
import com.zes.squad.gmh.entity.po.StorePo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectUnion extends Union {

    private static final long       serialVersionUID = 1L;

    private ProjectPo               projectPo;
    private StorePo                 storePo;
    private List<ProjectStockUnion> projectStockUnions;

}
