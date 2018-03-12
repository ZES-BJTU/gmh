package com.zes.squad.gmh.service;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.CustomerMemberCardQueryCondition;
import com.zes.squad.gmh.entity.po.CustomerMemberCardPo;
import com.zes.squad.gmh.entity.union.CustomerMemberCardUnion;

public interface CustomerMemberCardService {

	PagedList<CustomerMemberCardUnion> listPagedCustomerMemberCard(CustomerMemberCardQueryCondition customerMemberCardQueryCondition);

	void returnCard(CustomerMemberCardPo po);

	void turnCard(CustomerMemberCardPo po);
}
