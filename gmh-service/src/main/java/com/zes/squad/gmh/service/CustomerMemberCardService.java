package com.zes.squad.gmh.service;

import java.math.BigDecimal;
import java.util.List;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.CustomerMemberCardQueryCondition;
import com.zes.squad.gmh.entity.po.CustomerMemberCardPo;
import com.zes.squad.gmh.entity.union.CustomerMemberCardUnion;

public interface CustomerMemberCardService {

	PagedList<CustomerMemberCardUnion> listPagedCustomerMemberCard(CustomerMemberCardQueryCondition customerMemberCardQueryCondition);
	
	PagedList<CustomerMemberCardUnion> changedListPagedCustomerMemberCard(CustomerMemberCardQueryCondition customerMemberCardQueryCondition);

	void returnCard(CustomerMemberCardPo po);

	void turnCard(Long oldCardId,Long newCardId,BigDecimal returnedMoney,String reason);
	
	void changeStore(CustomerMemberCardPo po, Long storeId);

	List<CustomerMemberCardUnion> getCardListByMobile(String customerMobile);
}
