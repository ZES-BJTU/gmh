package com.zes.squad.gmh.entity.union;

import com.zes.squad.gmh.entity.po.EmployeeWorkTypePo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EmployeeWorkTypeUnion extends Union {

    private static final long  serialVersionUID = 1L;

    private EmployeeWorkTypePo employeeWorkTypePo;
    private String             storeName;

}
