package com.zes.squad.gmh.mapper;

import java.util.List;
import java.util.Map;

import com.zes.squad.gmh.entity.po.ConsumeSaleEmployeePo;
import com.zes.squad.gmh.entity.union.ConsumeSaleEmployeeUnion;
import com.zes.squad.gmh.entity.union.EmployeeSaleMoney;

public interface ConsumeSaleEmployeeMapper {
	
	List<ConsumeSaleEmployeeUnion> getUnionByrecordId(Long consumeRecordId);
	
	void insert(ConsumeSaleEmployeePo consumeSaleEmployee);
	
	List<EmployeeSaleMoney> getSaleUnionByEmployeeId(Map<String,Object> map); 
}
