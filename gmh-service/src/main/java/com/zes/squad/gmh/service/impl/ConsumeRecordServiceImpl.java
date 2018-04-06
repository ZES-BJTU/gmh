package com.zes.squad.gmh.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
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
import com.zes.squad.gmh.entity.condition.ConsumeRecordQueryCondition;
import com.zes.squad.gmh.entity.po.ActivityContentPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordDetailPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordGiftPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.entity.po.CustomerActivityContentPo;
import com.zes.squad.gmh.entity.po.CustomerActivityPo;
import com.zes.squad.gmh.entity.po.CustomerMemberCardContentPo;
import com.zes.squad.gmh.entity.po.CustomerMemberCardPo;
import com.zes.squad.gmh.entity.po.CustomerPo;
import com.zes.squad.gmh.entity.po.MemberCardPo;
import com.zes.squad.gmh.entity.po.ProductFlowPo;
import com.zes.squad.gmh.entity.po.ProjectStockPo;
import com.zes.squad.gmh.entity.po.StockFlowPo;
import com.zes.squad.gmh.entity.union.ConsumeRecordDetailUnion;
import com.zes.squad.gmh.entity.union.ConsumeRecordGiftUnion;
import com.zes.squad.gmh.entity.union.ConsumeRecordUnion;
import com.zes.squad.gmh.entity.union.CustomerActivityContentUnion;
import com.zes.squad.gmh.entity.union.CustomerMemberCardContentUnion;
import com.zes.squad.gmh.mapper.ActivityContentMapper;
import com.zes.squad.gmh.mapper.ConsumeRecordDetailMapper;
import com.zes.squad.gmh.mapper.ConsumeRecordDetailUnionMapper;
import com.zes.squad.gmh.mapper.ConsumeRecordGiftMapper;
import com.zes.squad.gmh.mapper.ConsumeRecordMapper;
import com.zes.squad.gmh.mapper.CustomerActivityContentMapper;
import com.zes.squad.gmh.mapper.CustomerActivityMapper;
import com.zes.squad.gmh.mapper.CustomerMapper;
import com.zes.squad.gmh.mapper.CustomerMemberCardContentMapper;
import com.zes.squad.gmh.mapper.CustomerMemberCardMapper;
import com.zes.squad.gmh.mapper.ProjectStockMapper;
import com.zes.squad.gmh.mapper.TradeSerialNumberMapper;
import com.zes.squad.gmh.service.ConsumeRecordService;
import com.zes.squad.gmh.service.StockService;

@Service("consumeRecordService")
public class ConsumeRecordServiceImpl implements ConsumeRecordService {

	@Autowired
	private ConsumeRecordMapper consumeRecordMapper;
	@Autowired
	private TradeSerialNumberMapper tradeSerialNumberMapper;
	@Autowired
	private ConsumeRecordDetailMapper consumeRecordDetailMapper;
	@Autowired
	private ConsumeRecordDetailUnionMapper consumeRecordDetailUnionMapper;
	@Autowired
	private ConsumeRecordGiftMapper consumeRecordGiftMapper;
	@Autowired
	private ProjectStockMapper projectStockMapper;
	@Autowired
	private CustomerMemberCardMapper customerMemberCardMapper;
	@Autowired
	private CustomerMemberCardContentMapper customerMemberCardContentMapper;
	@Autowired
	private ActivityContentMapper activityContentMapper;
	@Autowired
	private CustomerActivityContentMapper customerActivityContentMapper;
	@Autowired
	private CustomerActivityMapper customerActivityMapper;
	@Autowired
	private StockService stockService;
	@Autowired
	private CustomerMapper customerMapper;

