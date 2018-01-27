package com.zes.squad.gmh.entity.union;

import java.util.List;

import com.zes.squad.gmh.entity.po.EmployeePo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EmployeeUnion extends Union {

    private static final long       serialVersionUID = 1L;

    private EmployeePo              employeePo;
    private List<EmployeeWorkUnion> employeeWorkUnions;
    private String                  storeName;

}
