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
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.CustomerMemberCardQueryCondition;
import com.zes.squad.gmh.entity.po.ConsumeRecordDetailPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.entity.po.CustomerMemberCardContentPo;
import com.zes.squad.gmh.entity.po.CustomerMemberCardPo;
import com.zes.squad.gmh.entity.po.CustomerPo;
import com.zes.squad.gmh.entity.po.MemberCardPo;
import com.zes.squad.gmh.entity.union.CustomerMemberCardContentUnion;
import com.zes.squad.gmh.entity.union.CustomerMemberCardUnion;
import com.zes.squad.gmh.mapper.ConsumeRecordDetailMapper;
import com.zes.squad.gmh.mapper.ConsumeRecordMapper;
import com.zes.squad.gmh.mapper.CustomerMapper;
import com.zes.squad.gmh.mapper.CustomerMemberCardContentMapper;
import com.zes.squad.gmh.mapper.CustomerMemberCardMapper;
import com.zes.squad.gmh.mapper.MemberCardMapper;
import com.zes.squad.gmh.mapper.TradeSerialNumberMapper;
import com.zes.squad.gmh.service.ConsumeRecordService;
import com.zes.squad.gmh.service.CustomerMemberCardService;


@Service("customerMemberCardService")
public class CustomerMemberCardServiceImpl implements CustomerMemberCardService {