	@Override
	public void createProductConsumeRecord(Map<String, Object> map, ConsumeRecordPo consumeRecord,
			List<ConsumeRecordDetailPo> consumeRecordDetails) {
		String tradeSerialNumber = (String) map.get("tradeSerialNumber");
		Integer oldNumber = (Integer) map.get("oldNumber");
		consumeRecord.setTradeSerialNumber(tradeSerialNumber);
		consumeRecord.setStoreId(ThreadContext.getUserStoreId());
		consumeRecord.setIsModified(0);

		consumeRecordMapper.insert(consumeRecord);
		tradeSerialNumberMapper.productNumberAdd(oldNumber + 1);

		for (ConsumeRecordDetailPo detail : consumeRecordDetails) {
			detail.setConsumeRecordId(consumeRecord.getId());
			consumeRecordDetailMapper.insert(detail);
		}
		calAmount(consumeRecord, consumeRecordDetails, new ArrayList<ConsumeRecordGiftPo>());
		calMoney(consumeRecord, consumeRecordDetails);
	}

	public Map<String, Object> getTradeSerialNumber(String type) {
		String tradeSerialNumber = type + ThreadContext.getUserStoreId();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		tradeSerialNumber = tradeSerialNumber + sdf.format(date);
		Integer oldNumber = 0;
		if (type == "A") {
			oldNumber = tradeSerialNumberMapper.getActivityNumber();
		} else if (type == "B") {
			oldNumber = tradeSerialNumberMapper.getProductNumber();
		} else if (type == "C") {
			oldNumber = tradeSerialNumberMapper.getCardNumber();
		} else if (type == "D") {
			oldNumber = tradeSerialNumberMapper.getProjectNumber();
		}
		Integer tmpNumber = 10000 + oldNumber;
		String number = tmpNumber.toString().substring(1, 5);
		tradeSerialNumber = tradeSerialNumber + number;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tradeSerialNumber", tradeSerialNumber);
		map.put("oldNumber", oldNumber);
		return map;
	}

	@Override
	public void createCardConsumeRecord(Map<String, Object> map, ConsumeRecordPo consumeRecord,
			List<ConsumeRecordDetailPo> consumeRecordDetails, List<ConsumeRecordGiftPo> gifts,
			MemberCardPo memberCardPo) {
		String tradeSerialNumber = (String) map.get("tradeSerialNumber");
		Integer oldNumber = (Integer) map.get("oldNumber");
		consumeRecord.setTradeSerialNumber(tradeSerialNumber);
		consumeRecord.setStoreId(ThreadContext.getUserStoreId());
		consumeRecord.setIsModified(0);

		CustomerMemberCardPo customerMemberCardPo = new CustomerMemberCardPo();
		CustomerPo customerPo = customerMapper.getByMobile(consumeRecord.getCustomerMobile());
		if (customerPo == null)
			throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "请先建立客户信息");

		customerMemberCardPo.setCustomerId(customerPo.getId());
		customerMemberCardPo.setIsReturned(0);
		customerMemberCardPo.setIsTurned(0);
		customerMemberCardPo.setIsValid(1);
		customerMemberCardPo.setStoreId(ThreadContext.getUserStoreId());
		customerMemberCardPo.setUniqueIdentifier(tradeSerialNumber);
		customerMemberCardPo.setMemberCardId(memberCardPo.getId());
		customerMemberCardPo.setProductDiscount(memberCardPo.getProductDiscount());
		customerMemberCardPo.setProjectDiscount(memberCardPo.getProjectDiscount());
		customerMemberCardPo.setRemainingMoney(memberCardPo.getAmount());

		consumeRecord.setCustomerId(customerPo.getId());
		consumeRecordMapper.insert(consumeRecord);
		tradeSerialNumberMapper.cardNumberAdd(oldNumber + 1);
		customerMemberCardMapper.insert(customerMemberCardPo);

		ConsumeRecordDetailPo consumeRecordDetailPo = consumeRecordDetails.get(0);
		consumeRecordDetailPo.setConsumeRecordId(consumeRecord.getId());
		consumeRecordDetailMapper.insert(consumeRecordDetailPo);

