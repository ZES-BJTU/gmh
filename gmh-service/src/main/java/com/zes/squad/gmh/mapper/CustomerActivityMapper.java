package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.condition.CustomerActivityQueryCondition;
import com.zes.squad.gmh.entity.po.CustomerActivityPo;
import com.zes.squad.gmh.entity.union.CustomerActivityUnion;

public interface CustomerActivityMapper {

	void insert(CustomerActivityPo customerActivityPo);
	
	List<CustomerActivityUnion> listByCondition(CustomerActivityQueryCondition condition);

	CustomerActivityPo getById(Long id);
}
