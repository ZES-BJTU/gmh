package com.zes.squad.gmh.entity.union;

import com.zes.squad.gmh.entity.po.EmployeeWorkPo;
import com.zes.squad.gmh.entity.po.EmployeeWorkTypePo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EmployeeWorkUnion extends Union {

    private static final long  serialVersionUID = 1L;

    private EmployeeWorkPo     employeeWorkPo;
    private EmployeeWorkTypePo employeeWorkTypePo;

}