	@Autowired
	private CustomerMemberCardMapper customerMemberCardMapper;
	@Autowired
	private MemberCardMapper memberCardMapper;
	@Autowired
	private CustomerMemberCardContentMapper customerMemberCardContentMapper;
	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private ConsumeRecordMapper consumeRecordMapper;
	@Autowired
	private ConsumeRecordDetailMapper consumeRecordDetailMapper;
	@Autowired
	private ConsumeRecordService consumeRecordService;
	@Autowired
	private TradeSerialNumberMapper tradeSerialNumberMapper;

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
			cardContent.setCustomerMemberCardId(newCard.getId());
			customerMemberCardContentMapper.insert(cardContent);
		}

		// 将此次交易记录到消费记录中
		CustomerPo customer = customerMapper.getById(newCard.getCustomerId());
		ConsumeRecordPo consumeRecord = new ConsumeRecordPo();

		Map<String, Object> tmpMap = new HashMap<String, Object>();
		tmpMap = consumeRecordService.getTradeSerialNumber("C");

		String tradeSerialNumber = (String) tmpMap.get("tradeSerialNumber");
		Integer oldNumber = (Integer) tmpMap.get("oldNumber");
		consumeRecord.setTradeSerialNumber(tradeSerialNumber);
		consumeRecord.setIsModified(0);
		tradeSerialNumberMapper.cardNumberAdd(oldNumber + 1);

		consumeRecord.setConsumeMoney(returnedMoney);
		consumeRecord.setConsumeTime(new Date());
		consumeRecord.setConsumeType(1);
		consumeRecord.setCustomerMobile(customer.getMobile());
		consumeRecord.setPaymentWay(3);
		consumeRecord.setStoreId(ThreadContext.getUserStoreId());
		consumeRecord.setCustomerId(customer.getId());
		consumeRecord.setRemark("换卡");
		consumeRecordMapper.insert(consumeRecord);

	}

	@Override
	public void changeStore(CustomerMemberCardPo po, Long storeId, BigDecimal money, String reason) {
		CustomerMemberCardPo oldCard = customerMemberCardMapper.getById(po.getId());
		CustomerMemberCardPo newCard = CommonConverter.map(oldCard, CustomerMemberCardPo.class);
		newCard.setIsTurned(1);
		newCard.setIsValid(0);
		newCard.setTurnedTime(new Date());
		newCard.setTurnedReason(reason);
		
		// 将此次交易记录到消费记录中
		CustomerPo customer = customerMapper.getById(oldCard.getCustomerId());
		ConsumeRecordPo consumeRecord = new ConsumeRecordPo();

		Map<String, Object> tmpMap = new HashMap<String, Object>();
		tmpMap = consumeRecordService.getTradeSerialNumber("C");

		String tradeSerialNumber = (String) tmpMap.get("tradeSerialNumber");
		Integer oldNumber = (Integer) tmpMap.get("oldNumber");
		consumeRecord.setTradeSerialNumber(tradeSerialNumber);
		consumeRecord.setIsModified(0);
		tradeSerialNumberMapper.cardNumberAdd(oldNumber + 1);

		consumeRecord.setConsumeMoney(money);
		consumeRecord.setConsumeTime(new Date());
		consumeRecord.setConsumeType(1);
		consumeRecord.setCustomerMobile(customer.getMobile());
		consumeRecord.setPaymentWay(3);
		consumeRecord.setStoreId(ThreadContext.getUserStoreId());
		consumeRecord.setCustomerId(customer.getId());
		consumeRecord.setRemark("转店");
		consumeRecordMapper.insert(consumeRecord);

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", oldCard.getId());
		map.put("storeId", storeId);
		customerMemberCardMapper.changeStore(map);
		newCard.setId(null);
		customerMemberCardMapper.insert(newCard);

	}

	@Override
	public List<CustomerMemberCardUnion> getCardListByMobile(Integer paymentWay, String customerMobile) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("storeId", ThreadContext.getUserStoreId());
		CustomerPo customer = customerMapper.getByMobile(customerMobile);
		map.put("customerId", customer.getId());
		List<CustomerMemberCardUnion> customerMemberCardUnions = customerMemberCardMapper.getCardListByCustomerId(map);
		if (paymentWay == 1) {
			for (CustomerMemberCardUnion cmcu : customerMemberCardUnions) {
				cmcu.setCustomerMemberCardContent(customerMemberCardContentMapper.getProjectContentList(cmcu.getId()));
			}
		} else if (paymentWay == 31) {
			for (CustomerMemberCardUnion cmcu : customerMemberCardUnions) {
				cmcu.setCustomerMemberCardContent(customerMemberCardContentMapper.getCouponContentList(cmcu.getId()));
			}
		}

		return customerMemberCardUnions;
	}

	public void buyProject(Long cardId, Long projectId, Integer projectTimes, BigDecimal useRemainMoney) {

		CustomerMemberCardContentPo customerMemberCardContentPo = new CustomerMemberCardContentPo();
		CustomerMemberCardPo customerMemberCardPo = customerMemberCardMapper.getById(cardId);

		if (customerMemberCardPo.getRemainingMoney().compareTo(useRemainMoney) == -1) {
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

		} else { // 如果原会员卡中本身不存在项目 则直接添加
			customerMemberCardContentPo.setCustomerMemberCardId(cardId);
			customerMemberCardContentPo.setRelatedId(projectId);
			customerMemberCardContentPo.setAmount(projectTimes);
			customerMemberCardContentMapper.insert(customerMemberCardContentPo);
		}

		// 调整会员卡余额
		Map<String, Object> moneyMap = new HashMap<String, Object>();
		moneyMap.put("id", cardId);
		moneyMap.put("remainMoney", customerMemberCardPo.getRemainingMoney().subtract(useRemainMoney));
		customerMemberCardMapper.calRemainMoney(moneyMap);

	}

	@Override
	public void recharge(Long cardId, BigDecimal rechargeMoney, Long consultantId, Long salesManId) {
		// 更新客户会员卡记录
		CustomerMemberCardPo customerMemberCardPo = customerMemberCardMapper.getById(cardId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", cardId);
		if (customerMemberCardPo.getRemainingMoney() == null) {
			map.put("remainMoney", rechargeMoney);
		} else {
			map.put("remainMoney", customerMemberCardPo.getRemainingMoney().add(rechargeMoney));
		}
		customerMemberCardMapper.calRemainMoney(map);

		// 将此次充值记录到消费记录中
		CustomerPo customer = customerMapper.getById(customerMemberCardPo.getCustomerId());
		ConsumeRecordPo consumeRecord = new ConsumeRecordPo();

		Map<String, Object> tmpMap = new HashMap<String, Object>();
		tmpMap = consumeRecordService.getTradeSerialNumber("C");

		String tradeSerialNumber = (String) tmpMap.get("tradeSerialNumber");
		Integer oldNumber = (Integer) tmpMap.get("oldNumber");
		consumeRecord.setTradeSerialNumber(tradeSerialNumber);
		consumeRecord.setIsModified(0);
		tradeSerialNumberMapper.cardNumberAdd(oldNumber + 1);

		consumeRecord.setConsumeMoney(rechargeMoney);
		consumeRecord.setConsumeTime(new Date());
		consumeRecord.setConsumeType(1);
		consumeRecord.setCustomerMobile(customer.getMobile());
		consumeRecord.setPaymentWay(3);
		consumeRecord.setStoreId(ThreadContext.getUserStoreId());
		consumeRecord.setCustomerId(customer.getId());
		consumeRecord.setRemark("充值");
		consumeRecordMapper.insert(consumeRecord);
		// 消费内容
		ConsumeRecordDetailPo consumeRecordDetailPo = new ConsumeRecordDetailPo();
		consumeRecordDetailPo.setCardId(cardId);
		consumeRecordDetailPo.setConsultantId(consultantId);
		consumeRecordDetailPo.setConsumeRecordId(consumeRecord.getId());
		consumeRecordDetailPo.setSalesManId(salesManId);
		consumeRecordDetailMapper.insert(consumeRecordDetailPo);

	}

}
