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
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
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
	// @Autowired
	// private MemberCardService memberCardService;
	@Autowired
	private CustomerMapper customerMapper;

	@Override
	public PagedList<CustomerMemberCardUnion> listPagedCustomerMemberCard(CustomerMemberCardQueryCondition condition) {
		int pageNum = condition.getPageNum();
		int pageSize = condition.getPageSize();
		PageHelper.startPage(pageNum, pageSize);
		List<CustomerMemberCardUnion> customerMemberCardUnions = customerMemberCardMapper
				.listCustomerMemberCardByCondition(condition);
		for (CustomerMemberCardUnion cmcu : customerMemberCardUnions) {
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
		for (CustomerMemberCardUnion cmcu : customerMemberCardUnions) {
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
	public void turnCard(Long oldCardId, Long newCardId, BigDecimal returnedMoney, String reason) {
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

		if (memberCard.getType() == 1) {
			cardContent.setRelatedId(memberCard.getProjectId());
			cardContent.setAmount(memberCard.getTimes());
		}
		if (memberCard.getType() == 2) {
			newCard.setRemainingMoney(memberCard.getAmount());
		}
		if (memberCard.getType() == 3) {
			cardContent.setRelatedId(memberCard.getProjectId());
			cardContent.setAmount(memberCard.getTimes());
			newCard.setRemainingMoney(memberCard.getAmount());
		}
		customerMemberCardMapper.insert(newCard);
		if (memberCard.getType() != 2) {
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
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("storeId", ThreadContext.getUserStoreId());
		CustomerPo customer = customerMapper.getByMobile(customerMobile);
		map.put("customerId", customer.getId());
		List<CustomerMemberCardUnion> customerMemberCardUnions = customerMemberCardMapper.getCardListByCustomerId(map);
		for (CustomerMemberCardUnion cmcu : customerMemberCardUnions) {
			cmcu.setCustomerMemberCardContent(customerMemberCardContentMapper.getContentList(cmcu.getId()));
		}

		return customerMemberCardUnions;
	}

	
	public void buyProject(Long cardId, Long projectId, Integer projectTimes, BigDecimal useRemainMoney) {

		CustomerMemberCardContentPo customerMemberCardContentPo = new CustomerMemberCardContentPo();
		CustomerMemberCardPo customerMemberCardPo = customerMemberCardMapper.getById(cardId);

		if(customerMemberCardPo.getRemainingMoney().compareTo(useRemainMoney)==-1){
			throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "会员卡余额不足");
		}
		
		List<CustomerMemberCardContentUnion> customerMemberCardContentUnions = customerMemberCardContentMapper
				.getContentList(cardId);

			// 如果会员卡中存在疗程并且包含新项目，则在原项目剩余次数中添加次数或补充新项目
			if (customerMemberCardContentUnions.size() != 0) {
				boolean have = false;
				Map<String, Object> map = new HashMap<String, Object>();

				for (CustomerMemberCardContentUnion cmccu : customerMemberCardContentUnions) {

					if (cmccu.getRelatedId() == projectId) {

						map.put("id", cmccu.getId());
						map.put("amount", cmccu.getAmount() + projectTimes);
						have = true;
						break;
					}
				}
				if (have) {
					customerMemberCardContentMapper.calAmount(map);
				} else {
					customerMemberCardContentPo.setCustomerMemberCardId(cardId);
					customerMemberCardContentPo.setRelatedId(projectId);
					customerMemberCardContentPo.setAmount(projectTimes);
					customerMemberCardContentMapper.insert(customerMemberCardContentPo);
				}

			} else {																	//如果原会员卡中本身不存在项目 则直接添加
				customerMemberCardContentPo.setCustomerMemberCardId(cardId);
				customerMemberCardContentPo.setRelatedId(projectId);
				customerMemberCardContentPo.setAmount(projectTimes);
				customerMemberCardContentMapper.insert(customerMemberCardContentPo);
			}

		
		
		//调整会员卡余额
		Map<String,Object> moneyMap = new HashMap<String,Object>();
		moneyMap.put("id", cardId);
		moneyMap.put("remainMoney", customerMemberCardPo.getRemainingMoney().subtract(useRemainMoney));
		customerMemberCardMapper.calRemainMoney(moneyMap);

	}

	@Override
	public void recharge(Long cardId, BigDecimal rechargeMoney) {
		CustomerMemberCardPo customerMemberCardPo = customerMemberCardMapper.getById(cardId);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", cardId);
		map.put("remainMoney", customerMemberCardPo.getRemainingMoney().add(rechargeMoney));
		customerMemberCardMapper.calRemainMoney(map);
		
	}

}
