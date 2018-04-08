package com.zes.squad.gmh.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.CustomerMemberCardQueryCondition;
import com.zes.squad.gmh.entity.po.CustomerMemberCardPo;
import com.zes.squad.gmh.entity.po.CustomerPo;
import com.zes.squad.gmh.entity.union.CustomerMemberCardUnion;
import com.zes.squad.gmh.mapper.CustomerMapper;
import com.zes.squad.gmh.mapper.CustomerMemberCardContentMapper;
import com.zes.squad.gmh.mapper.CustomerMemberCardMapper;
import com.zes.squad.gmh.service.CustomerMemberCardService;

@Service("customerMemberCardService")
public class CustomerMemberCardServiceImpl implements CustomerMemberCardService {

	@Autowired
	private CustomerMemberCardMapper customerMemberCardMapper;
	@Autowired
	private CustomerMemberCardContentMapper customerMemberCardContentMapper;
//	@Autowired
//	private MemberCardService memberCardService;
	@Autowired
	private CustomerMapper customerMapper;
	@Override
	public PagedList<CustomerMemberCardUnion> listPagedCustomerMemberCard(CustomerMemberCardQueryCondition condition) {
		int pageNum = condition.getPageNum();
		int pageSize = condition.getPageSize();
		PageHelper.startPage(pageNum, pageSize);
		List<CustomerMemberCardUnion> customerMemberCardUnions = customerMemberCardMapper
				.listCustomerMemberCardByCondition(condition);
		for(CustomerMemberCardUnion cmcu:customerMemberCardUnions){
			cmcu.setCustomerMemberCardContent(customerMemberCardContentMapper.getContentList(cmcu.getId()));
		}
		PageInfo<CustomerMemberCardUnion> info = new PageInfo<>(customerMemberCardUnions);
		return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(),
				customerMemberCardUnions);
	}

	public PagedList<CustomerMemberCardUnion> changedListPagedCustomerMemberCard(
			CustomerMemberCardQueryCondition condition) {
		int pageNum = condition.getPageNum();
		int pageSize = condition.getPageSize();
		PageHelper.startPage(pageNum, pageSize);
		List<CustomerMemberCardUnion> customerMemberCardUnions = customerMemberCardMapper
				.changedListCustomerMemberCardByCondition(condition);
		for(CustomerMemberCardUnion cmcu:customerMemberCardUnions){
			cmcu.setCustomerMemberCardContent(customerMemberCardContentMapper.getContentList(cmcu.getId()));
		}
		PageInfo<CustomerMemberCardUnion> info = new PageInfo<>(customerMemberCardUnions);
		return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(),
				customerMemberCardUnions);
	}

	@Override
	public void returnCard(CustomerMemberCardPo po) {
		po.setIsReturned(1);
		po.setIsValid(0);
		po.setReturnedTime(new Date());
		customerMemberCardMapper.returnCard(po);
	}

	@Override
	public void turnCard(CustomerMemberCardPo po, Long cardId) {
		CustomerMemberCardPo oldCard = customerMemberCardMapper.getById(po.getId());
		CustomerMemberCardPo newCard = new CustomerMemberCardPo();
		// MemberCardUnion mcp =
		// memberCardService.queryMemberCardDetail(cardId);
		oldCard.setIsTurned(1);
		oldCard.setIsValid(0);
		oldCard.setTurnedTime(new Date());
		oldCard.setTurnedMoney(po.getTurnedMoney());
		oldCard.setTurnedReason(po.getTurnedReason());
		customerMemberCardMapper.turnCard(oldCard);
		newCard.setCustomerId(oldCard.getCustomerId());
		newCard.setIsValid(1);
		newCard.setMemberCardId(cardId);
		newCard.setUniqueIdentifier(oldCard.getUniqueIdentifier());
		newCard.setStoreId(ThreadContext.getUserStoreId());
		// TODO 补充新卡剩余内容
		customerMemberCardMapper.insert(newCard);

	}

	@Override
	public void changeStore(CustomerMemberCardPo po, Long storeId) {
		CustomerMemberCardPo oldCard = customerMemberCardMapper.getById(po.getId());
		// CustomerMemberCardPo newCard = new CustomerMemberCardPo();
		po.setIsTurned(1);
		po.setIsValid(0);
		po.setTurnedTime(new Date());
		customerMemberCardMapper.turnCard(po);
		// newCard.setCustomerId(oldCard.getCustomerId());
		// newCard.setIsValid(1);
		// newCard.setMemberCardId(oldCard.getMemberCardId());
		// newCard.setUniqueIdentifier(oldCard.getUniqueIdentifier());
		// newCard.setStoreId(storeId);
		oldCard.setStoreId(storeId);
		customerMemberCardMapper.insert(oldCard);

	}

	@Override
	public List<CustomerMemberCardUnion> getCardListByMobile(String customerMobile) {
		Map<String,Object> map = new HashMap<String,Object>();		
		map.put("storeId", ThreadContext.getUserStoreId());
		CustomerPo customer = customerMapper.getByMobile(customerMobile);
		map.put("customerId", customer.getId());
		List<CustomerMemberCardUnion> customerMemberCardUnions = customerMemberCardMapper
				.getCardListByCustomerId(map);
		for(CustomerMemberCardUnion cmcu:customerMemberCardUnions){
			cmcu.setCustomerMemberCardContent(customerMemberCardContentMapper.getContentList(cmcu.getId()));
		}
			
		return customerMemberCardUnions;
	}
	

}
