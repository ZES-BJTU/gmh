package com.zes.squad.gmh.entity.union;

import com.zes.squad.gmh.entity.po.ProjectTypePo;
import com.zes.squad.gmh.entity.po.StorePo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectTypeUnion extends Union {

    private static final long serialVersionUID = 1L;

    private ProjectTypePo     projectTypePo;
    private StorePo           storePo;

}
