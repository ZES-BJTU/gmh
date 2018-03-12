package com.zes.squad.gmh.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.CustomerMemberCardQueryCondition;
import com.zes.squad.gmh.entity.po.CustomerMemberCardPo;
import com.zes.squad.gmh.entity.union.CustomerMemberCardUnion;
import com.zes.squad.gmh.mapper.CustomerMemberCardMapper;
import com.zes.squad.gmh.service.CustomerMemberCardService;

@Service("customerMemberCardService")
public class CustomerMemberCardServiceImpl implements CustomerMemberCardService{

	@Autowired
	private CustomerMemberCardMapper customerMemberCardMapper;
	@Override
	public PagedList<CustomerMemberCardUnion> listPagedCustomerMemberCard(
			CustomerMemberCardQueryCondition condition) {
		int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<CustomerMemberCardUnion> customerMemberCardUnions = customerMemberCardMapper.listCustomerMemberCardByCondition(condition);
        PageInfo<CustomerMemberCardUnion> info = new PageInfo<>(customerMemberCardUnions);
		return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), customerMemberCardUnions);
	}
	@Override
	public void returnCard(CustomerMemberCardPo po) {
		po.setIsReturned(1);
		po.setIsValid(0);
		po.setReturnedTime(new Date());
		customerMemberCardMapper.returnCard(po);
	}
	@Override
	public void turnCard(CustomerMemberCardPo po) {
		CustomerMemberCardPo oldCard = customerMemberCardMapper.getById(po.getId());
		CustomerMemberCardPo newCard = new CustomerMemberCardPo();
		po.setIsTurned(1);
		po.setIsValid(0);
		po.setTurnedTime(new Date());
		customerMemberCardMapper.turnCard(po);
		newCard.setCustomerId(oldCard.getCustomerId());
		newCard.setIsValid(1);
		newCard.setMemberCardId(po.getMemberCardId());
		newCard.setUniqueIdentifier(po.getUniqueIdentifier());
		newCard.setStoreId(ThreadContext.getUserStoreId());
		customerMemberCardMapper.insert(newCard);
		
	}

}
