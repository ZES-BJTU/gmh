package com.zes.squad.gmh.service.impl;

import java.math.BigDecimal;
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
import com.zes.squad.gmh.entity.po.CustomerMemberCardContentPo;
import com.zes.squad.gmh.entity.po.CustomerMemberCardPo;
import com.zes.squad.gmh.entity.po.CustomerPo;
import com.zes.squad.gmh.entity.po.MemberCardPo;
import com.zes.squad.gmh.entity.union.CustomerMemberCardContentUnion;
import com.zes.squad.gmh.entity.union.CustomerMemberCardUnion;
import com.zes.squad.gmh.mapper.CustomerMapper;
import com.zes.squad.gmh.mapper.CustomerMemberCardContentMapper;
import com.zes.squad.gmh.mapper.CustomerMemberCardMapper;
import com.zes.squad.gmh.mapper.MemberCardMapper;
import com.zes.squad.gmh.service.CustomerMemberCardService;

@Service("customerMemberCardService")
public class CustomerMemberCardServiceImpl implements CustomerMemberCardService {

	@Autowired
	private CustomerMemberCardMapper customerMemberCardMapper;
	@Autowired
	private MemberCardMapper memberCardMapper;
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
	public void turnCard(Long oldCardId,Long newCardId,BigDecimal returnedMoney,String reason) {
		CustomerMemberCardPo oldCard = customerMemberCardMapper.getById(oldCardId);
		CustomerMemberCardPo newCard = new CustomerMemberCardPo();
		// MemberCardUnion mcp =
		// memberCardService.queryMemberCardDetail(cardId);
		oldCard.setIsTurned(1);
		oldCard.setIsValid(0);
		oldCard.setTurnedTime(new Date());
		oldCard.setTurnedMoney(returnedMoney);
		oldCard.setTurnedReason(reason);
		customerMemberCardMapper.turnCard(oldCard);
		
		newCard.setCustomerId(oldCard.getCustomerId());
		newCard.setIsValid(1);
		newCard.setMemberCardId(newCardId);
		newCard.setUniqueIdentifier(oldCard.getUniqueIdentifier());
		newCard.setStoreId(ThreadContext.getUserStoreId());
		MemberCardPo memberCard = memberCardMapper.selectById(newCardId);
		CustomerMemberCardContentPo cardContent = new CustomerMemberCardContentPo();
		
		if(memberCard.getType()==1){
			cardContent.setRelatedId(memberCard.getProjectId());
			cardContent.setAmount(memberCard.getTimes());
		}
		if(memberCard.getType()==2){
			newCard.setRemainingMoney(memberCard.getAmount());
		}
		if(memberCard.getType()==3){
			cardContent.setRelatedId(memberCard.getProjectId());
			cardContent.setAmount(memberCard.getTimes());
			newCard.setRemainingMoney(memberCard.getAmount());
		}
		customerMemberCardMapper.insert(newCard);
		if(memberCard.getType()!=2){
			cardContent.setCustomerMemberCardId(memberCard.getId());
			customerMemberCardContentMapper.insert(cardContent);
		}
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

	@Override
	public void rechargeOrBuyProject(Long cardId, Long projectId, Integer projectTimes, BigDecimal rechargeMoney) {
		
		CustomerMemberCardContentPo customerMemberCardContentPo = new CustomerMemberCardContentPo();
		CustomerMemberCardPo customerMemberCardPo = new CustomerMemberCardPo();
		
		List<CustomerMemberCardContentUnion> customerMemberCardContentUnion = customerMemberCardContentMapper.getContentList(cardId);
		
		
		
	}
	

}
