package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.condition.CustomerQueryCondition;
import com.zes.squad.gmh.entity.po.CustomerPo;

public interface CustomerMapper {

	int insert(CustomerPo customerPo);
	
	int update(CustomerPo customerPo);

	int delete(Long id);

	List<CustomerPo> selectByCondition(CustomerQueryCondition condition);
}
