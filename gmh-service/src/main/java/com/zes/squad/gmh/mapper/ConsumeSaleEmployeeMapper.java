package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.po.ConsumeSaleEmployeePo;
import com.zes.squad.gmh.entity.union.ConsumeSaleEmployeeUnion;

public interface ConsumeSaleEmployeeMapper {
	
	List<ConsumeSaleEmployeeUnion> getUnionByrecordId(Long consumeRecordId);
	void insert(ConsumeSaleEmployeePo consumeSaleEmployee);
	
}