		CustomerMemberCardContentPo customerMemberCardContentPo = new CustomerMemberCardContentPo();
		customerMemberCardContentPo.setCustomerMemberCardId(memberCardPo.getId());
		if (memberCardPo.getType() != 2) {

			customerMemberCardContentPo.setRelatedId(memberCardPo.getProjectId());
			customerMemberCardContentPo.setAmount(memberCardPo.getTimes());
			customerMemberCardContentMapper.insert(customerMemberCardContentPo);
		}

		for (ConsumeRecordGiftPo gift : gifts) {
			gift.setConsumeRecordId(consumeRecord.getId());
			consumeRecordGiftMapper.insert(gift);
			if (gift.getProjectId() != null) {
				customerMemberCardContentPo.setRelatedId(gift.getProjectId());
				customerMemberCardContentPo.setAmount(gift.getProjectAmount());
			}
			if (gift.getCouponMoney() != null) {
				customerMemberCardContentPo.setContent(gift.getCouponMoney());
				customerMemberCardContentPo.setAmount(gift.getCouponAmount());
			}
		}
		calAmount(consumeRecord, consumeRecordDetails, gifts);
		calMoney(consumeRecord, consumeRecordDetails);
	}

	@Override
	public void createProjectConsumeRecord(Map<String, Object> map, ConsumeRecordPo consumeRecord,
			List<ConsumeRecordDetailPo> consumeRecordDetails) {
		String tradeSerialNumber = (String) map.get("tradeSerialNumber");
		Integer oldNumber = (Integer) map.get("oldNumber");
		consumeRecord.setTradeSerialNumber(tradeSerialNumber);
		consumeRecord.setStoreId(ThreadContext.getUserStoreId());
		consumeRecord.setIsModified(0);

		consumeRecordMapper.insert(consumeRecord);
		tradeSerialNumberMapper.projectNumberAdd(oldNumber + 1);

		for (ConsumeRecordDetailPo detail : consumeRecordDetails) {
			detail.setConsumeRecordId(consumeRecord.getId());
			consumeRecordDetailMapper.insert(detail);
		}
		calAmount(consumeRecord, consumeRecordDetails, new ArrayList<ConsumeRecordGiftPo>());
		calMoney(consumeRecord, consumeRecordDetails);
	}

	@Override
	public void createActivityConsumeRecord(Map<String, Object> map, ConsumeRecordPo consumeRecord,
			List<ConsumeRecordDetailPo> consumeRecordDetails) {
		String tradeSerialNumber = (String) map.get("tradeSerialNumber");
		Integer oldNumber = (Integer) map.get("oldNumber");
		consumeRecord.setTradeSerialNumber(tradeSerialNumber);
		consumeRecord.setStoreId(ThreadContext.getUserStoreId());
		consumeRecord.setIsModified(0);

		consumeRecordMapper.insert(consumeRecord);
		tradeSerialNumberMapper.activityNumberAdd(oldNumber + 1);

		ConsumeRecordDetailPo detail = consumeRecordDetails.get(0);
		detail.setConsumeRecordId(consumeRecord.getId());
		consumeRecordDetailMapper.insert(detail);

		CustomerActivityPo caPo = new CustomerActivityPo();
		caPo.setCustomerId(consumeRecord.getCustomerId());
		caPo.setActivityId(consumeRecord.getActivityId());
		caPo.setStoreId(ThreadContext.getUserStoreId());
		customerActivityMapper.insert(caPo);
		List<ActivityContentPo> acList = activityContentMapper.selectByActivityId(caPo.getId());

		for (ActivityContentPo ac : acList) {
			CustomerActivityContentPo cacPo = CommonConverter.map(ac, CustomerActivityContentPo.class);
			cacPo.setCustomerActivityId(caPo.getId());
			customerActivityContentMapper.insert(cacPo);
		}
		calAmount(consumeRecord, consumeRecordDetails, new ArrayList<ConsumeRecordGiftPo>());
		calMoney(consumeRecord, consumeRecordDetails);
	}

	@Override
	public PagedList<ConsumeRecordUnion> listPagedConsumeRecords(ConsumeRecordQueryCondition condition) {
		int pageNum = condition.getPageNum();
		int pageSize = condition.getPageSize();
		PageHelper.startPage(pageNum, pageSize);
		ConsumeRecordUnion consumeRecordUnion = new ConsumeRecordUnion();
		List<ConsumeRecordUnion> consumeRecordUnions = new ArrayList<ConsumeRecordUnion>();
		List<ConsumeRecordPo> consumeRecordPos = consumeRecordMapper.listConsumeRecordByCondition(condition);
		List<ConsumeRecordDetailUnion> consumeRecordDetailUnioins = new ArrayList<ConsumeRecordDetailUnion>();
		List<ConsumeRecordGiftUnion> consumeRecordGiftUnions = new ArrayList<ConsumeRecordGiftUnion>();
		if (CollectionUtils.isEmpty(consumeRecordPos)) {
			return PagedLists.newPagedList(pageNum, pageSize);
		}
		for (ConsumeRecordPo consumeRecordPo : consumeRecordPos) {
			consumeRecordUnion.setConsumeRecordPo(consumeRecordPo);
			consumeRecordDetailUnioins = consumeRecordDetailUnionMapper
					.getRecordDetailUnionByTradeSerialNumber(consumeRecordPo.getTradeSerialNumber());
			consumeRecordGiftUnions = consumeRecordGiftMapper
					.getRecordGiftUnionByTradeSerialNumber(consumeRecordPo.getTradeSerialNumber());
			consumeRecordUnion.setConsumeRecordDetailUnion(consumeRecordDetailUnioins);
			consumeRecordUnion.setConsumeRecordGiftUnion(consumeRecordGiftUnions);
			consumeRecordUnions.add(CommonConverter.map(consumeRecordUnion, ConsumeRecordUnion.class));
		}
		PageInfo<ConsumeRecordUnion> info = new PageInfo<>(consumeRecordUnions);
		return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), consumeRecordUnions);

	}

	@Override
	public PagedList<ConsumeRecordUnion> changedListPagedConsumeRecords(ConsumeRecordQueryCondition condition) {
		int pageNum = condition.getPageNum();
		int pageSize = condition.getPageSize();
		PageHelper.startPage(pageNum, pageSize);
		ConsumeRecordUnion consumeRecordUnion = new ConsumeRecordUnion();
		List<ConsumeRecordUnion> consumeRecordUnions = new ArrayList<ConsumeRecordUnion>();
		List<ConsumeRecordPo> consumeRecordPos = consumeRecordMapper.changedListConsumeRecordByCondition(condition);
		List<ConsumeRecordDetailUnion> consumeRecordDetailUnioins = new ArrayList<ConsumeRecordDetailUnion>();
		List<ConsumeRecordGiftUnion> consumeRecordGiftUnions = new ArrayList<ConsumeRecordGiftUnion>();
		if (CollectionUtils.isEmpty(consumeRecordPos)) {
			return PagedLists.newPagedList(pageNum, pageSize);
		}
		for (ConsumeRecordPo consumeRecordPo : consumeRecordPos) {
			consumeRecordUnion.setConsumeRecordPo(consumeRecordPo);
			consumeRecordDetailUnioins = consumeRecordDetailUnionMapper
					.getRecordDetailUnionByTradeSerialNumber(consumeRecordPo.getTradeSerialNumber());
			consumeRecordGiftUnions = consumeRecordGiftMapper
					.getRecordGiftUnionByTradeSerialNumber(consumeRecordPo.getTradeSerialNumber());
			consumeRecordUnion.setConsumeRecordDetailUnion(consumeRecordDetailUnioins);
			consumeRecordUnion.setConsumeRecordGiftUnion(consumeRecordGiftUnions);
			consumeRecordUnions.add(CommonConverter.map(consumeRecordUnion, ConsumeRecordUnion.class));
		}
		PageInfo<ConsumeRecordUnion> info = new PageInfo<>(consumeRecordUnions);
		return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), consumeRecordUnions);

	}

	@Override
	public void modify(ConsumeRecordPo consumeRecord, List<ConsumeRecordDetailPo> consumeRecordProducts,
			List<ConsumeRecordGiftPo> gifts, Long id, MemberCardPo memberCardPo) {
		consumeRecordMapper.modify(id);
		Map<String, Object> map = new HashMap<String, Object>();
		if (consumeRecord.getConsumeType() == 1) {
			map = getTradeSerialNumber("C");
			map.replace("tradeSerialNumber", consumeRecord.getTradeSerialNumber());
			createCardConsumeRecord(map, consumeRecord, consumeRecordProducts, gifts, memberCardPo);
		} else if (consumeRecord.getConsumeType() == 2) {
			map = getTradeSerialNumber("B");
			map.replace("tradeSerialNumber", consumeRecord.getTradeSerialNumber());
			createProductConsumeRecord(map, consumeRecord, consumeRecordProducts);
		} else if (consumeRecord.getConsumeType() == 3) {
			map = getTradeSerialNumber("D");
			map.replace("tradeSerialNumber", consumeRecord.getTradeSerialNumber());
			createProjectConsumeRecord(map, consumeRecord, consumeRecordProducts);
		} else if (consumeRecord.getConsumeType() == 4) {
			map = getTradeSerialNumber("A");
			map.replace("tradeSerialNumber", consumeRecord.getTradeSerialNumber());
			createActivityConsumeRecord(map, consumeRecord, consumeRecordProducts);
		}
	}

	@Override
	public void createConsumeRecord(ConsumeRecordPo consumeRecord, List<ConsumeRecordDetailPo> consumeRecordDetail,
			List<ConsumeRecordGiftPo> gifts, MemberCardPo memberCardPo) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (consumeRecord.getConsumeType() == 1) {
			map = getTradeSerialNumber("C");
			createCardConsumeRecord(map, consumeRecord, consumeRecordDetail, gifts, memberCardPo);
		} else if (consumeRecord.getConsumeType() == 2) {
			map = getTradeSerialNumber("B");
			createProductConsumeRecord(map, consumeRecord, consumeRecordDetail);
		} else if (consumeRecord.getConsumeType() == 3) {
			map = getTradeSerialNumber("D");
			createProjectConsumeRecord(map, consumeRecord, consumeRecordDetail);
		} else if (consumeRecord.getConsumeType() == 4) {
			map = getTradeSerialNumber("A");
			createActivityConsumeRecord(map, consumeRecord, consumeRecordDetail);
		}
	}

	private void calMoney(ConsumeRecordPo consumeRecord, List<ConsumeRecordDetailPo> consumeRecordDetails) {
		Integer paymentWay = consumeRecord.getPaymentWay();
		Long payWayId = consumeRecord.getPayWayId();
		Long payWayContentId = consumeRecord.getPayWayContentId();

		if (paymentWay == 1) {
			if (consumeRecord.getPayWayContentId() != null) {
				CustomerMemberCardContentUnion cmccu = customerMemberCardContentMapper
						.getContent(consumeRecord.getPayWayContentId());
				CustomerMemberCardPo cmcPo = customerMemberCardMapper.getById(payWayId);
				if (cmccu.getRelatedId() != null) {
					if (consumeRecordDetails.size() == 1
							&& cmccu.getRelatedId() == consumeRecordDetails.get(0).getProjectId()) {
						if (cmccu.getAmount() > consumeRecordDetails.get(0).getAmount().intValue()) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("id", cmccu.getId());
							map.put("amount", cmccu.getAmount() - consumeRecordDetails.get(0).getAmount().intValue());
							customerMemberCardContentMapper.calAmount(map);
							return;
						} else
							throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "会员卡剩余次数不足");
					} else
						throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "选择有误");
				} else {
					if (cmcPo.getRemainingMoney().compareTo(consumeRecord.getConsumeMoney()) >= 0) {
						Map<String, Object> cardMap = new HashMap<String, Object>();
						cardMap.put("id", consumeRecord.getPayWayId());
						cardMap.put("remainMoney", cmcPo.getRemainingMoney().subtract(consumeRecord.getConsumeMoney()));
						return;
					} else
						throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "会员卡余额不足");
				}
			}

		} else if (paymentWay == 2) {


				CustomerActivityContentUnion cacu = customerActivityContentMapper.getById(payWayContentId);
				if(consumeRecordDetails.size() != 1)
					throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "活动选择有误");
				
				if(cacu.getNumber().intValue()>=1){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("id", payWayContentId);
					map.put("amount", cacu.getNumber().subtract(new BigDecimal(1)));
					customerActivityContentMapper.updateAmount(map);
					return;
				}else{
					throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "活动余额不足");
				}
			
			
		} else if (paymentWay == 31) {
			//使用会员卡代金券
			CustomerMemberCardContentUnion cmccu = customerMemberCardContentMapper
					.getContent(consumeRecord.getPayWayContentId());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", cmccu.getId());
			map.put("amount", cmccu.getAmount() - consumeRecordDetails.get(0).getAmount().intValue());
			customerMemberCardContentMapper.calAmount(map);
			return;
		}else if (paymentWay == 32) {
			//使用活动代金券
			CustomerActivityContentUnion cacu = customerActivityContentMapper.getById(payWayContentId);
			if(consumeRecordDetails.size() != 1)
				throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "活动选择有误");
			
			if(cacu.getNumber().intValue()>=1){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", payWayContentId);
				map.put("amount", cacu.getNumber().subtract(new BigDecimal(1)));
				customerActivityContentMapper.updateAmount(map);
				return;
			}else{
				throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "活动余额不足");
			}
		}
	}

	private void calAmount(ConsumeRecordPo consumeRecord, List<ConsumeRecordDetailPo> consumeRecordDetails,
			List<ConsumeRecordGiftPo> gifts) {
		for (ConsumeRecordDetailPo detail : consumeRecordDetails) {
			if (detail.getProductId() != null) {
				ProductFlowPo flowPo = new ProductFlowPo();
				flowPo.setAmount(detail.getAmount());
				flowPo.setProductId(detail.getProductId());
				flowPo.setRecordId(consumeRecord.getId());
				flowPo.setStatus(1);
				flowPo.setStoreId(ThreadContext.getUserStoreId());
				flowPo.setType(3);
			}
			if (detail.getProjectId() != null) {
				List<ProjectStockPo> projectStockPos = projectStockMapper.getProjectStockByProId(detail.getProjectId());
				for (ProjectStockPo projectStockPo : projectStockPos) {
					StockFlowPo stockFlowPo = new StockFlowPo();
					stockFlowPo.setAmount(projectStockPo.getStockConsumptionAmount());
					stockFlowPo.setRecordId(consumeRecord.getId());
					stockFlowPo.setStatus(1);
					stockFlowPo.setStockId(projectStockPo.getStockId());
					stockFlowPo.setStoreId(ThreadContext.getUserStoreId());
					stockFlowPo.setType(3);
					stockService.reduceStockAmount(stockFlowPo);
				}
			}

		}
		for (ConsumeRecordGiftPo gift : gifts) {
			if (gift.getProductId() != null) {
				ProductFlowPo flowPo = new ProductFlowPo();
				flowPo.setAmount(new BigDecimal(gift.getProductAmount()));
				flowPo.setProductId(gift.getProductId());
				flowPo.setRecordId(consumeRecord.getId());
				flowPo.setStatus(1);
				flowPo.setStoreId(ThreadContext.getUserStoreId());
				flowPo.setType(3);
			}
		}

	}

}
