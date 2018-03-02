package com.zes.squad.gmh.entity.union;

import java.util.List;

import com.zes.squad.gmh.entity.po.EmployeePo;
import com.zes.squad.gmh.entity.po.EmployeeWorkPo;
import com.zes.squad.gmh.entity.po.StorePo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EmployeeUnion extends Union {

    private static final long    serialVersionUID = 1L;

    private EmployeePo           employeePo;
    private StorePo              storePo;
    private List<EmployeeWorkPo> employeeWorkPos;

}
