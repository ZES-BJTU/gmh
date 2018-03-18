package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.condition.CustomerMemberCardQueryCondition;
import com.zes.squad.gmh.entity.po.CustomerMemberCardPo;
import com.zes.squad.gmh.entity.union.CustomerMemberCardUnion;

public interface CustomerMemberCardMapper {

	int insert(CustomerMemberCardPo po);

	List<CustomerMemberCardUnion> listCustomerMemberCardByCondition(CustomerMemberCardQueryCondition condition);
	
	List<CustomerMemberCardUnion> changedListCustomerMemberCardByCondition(CustomerMemberCardQueryCondition condition);
	
	int returnCard(CustomerMemberCardPo po);

	int turnCard(CustomerMemberCardPo po);
	
	CustomerMemberCardPo getById(Long id);
	
}
