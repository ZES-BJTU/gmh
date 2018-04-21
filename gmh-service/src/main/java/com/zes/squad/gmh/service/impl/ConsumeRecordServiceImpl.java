package com.zes.squad.gmh.service.impl;

import static com.zes.squad.gmh.helper.ExcelHelper.generateStringCell;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.enums.PaymentWayEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.ConsumeRecordQueryCondition;
import com.zes.squad.gmh.entity.po.ActivityContentPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordDetailPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordGiftPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.entity.po.ConsumeSaleEmployeePo;
import com.zes.squad.gmh.entity.po.CustomerActivityContentPo;
import com.zes.squad.gmh.entity.po.CustomerActivityPo;
import com.zes.squad.gmh.entity.po.CustomerMemberCardContentPo;
import com.zes.squad.gmh.entity.po.CustomerMemberCardFlowPo;
import com.zes.squad.gmh.entity.po.CustomerMemberCardPo;
import com.zes.squad.gmh.entity.po.CustomerPo;
import com.zes.squad.gmh.entity.po.EmployeePo;
import com.zes.squad.gmh.entity.po.MemberCardPo;
import com.zes.squad.gmh.entity.po.ProductFlowPo;
import com.zes.squad.gmh.entity.po.ProjectStockPo;
import com.zes.squad.gmh.entity.po.StockFlowPo;
import com.zes.squad.gmh.entity.union.ActivityUnion;
import com.zes.squad.gmh.entity.union.ConsumeRecordDetailUnion;
import com.zes.squad.gmh.entity.union.ConsumeRecordGiftUnion;
import com.zes.squad.gmh.entity.union.ConsumeRecordUnion;
import com.zes.squad.gmh.entity.union.ConsumeSaleEmployeeUnion;
import com.zes.squad.gmh.entity.union.CustomerActivityContentUnion;
import com.zes.squad.gmh.entity.union.CustomerMemberCardContentUnion;
import com.zes.squad.gmh.entity.union.CustomerMemberCardUnion;
import com.zes.squad.gmh.entity.union.EmployeeIntegralUnion;
import com.zes.squad.gmh.entity.union.EmployeeSaleMoney;
import com.zes.squad.gmh.entity.union.PrintUnion;
import com.zes.squad.gmh.entity.union.StoreUnion;
import com.zes.squad.gmh.mapper.ActivityContentMapper;
import com.zes.squad.gmh.mapper.ActivityUnionMapper;
import com.zes.squad.gmh.mapper.ConsumeRecordDetailMapper;
import com.zes.squad.gmh.mapper.ConsumeRecordDetailUnionMapper;
import com.zes.squad.gmh.mapper.ConsumeRecordGiftMapper;
import com.zes.squad.gmh.mapper.ConsumeRecordMapper;
import com.zes.squad.gmh.mapper.ConsumeSaleEmployeeMapper;
import com.zes.squad.gmh.mapper.CustomerActivityContentMapper;
import com.zes.squad.gmh.mapper.CustomerActivityMapper;
import com.zes.squad.gmh.mapper.CustomerMapper;
import com.zes.squad.gmh.mapper.CustomerMemberCardContentMapper;
import com.zes.squad.gmh.mapper.CustomerMemberCardFlowMapper;
import com.zes.squad.gmh.mapper.CustomerMemberCardMapper;
import com.zes.squad.gmh.mapper.EmployeeMapper;
import com.zes.squad.gmh.mapper.MemberCardMapper;
import com.zes.squad.gmh.mapper.ProductFlowMapper;
import com.zes.squad.gmh.mapper.ProductMapper;
import com.zes.squad.gmh.mapper.ProjectMapper;
import com.zes.squad.gmh.mapper.ProjectStockMapper;
import com.zes.squad.gmh.mapper.StockFlowMapper;
import com.zes.squad.gmh.mapper.TradeSerialNumberMapper;
import com.zes.squad.gmh.service.ConsumeRecordService;
import com.zes.squad.gmh.service.ProductService;
import com.zes.squad.gmh.service.StockService;
import com.zes.squad.gmh.service.StoreService;

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
	private ProductFlowMapper productFlowMapper;
	@Autowired
	private StockFlowMapper stockFlowMapper;
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
	private ProductService productService;
	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private StoreService storeService;
	@Autowired
	private EmployeeMapper employeeMapper;
	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private ActivityUnionMapper activityUnionMapper;
	@Autowired
	private CustomerMemberCardFlowMapper customerMemberCardFlowMapper;
	@Autowired
	private MemberCardMapper memberCardMapper;
	@Autowired
	private ConsumeSaleEmployeeMapper consumeSaleEmployeeMapper;

	@Override
	public void createProductConsumeRecord(Map<String, Object> map, ConsumeRecordPo consumeRecord,
			List<ConsumeRecordDetailPo> consumeRecordDetails, List<ConsumeSaleEmployeePo> consumeSaleEmployees) {
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
		saveSaleEmployee(consumeRecord.getId(), consumeSaleEmployees);
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
			MemberCardPo memberCardPo, List<ConsumeSaleEmployeePo> consumeSaleEmployees) {
		String tradeSerialNumber = (String) map.get("tradeSerialNumber");
		Integer oldNumber = (Integer) map.get("oldNumber");
		consumeRecord.setTradeSerialNumber(tradeSerialNumber);
		consumeRecord.setStoreId(ThreadContext.getUserStoreId());
		consumeRecord.setIsModified(0);
		consumeRecord.setConsumeTime(new Date());
		CustomerMemberCardPo customerMemberCardPo = new CustomerMemberCardPo();
		CustomerPo customerPo = customerMapper.getByMobile(consumeRecord.getCustomerMobile());
		if (customerPo == null)
			throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "请先建立客户信息");
		if (consumeRecordDetails.size() > 1) {
			throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "每次只能购买一张卡");
		}
		customerMemberCardPo.setCustomerId(customerPo.getId());
		customerMemberCardPo.setIsReturned(0);
		customerMemberCardPo.setIsTurned(0);
		customerMemberCardPo.setIsValid(1);
		customerMemberCardPo.setStoreId(ThreadContext.getUserStoreId());
		customerMemberCardPo.setUniqueIdentifier(tradeSerialNumber);
		customerMemberCardPo.setMemberCardId(memberCardPo.getId());
		customerMemberCardPo.setProductDiscount(memberCardPo.getProductDiscount().divide(new BigDecimal(100)));
		customerMemberCardPo.setProjectDiscount(memberCardPo.getProjectDiscount().divide(new BigDecimal(100)));
		customerMemberCardPo.setRemainingMoney(memberCardPo.getAmount());
		customerMemberCardPo.setValidDate(consumeRecordDetails.get(0).getValidDate());
		consumeRecord.setCustomerId(customerPo.getId());
		if (consumeRecord.getPaymentWay() == 4)
			consumeRecord.setConsumeMoney(new BigDecimal(0));
		consumeRecordMapper.insert(consumeRecord);
		tradeSerialNumberMapper.cardNumberAdd(oldNumber + 1);
		customerMemberCardMapper.insert(customerMemberCardPo);

		ConsumeRecordDetailPo consumeRecordDetailPo = consumeRecordDetails.get(0);
		consumeRecordDetailPo.setConsumeRecordId(consumeRecord.getId());
		consumeRecordDetailMapper.insert(consumeRecordDetailPo);

		CustomerMemberCardContentPo customerMemberCardContentPo = new CustomerMemberCardContentPo();
		customerMemberCardContentPo.setCustomerMemberCardId(customerMemberCardPo.getId());
		if (memberCardPo.getType() != 2) {

			customerMemberCardContentPo.setRelatedId(memberCardPo.getProjectId());
			customerMemberCardContentPo.setAmount(memberCardPo.getTimes());
			customerMemberCardContentMapper.insert(customerMemberCardContentPo);
		}

		for (ConsumeRecordGiftPo gift : gifts) {
			gift.setConsumeRecordId(consumeRecord.getId());
			consumeRecordGiftMapper.insert(gift);
			if (gift.getProjectId() != null) {
				CustomerMemberCardContentPo tmpcustomerMemberCardContentPo = new CustomerMemberCardContentPo();
				tmpcustomerMemberCardContentPo.setCustomerMemberCardId(customerMemberCardPo.getId());
				tmpcustomerMemberCardContentPo.setRelatedId(gift.getProjectId());
				tmpcustomerMemberCardContentPo.setAmount(gift.getProjectAmount());
				customerMemberCardContentMapper.insert(tmpcustomerMemberCardContentPo);
			}
			if (gift.getCouponMoney() != null) {
				CustomerMemberCardContentPo tmpCustomerMemberCardContentPo = new CustomerMemberCardContentPo();
				tmpCustomerMemberCardContentPo.setCustomerMemberCardId(customerMemberCardPo.getId());
				tmpCustomerMemberCardContentPo.setContent(gift.getCouponMoney());
				tmpCustomerMemberCardContentPo.setAmount(gift.getCouponAmount());
				customerMemberCardContentMapper.insert(tmpCustomerMemberCardContentPo);
			}
		}
		calAmount(consumeRecord, consumeRecordDetails, gifts);
		calMoney(consumeRecord, consumeRecordDetails);
		saveSaleEmployee(consumeRecord.getId(), consumeSaleEmployees);
	}

	@Override
	public void createProjectConsumeRecord(Map<String, Object> map, ConsumeRecordPo consumeRecord,
			List<ConsumeRecordDetailPo> consumeRecordDetails, List<ConsumeSaleEmployeePo> consumeSaleEmployees) {
		String tradeSerialNumber = (String) map.get("tradeSerialNumber");
		Integer oldNumber = (Integer) map.get("oldNumber");
		consumeRecord.setTradeSerialNumber(tradeSerialNumber);
		consumeRecord.setStoreId(ThreadContext.getUserStoreId());
		consumeRecord.setIsModified(0);
		consumeRecord.setConsumeTime(new Date());
		CustomerPo customerPo = customerMapper.getByMobile(consumeRecord.getCustomerMobile());
		consumeRecord.setCustomerId(customerPo.getId());
		if (consumeRecord.getPaymentWay() == 4)
			consumeRecord.setConsumeMoney(new BigDecimal(0));
		consumeRecordMapper.insert(consumeRecord);
		tradeSerialNumberMapper.projectNumberAdd(oldNumber + 1);

		for (ConsumeRecordDetailPo detail : consumeRecordDetails) {
			detail.setConsumeRecordId(consumeRecord.getId());
			consumeRecordDetailMapper.insert(detail);
		}
		calAmount(consumeRecord, consumeRecordDetails, new ArrayList<ConsumeRecordGiftPo>());
		calMoney(consumeRecord, consumeRecordDetails);
		saveSaleEmployee(consumeRecord.getId(), consumeSaleEmployees);
	}

	@Override
	public void createActivityConsumeRecord(Map<String, Object> map, ConsumeRecordPo consumeRecord,
			List<ConsumeRecordDetailPo> consumeRecordDetails, List<ConsumeSaleEmployeePo> consumeSaleEmployees) {
		String tradeSerialNumber = (String) map.get("tradeSerialNumber");
		Integer oldNumber = (Integer) map.get("oldNumber");
		consumeRecord.setTradeSerialNumber(tradeSerialNumber);
		consumeRecord.setStoreId(ThreadContext.getUserStoreId());
		consumeRecord.setIsModified(0);
		consumeRecord.setConsumeTime(new Date());
		CustomerPo customerPo = customerMapper.getByMobile(consumeRecord.getCustomerMobile());
		consumeRecord.setCustomerId(customerPo.getId());
		if (consumeRecord.getPaymentWay() == 4)
			consumeRecord.setConsumeMoney(new BigDecimal(0));
		consumeRecordMapper.insert(consumeRecord);
		tradeSerialNumberMapper.activityNumberAdd(oldNumber + 1);

		CustomerActivityPo caPo = new CustomerActivityPo();
		caPo.setCustomerId(customerPo.getId());
		caPo.setActivityId(consumeRecord.getActivityId());
		caPo.setStoreId(ThreadContext.getUserStoreId());
		customerActivityMapper.insert(caPo);
		List<ActivityContentPo> acList = activityContentMapper.selectByActivityId(consumeRecord.getActivityId());

		for (ActivityContentPo ac : acList) {
			if (ac.getType() == 1 || ac.getType() == 4) { // 项目或代金券
				CustomerActivityContentPo cacPo = CommonConverter.map(ac, CustomerActivityContentPo.class);
				cacPo.setCustomerActivityId(caPo.getId());
				customerActivityContentMapper.insert(cacPo);
			}
			if (ac.getType() == 2) {// 如果类型是会员卡，则建立会员卡信息
				MemberCardPo memberCardPo = memberCardMapper.selectById(ac.getRelatedId());
				CustomerMemberCardPo customerMemberCardPo = new CustomerMemberCardPo();

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
				customerMemberCardMapper.insert(customerMemberCardPo);

				CustomerMemberCardContentPo customerMemberCardContentPo = new CustomerMemberCardContentPo();
				customerMemberCardContentPo.setCustomerMemberCardId(customerMemberCardPo.getId());
				if (memberCardPo.getType() != 2) {
					customerMemberCardContentPo.setRelatedId(memberCardPo.getProjectId());
					customerMemberCardContentPo.setAmount(memberCardPo.getTimes());
					customerMemberCardContentMapper.insert(customerMemberCardContentPo);
				}

			}

		}
		calAmount(consumeRecord, consumeRecordDetails, new ArrayList<ConsumeRecordGiftPo>());
		calMoney(consumeRecord, consumeRecordDetails);
		saveSaleEmployee(consumeRecord.getId(), consumeSaleEmployees);
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
		List<ConsumeSaleEmployeeUnion> consumeSaleEmployees = new ArrayList<ConsumeSaleEmployeeUnion>();
		if (CollectionUtils.isEmpty(consumeRecordPos)) {
			return PagedLists.newPagedList(pageNum, pageSize);
		}
		for (ConsumeRecordPo consumeRecordPo : consumeRecordPos) {
			consumeRecordUnion.setConsumeRecordPo(consumeRecordPo);
			consumeRecordDetailUnioins = consumeRecordDetailUnionMapper
					.getRecordDetailUnionByConsumeRecordId(consumeRecordPo.getId());
			consumeRecordGiftUnions = consumeRecordGiftMapper
					.getRecordGiftUnionByConsumeRecordId(consumeRecordPo.getId());
			consumeSaleEmployees = consumeSaleEmployeeMapper.getUnionByrecordId(consumeRecordPo.getId());
			consumeRecordUnion.setConsumeRecordDetailUnion(consumeRecordDetailUnioins);
			consumeRecordUnion.setConsumeRecordGiftUnion(consumeRecordGiftUnions);
			consumeRecordUnion.setConsumeSaleEmployees(consumeSaleEmployees);
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
					.getRecordDetailUnionByConsumeRecordId(consumeRecordPo.getId());
			consumeRecordGiftUnions = consumeRecordGiftMapper
					.getRecordGiftUnionByConsumeRecordId(consumeRecordPo.getId());
			consumeRecordUnion.setConsumeRecordDetailUnion(consumeRecordDetailUnioins);
			consumeRecordUnion.setConsumeRecordGiftUnion(consumeRecordGiftUnions);
			consumeRecordUnions.add(CommonConverter.map(consumeRecordUnion, ConsumeRecordUnion.class));
		}
		PageInfo<ConsumeRecordUnion> info = new PageInfo<>(consumeRecordUnions);
		return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), consumeRecordUnions);

	}

	@Transactional(rollbackFor = { Throwable.class })
	@Override
	public void modify(ConsumeRecordPo consumeRecord, List<ConsumeRecordDetailPo> consumeRecordDetails,
			List<ConsumeRecordGiftPo> gifts, Long id, MemberCardPo memberCardPo,
			List<ConsumeSaleEmployeePo> consumeSaleEmployees) {
		consumeRecordMapper.modify(id);
		ConsumeRecordPo tmp = consumeRecordMapper.getById(id);
		Map<String, Object> map = new HashMap<String, Object>();
		if (consumeRecord.getConsumeType() == 1) {
			map = getTradeSerialNumber("C");
			map.replace("tradeSerialNumber", tmp.getTradeSerialNumber());
			customerMemberCardMapper.setInvalid(tmp.getTradeSerialNumber());
			createCardConsumeRecord(map, consumeRecord, consumeRecordDetails, gifts, memberCardPo,
					consumeSaleEmployees);
		} else if (consumeRecord.getConsumeType() == 2) {
			map = getTradeSerialNumber("B");
			map.replace("tradeSerialNumber", tmp.getTradeSerialNumber());
			createProductConsumeRecord(map, consumeRecord, consumeRecordDetails, consumeSaleEmployees);
		} else if (consumeRecord.getConsumeType() == 3) {
			map = getTradeSerialNumber("D");
			map.replace("tradeSerialNumber", tmp.getTradeSerialNumber());
			createProjectConsumeRecord(map, consumeRecord, consumeRecordDetails, consumeSaleEmployees);
		} else if (consumeRecord.getConsumeType() == 4) {
			map = getTradeSerialNumber("A");
			map.replace("tradeSerialNumber", tmp.getTradeSerialNumber());
			customerActivityMapper.setInvalid(tmp.getId());
			createActivityConsumeRecord(map, consumeRecord, consumeRecordDetails, consumeSaleEmployees);
		}

		recoverAmount(id);
		recoverMoney(consumeRecord, consumeRecordDetails);
	}

	@Transactional(rollbackFor = { Throwable.class })
	@Override
	public void createConsumeRecord(ConsumeRecordPo consumeRecord, List<ConsumeRecordDetailPo> consumeRecordDetail,
			List<ConsumeRecordGiftPo> gifts, MemberCardPo memberCardPo,
			List<ConsumeSaleEmployeePo> consumeSaleEmployees) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (consumeRecord.getConsumeType() == 1) {
			map = getTradeSerialNumber("C");
			createCardConsumeRecord(map, consumeRecord, consumeRecordDetail, gifts, memberCardPo, consumeSaleEmployees);
		} else if (consumeRecord.getConsumeType() == 2) {
			map = getTradeSerialNumber("B");
			createProductConsumeRecord(map, consumeRecord, consumeRecordDetail, consumeSaleEmployees);
		} else if (consumeRecord.getConsumeType() == 3) {
			map = getTradeSerialNumber("D");
			createProjectConsumeRecord(map, consumeRecord, consumeRecordDetail, consumeSaleEmployees);
		} else if (consumeRecord.getConsumeType() == 4) {
			map = getTradeSerialNumber("A");
			createActivityConsumeRecord(map, consumeRecord, consumeRecordDetail, consumeSaleEmployees);
		}
	}

	public void saveSaleEmployee(Long consumeRecordId, List<ConsumeSaleEmployeePo> consumeSaleEmployees) {
		
		if(consumeSaleEmployees==null){
			return;
		}
		for (ConsumeSaleEmployeePo po : consumeSaleEmployees) {
			po.setConsumeRecordId(consumeRecordId);
			po.setPercent(po.getPercent().divide(new BigDecimal(100)));
			consumeSaleEmployeeMapper.insert(po);
		}
	}

	private void calMoney(ConsumeRecordPo consumeRecord, List<ConsumeRecordDetailPo> consumeRecordDetails) {
		Integer paymentWay = consumeRecord.getPaymentWay();
		Long payWayId = consumeRecord.getPayWayId();
		Long payWayContentId = consumeRecord.getPayWayContentId();
		Integer consumeType = consumeRecord.getConsumeType();
		CustomerMemberCardPo cmcPo = customerMemberCardMapper.getById(payWayId);
		if (paymentWay == 1) {
			CustomerMemberCardFlowPo cardFlowPo = new CustomerMemberCardFlowPo();
			cardFlowPo.setConsumeRecordId(consumeRecord.getId());
			if(cmcPo.getIsValid()==0){
				throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED,
						"此会员卡已失效");
			}
			if(cmcPo.getValidDate().before(new Date())){
				throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED,
						"此会员卡已过期");
			}
			if (consumeType == 2) {// 买产品
				if (cmcPo.getRemainingMoney() == null) {
					throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED,
							"次会员卡没有储值功能，不支持购买产品");
				}
				if (cmcPo.getRemainingMoney().compareTo(consumeRecord.getConsumeMoney()) >= 0) {
					Map<String, Object> cardMap = new HashMap<String, Object>();
					cardMap.put("id", consumeRecord.getPayWayId());
					cardMap.put("remainMoney", cmcPo.getRemainingMoney().subtract(consumeRecord.getConsumeMoney()));
					customerMemberCardMapper.calRemainMoney(cardMap);
					cardFlowPo.setCustomerMemberCardId(payWayId);
					cardFlowPo.setMoney(consumeRecord.getConsumeMoney());
					customerMemberCardFlowMapper.insert(cardFlowPo);
					return;
				} else {
					throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "会员卡余额不足");
				}
			} else if (consumeType == 3) { // 做项目

				// 先扣余额，如果不足则返回余额不足消息
				if (consumeRecord.getConsumeMoney() != null) {
					if (cmcPo.getRemainingMoney() == null) {
						throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED,
								"次会员卡没有储值功能，不支持购买产品");
					}
					if (cmcPo.getRemainingMoney().compareTo(consumeRecord.getConsumeMoney()) >= 0) {
						Map<String, Object> cardMap = new HashMap<String, Object>();
						cardMap.put("id", consumeRecord.getPayWayId());
						cardMap.put("remainMoney", cmcPo.getRemainingMoney().subtract(consumeRecord.getConsumeMoney()));
						customerMemberCardMapper.calRemainMoney(cardMap);
						cardFlowPo.setCustomerMemberCardId(payWayId);
						cardFlowPo.setMoney(consumeRecord.getConsumeMoney());
						customerMemberCardFlowMapper.insert(cardFlowPo);
					} else {
						throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "会员卡余额不足");
					}
				}

				// 扣除项目
				for (ConsumeRecordDetailPo detail : consumeRecordDetails) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("customerMemberCardId", payWayId);
					map.put("relatedId", detail.getProjectId());
					CustomerMemberCardContentPo contentPo = customerMemberCardContentMapper
							.getByCustomerMemberCardIdRelatedId(map);
					if (contentPo != null) {
						if (contentPo.getAmount() >= detail.getAmount().intValue()) {
							Map<String, Object> contentMap = new HashMap<String, Object>();
							contentMap.put("id", contentPo.getId());
							contentMap.put("amount", contentPo.getAmount() - detail.getAmount().intValue());
							customerMemberCardContentMapper.calAmount(contentMap);
							cardFlowPo.setCustomerMemberCardContentId(contentPo.getId());
							cardFlowPo.setAmount(detail.getAmount().intValue());
							customerMemberCardFlowMapper.insert(cardFlowPo);
						} else {
							Map<String, Object> contentMap = new HashMap<String, Object>();
							contentMap.put("id", contentPo.getId());
							contentMap.put("amount", 0);
							customerMemberCardContentMapper.calAmount(contentMap);
							cardFlowPo.setCustomerMemberCardContentId(contentPo.getId());
							cardFlowPo.setAmount(detail.getAmount().intValue());
							customerMemberCardFlowMapper.insert(cardFlowPo);
						}
					}
				}

			} else {
				throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "支付方式选择有误");
			}
		} else if (paymentWay == 2) {
			if (consumeType != 3) {
				throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "支付方式选择有误");
			}
			// 判断活动剩余中知否有这些项目并且剩余数量充足
			for (ConsumeRecordDetailPo detail : consumeRecordDetails) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("customerActivityId", payWayId);
				map.put("relatedId", detail.getProjectId());
				CustomerActivityContentPo contentPo = customerActivityContentMapper
						.getByActivityContentIdRelatedId(map);
				if (contentPo == null)
					throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "活动剩余中无此项目");
				if (contentPo.getNumber().intValue() < detail.getAmount().intValue()) {
					throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "活动剩余不足");
				}
			}
			// 如果剩余数量足够，则扣除数量
			for (ConsumeRecordDetailPo detail : consumeRecordDetails) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("customerActivityId", payWayId);
				map.put("relatedId", detail.getProjectId());
				CustomerActivityContentPo contentPo = customerActivityContentMapper
						.getByActivityContentIdRelatedId(map);
				Map<String, Object> activityMap = new HashMap<String, Object>();
				activityMap.put("id", contentPo.getId());
				activityMap.put("amount", contentPo.getNumber().subtract(detail.getAmount()));
				customerActivityContentMapper.updateAmount(activityMap);
			}

		} else if (paymentWay == 31) {
			CustomerMemberCardFlowPo cardFlowPo = new CustomerMemberCardFlowPo();
			cardFlowPo.setConsumeRecordId(consumeRecord.getId());
			// 使用会员卡代金券
			CustomerMemberCardContentUnion cmccu = customerMemberCardContentMapper
					.getContent(consumeRecord.getPayWayContentId());

			if (cmccu.getAmount() >= consumeRecord.getCouponAmount()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", cmccu.getId());
				map.put("amount", cmccu.getAmount() - consumeRecord.getCouponAmount());
				customerMemberCardContentMapper.calAmount(map);
				cardFlowPo.setCustomerMemberCardContentId(consumeRecord.getPayWayContentId());
				cardFlowPo.setAmount(consumeRecord.getCouponAmount());
				customerMemberCardFlowMapper.insert(cardFlowPo);
				return;
			} else {
				throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "会员卡余额不足");
			}

		} else if (paymentWay == 32) {
			// 使用活动代金券
			CustomerActivityContentUnion cacu = customerActivityContentMapper.getById(payWayContentId);

			if (cacu.getNumber().intValue() >= consumeRecord.getCouponAmount()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", payWayContentId);
				map.put("amount", cacu.getNumber().subtract(new BigDecimal(consumeRecord.getCouponAmount())));
				customerActivityContentMapper.updateAmount(map);
				return;
			} else {
				throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "活动余额不足");
			}
		}
	}

	private void calAmount(ConsumeRecordPo consumeRecord, List<ConsumeRecordDetailPo> consumeRecordDetails,
			List<ConsumeRecordGiftPo> gifts) {
		if (consumeRecordDetails != null) {
			for (ConsumeRecordDetailPo detail : consumeRecordDetails) {
				if (detail.getProductId() != null) {
					ProductFlowPo flowPo = new ProductFlowPo();
					flowPo.setAmount(detail.getAmount());
					flowPo.setProductId(detail.getProductId());
					flowPo.setRecordId(consumeRecord.getId());
					flowPo.setStatus(1);
					flowPo.setStoreId(ThreadContext.getUserStoreId());
					flowPo.setType(3);
					productService.reduceProductAmount(flowPo);
				}
				if (detail.getProjectId() != null) {
					List<ProjectStockPo> projectStockPos = projectStockMapper
							.getProjectStockByProId(detail.getProjectId());
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
		}

		if (gifts != null) {
			for (ConsumeRecordGiftPo gift : gifts) {
				if (gift.getProductId() != null) {
					ProductFlowPo flowPo = new ProductFlowPo();
					flowPo.setAmount(new BigDecimal(gift.getProductAmount()));
					flowPo.setProductId(gift.getProductId());
					flowPo.setRecordId(consumeRecord.getId());
					flowPo.setStatus(1);
					flowPo.setStoreId(ThreadContext.getUserStoreId());
					flowPo.setType(3);
					productService.reduceProductAmount(flowPo);
				}
			}
		}

	}

	private void recoverMoney(ConsumeRecordPo consumeRecord, List<ConsumeRecordDetailPo> consumeRecordDetails) {
		Integer paymentWay = consumeRecord.getPaymentWay();
		Long payWayId = consumeRecord.getPayWayId();
		Long payWayContentId = consumeRecord.getPayWayContentId();
		// 会员卡支付
		if (paymentWay == 1) {
			List<CustomerMemberCardFlowPo> cardFlowPoList = customerMemberCardFlowMapper
					.getListByConsumeRecordId(consumeRecord.getId());
			for (CustomerMemberCardFlowPo cardFlowPo : cardFlowPoList) {
				CustomerMemberCardContentUnion union = customerMemberCardContentMapper
						.getContent(cardFlowPo.getCustomerMemberCardContentId());
				if (cardFlowPo.getAmount() != null) {// 消费时为扣除次数
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", cardFlowPo.getCustomerMemberCardContentId());
					map.put("amount", union.getAmount() + cardFlowPo.getAmount());
					customerMemberCardContentMapper.calAmount(map);
				} else if (cardFlowPo.getMoney() != null) {
					CustomerMemberCardPo cmcPo = customerMemberCardMapper.getById(payWayId);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", cardFlowPo.getCustomerMemberCardId());
					map.put("remainMoney", cmcPo.getRemainingMoney().add(cardFlowPo.getMoney()));
					customerMemberCardMapper.calRemainMoney(map);
				}
			}
		}
		// 活动支付
		if (paymentWay == 2) {
			CustomerActivityContentUnion cacu = customerActivityContentMapper.getById(payWayContentId);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", payWayContentId);
			map.put("amount", cacu.getNumber().intValue() + consumeRecord.getCouponAmount());
			customerActivityContentMapper.updateAmount(map);
		}
		// 会员卡代金券+现金
		if (paymentWay == 31) {
			CustomerMemberCardContentUnion union = customerMemberCardContentMapper.getContent(payWayContentId);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", payWayContentId);
			map.put("amount", union.getAmount() + consumeRecord.getCouponAmount());
			customerMemberCardContentMapper.calAmount(map);
		}
		// 活动代金券+现金
		if (paymentWay == 2) {
			CustomerActivityContentUnion cacu = customerActivityContentMapper.getById(payWayContentId);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", payWayContentId);
			map.put("amount", cacu.getNumber().intValue() + consumeRecord.getCouponAmount());
			customerActivityContentMapper.updateAmount(map);
		}
	}

	private void recoverAmount(Long consumeRecordId) {
		Long storeId = ThreadContext.getUserStoreId();
		List<StockFlowPo> stockFlowList = stockFlowMapper.selectByRecordId(consumeRecordId, storeId);
		List<ProductFlowPo> productFlowList = productFlowMapper.selectByRecordId(consumeRecordId, storeId);
		if (stockFlowList.size() != 0)
			stockService.modifyFlowInvalid(consumeRecordId);
		if (productFlowList.size() != 0)
			productService.modifyFlowInvalid(consumeRecordId);

	}

	@Override
	public PrintUnion getPrint(Long consumeRecordId) {
		PrintUnion printUnion = new PrintUnion();
		ConsumeRecordPo consumeRecordPo = new ConsumeRecordPo();
		StoreUnion storeUnion = new StoreUnion();
		List<ConsumeRecordDetailUnion> consumeRecordDetailUnions = new ArrayList<ConsumeRecordDetailUnion>();
		List<ConsumeRecordGiftUnion> consumeRecordGiftUnions = new ArrayList<ConsumeRecordGiftUnion>();

		consumeRecordPo = consumeRecordMapper.getById(consumeRecordId);
		storeUnion = storeService.queryStoreDetail(ThreadContext.getUserStoreId());
		consumeRecordDetailUnions = consumeRecordDetailUnionMapper
				.getRecordDetailUnionByConsumeRecordId(consumeRecordId);
		consumeRecordGiftUnions = consumeRecordGiftMapper.getRecordGiftUnionByConsumeRecordId(consumeRecordId);
		printUnion.setConsumeRecordDetailUnion(consumeRecordDetailUnions);
		printUnion.setConsumeRecordGiftUnion(consumeRecordGiftUnions);
		printUnion.setConsumeRecordPo(consumeRecordPo);
		printUnion.setStorePo(storeUnion.getStorePo());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("storeId", consumeRecordPo.getStoreId());
		map.put("customerId", consumeRecordPo.getCustomerId());
		List<CustomerMemberCardUnion> customerMemberCardUnions = customerMemberCardMapper
				.listCustomerMemberCardByCustomerId(map);
		for (CustomerMemberCardUnion cmcu : customerMemberCardUnions) {
			cmcu.setCustomerMemberCardContent(customerMemberCardContentMapper.getContentList(cmcu.getId()));
		}
		printUnion.setCustomerMemberCardUnions(customerMemberCardUnions);
		return printUnion;
	}

	@Override
	public Workbook exportConsumeRecord(Integer consumeType, Date beginTime, Date endTime) {
		ConsumeRecordQueryCondition condition = new ConsumeRecordQueryCondition();
		condition.setBeginTime(beginTime);
		condition.setEndTime(endTime);
		condition.setConsumeType(consumeType);
		condition.setStoreId(ThreadContext.getUserStoreId());

		ConsumeRecordUnion consumeRecordUnion = new ConsumeRecordUnion();
		List<ConsumeRecordUnion> consumeRecordUnions = new ArrayList<ConsumeRecordUnion>();
		List<ConsumeRecordPo> consumeRecordPos = consumeRecordMapper.listConsumeRecordByCondition(condition);
		List<ConsumeRecordDetailUnion> consumeRecordDetailUnioins = new ArrayList<ConsumeRecordDetailUnion>();
		List<ConsumeRecordGiftUnion> consumeRecordGiftUnions = new ArrayList<ConsumeRecordGiftUnion>();
		for (ConsumeRecordPo consumeRecordPo : consumeRecordPos) {
			consumeRecordUnion.setConsumeRecordPo(consumeRecordPo);
			consumeRecordDetailUnioins = consumeRecordDetailUnionMapper
					.getRecordDetailUnionByConsumeRecordId(consumeRecordPo.getId());
			consumeRecordGiftUnions = consumeRecordGiftMapper
					.getRecordGiftUnionByConsumeRecordId(consumeRecordPo.getId());
			consumeRecordUnion.setConsumeRecordDetailUnion(consumeRecordDetailUnioins);
			consumeRecordUnion.setConsumeRecordGiftUnion(consumeRecordGiftUnions);
			consumeRecordUnions.add(CommonConverter.map(consumeRecordUnion, ConsumeRecordUnion.class));
		}

		Workbook workbook = new SXSSFWorkbook();
		Sheet sheet = workbook.createSheet("消费记录");
		if (CollectionUtils.isEmpty(consumeRecordUnions)) {
			return workbook;
		}
		if (consumeType == 1) { // 办卡
			buildCardSheetByConsumeRecordUnion(sheet, consumeRecordUnions);
		}
		if (consumeType == 3) { // 做项目
			buildProjectSheetByConsumeRecordUnion(sheet, consumeRecordUnions);
		}
		if (consumeType == 2) { // 买产品
			buildProductSheetByConsumeRecordUnion(sheet, consumeRecordUnions);
		}
		if (consumeType == 4) { // 参加活动
			buildActivitySheetByConsumeRecordUnion(sheet, consumeRecordUnions);
		}

		return workbook;
	}

	private void buildActivitySheetByConsumeRecordUnion(Sheet sheet, List<ConsumeRecordUnion> consumeRecordUnions) {
		int rowNum = 0;
		int columnNum = 0;
		Row row = sheet.createRow(rowNum++);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		generateStringCell(row, columnNum++, "流水号");
		generateStringCell(row, columnNum++, "客户姓名");
		generateStringCell(row, columnNum++, "手机号");
		generateStringCell(row, columnNum++, "消费类型");
		generateStringCell(row, columnNum++, "活动名称");
		generateStringCell(row, columnNum++, "消费金额");
		generateStringCell(row, columnNum++, "消费时间");
		for (ConsumeRecordUnion union : consumeRecordUnions) {
			columnNum = 0;
			row = sheet.createRow(rowNum++);
			// 流水号
			generateStringCell(row, columnNum++, union.getConsumeRecordPo().getTradeSerialNumber());
			// 客户姓名
			generateStringCell(row, columnNum++,
					customerMapper.getById(union.getConsumeRecordPo().getCustomerId()).getName());
			// 手机号
			generateStringCell(row, columnNum++,
					customerMapper.getById(union.getConsumeRecordPo().getCustomerId()).getMobile());
			// 消费类型
			generateStringCell(row, columnNum++, "购买活动");
			// 活动名称
			generateStringCell(row, columnNum++, activityUnionMapper
					.selectById(union.getConsumeRecordPo().getActivityId()).getActivityPo().getName());
			// 金额
			generateStringCell(row, columnNum++, union.getConsumeRecordPo().getConsumeMoney().toString());
			// 时间
			generateStringCell(row, columnNum++, formatter.format(union.getConsumeRecordPo().getConsumeTime()));

		}

	}

	private void buildProductSheetByConsumeRecordUnion(Sheet sheet, List<ConsumeRecordUnion> consumeRecordUnions) {
		int rowNum = 0;
		int columnNum = 0;
		int defaultStockColumnNum = 8;
		Row row = sheet.createRow(rowNum++);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		generateStringCell(row, columnNum++, "流水号");
		generateStringCell(row, columnNum++, "客户姓名");
		generateStringCell(row, columnNum++, "手机号");
		generateStringCell(row, columnNum++, "消费类型");
		generateStringCell(row, columnNum++, "支付方式");
		generateStringCell(row, columnNum++, "支付方式名称");
		generateStringCell(row, columnNum++, "消费金额");
		generateStringCell(row, columnNum++, "消费时间");
		generateStringCell(row, columnNum++, "产品名称");
		generateStringCell(row, columnNum++, "数量");
		for (ConsumeRecordUnion union : consumeRecordUnions) {
			columnNum = 0;
			row = sheet.createRow(rowNum++);
			// 流水号
			generateStringCell(row, columnNum++, union.getConsumeRecordPo().getTradeSerialNumber());
			// 客户姓名
			generateStringCell(row, columnNum++,
					customerMapper.getById(union.getConsumeRecordPo().getCustomerId()).getName());
			// 手机号
			generateStringCell(row, columnNum++,
					customerMapper.getById(union.getConsumeRecordPo().getCustomerId()).getMobile());
			// 消费类型
			generateStringCell(row, columnNum++, "购买产品");
			// 支付方式
			generateStringCell(row, columnNum++,
					EnumUtils.getDescByKey(union.getConsumeRecordPo().getPaymentWay(), PaymentWayEnum.class));
			// 支付方式名称
			if (union.getConsumeRecordPo().getPaymentWay() == 1 || union.getConsumeRecordPo().getPaymentWay() == 31) {
				generateStringCell(row, columnNum++, memberCardMapper.selectById(
						customerMemberCardMapper.getById(union.getConsumeRecordPo().getPayWayId()).getMemberCardId())
						.getName());
			} else if (union.getConsumeRecordPo().getPaymentWay() == 2
					|| union.getConsumeRecordPo().getPaymentWay() == 32) {
				generateStringCell(row, columnNum++, "活动");
			} else {
				generateStringCell(row, columnNum++, "");
			}
			// 金额
			generateStringCell(row, columnNum++, union.getConsumeRecordPo().getConsumeMoney().toString());
			// 时间
			generateStringCell(row, columnNum++, formatter.format(union.getConsumeRecordPo().getConsumeTime()));
			for (int i = 0; i < union.getConsumeRecordDetailUnion().size(); i++) {
				columnNum = defaultStockColumnNum;
				if (i > 0) {
					row = sheet.createRow(rowNum++);
				}
				// 产品名称
				generateStringCell(row, columnNum++, union.getConsumeRecordDetailUnion().get(i).getProductName());
				// 产品数量
				generateStringCell(row, columnNum++, union.getConsumeRecordDetailUnion().get(i).getAmount().toString());
			}

		}

	}

	private void buildProjectSheetByConsumeRecordUnion(Sheet sheet, List<ConsumeRecordUnion> consumeRecordUnions) {
		int rowNum = 0;
		int columnNum = 0;
		int defaultStockColumnNum = 8;
		Row row = sheet.createRow(rowNum++);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		generateStringCell(row, columnNum++, "流水号");
		generateStringCell(row, columnNum++, "客户姓名");
		generateStringCell(row, columnNum++, "手机号");
		generateStringCell(row, columnNum++, "消费类型");
		generateStringCell(row, columnNum++, "支付方式");
		generateStringCell(row, columnNum++, "支付方式名称");
		generateStringCell(row, columnNum++, "消费金额");
		generateStringCell(row, columnNum++, "消费时间");
		generateStringCell(row, columnNum++, "项目名称");
		generateStringCell(row, columnNum++, "数量");
		for (ConsumeRecordUnion union : consumeRecordUnions) {
			columnNum = 0;
			row = sheet.createRow(rowNum++);
			// 流水号
			generateStringCell(row, columnNum++, union.getConsumeRecordPo().getTradeSerialNumber());
			// 客户姓名
			generateStringCell(row, columnNum++,
					customerMapper.getById(union.getConsumeRecordPo().getCustomerId()).getName());
			// 手机号
			generateStringCell(row, columnNum++,
					customerMapper.getById(union.getConsumeRecordPo().getCustomerId()).getMobile());
			// 消费类型
			generateStringCell(row, columnNum++, "做项目");
			// 支付方式
			generateStringCell(row, columnNum++,
					EnumUtils.getDescByKey(union.getConsumeRecordPo().getPaymentWay(), PaymentWayEnum.class));
			// 支付方式名称
			if (union.getConsumeRecordPo().getPaymentWay() == 1 || union.getConsumeRecordPo().getPaymentWay() == 31) {
				generateStringCell(row, columnNum++, memberCardMapper.selectById(
						customerMemberCardMapper.getById(union.getConsumeRecordPo().getPayWayId()).getMemberCardId())
						.getName());
			} else if (union.getConsumeRecordPo().getPaymentWay() == 2
					|| union.getConsumeRecordPo().getPaymentWay() == 32) {
				generateStringCell(row, columnNum++, "活动");
			} else {
				generateStringCell(row, columnNum++, "");
			}
			// 金额
			generateStringCell(row, columnNum++, union.getConsumeRecordPo().getConsumeMoney().toString());
			// 时间
			generateStringCell(row, columnNum++, formatter.format(union.getConsumeRecordPo().getConsumeTime()));
			for (int i = 0; i < union.getConsumeRecordDetailUnion().size(); i++) {
				columnNum = defaultStockColumnNum;
				if (i > 0) {
					row = sheet.createRow(rowNum++);
				}
				// 产品名称
				generateStringCell(row, columnNum++, union.getConsumeRecordDetailUnion().get(i).getProjectName());
				// 产品数量
				generateStringCell(row, columnNum++, union.getConsumeRecordDetailUnion().get(i).getAmount().toString());
			}

		}

	}

	private void buildCardSheetByConsumeRecordUnion(Sheet sheet, List<ConsumeRecordUnion> consumeRecordUnions) {
		int rowNum = 0;
		int columnNum = 0;
		Row row = sheet.createRow(rowNum++);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		generateStringCell(row, columnNum++, "流水号");
		generateStringCell(row, columnNum++, "客户姓名");
		generateStringCell(row, columnNum++, "手机号");
		generateStringCell(row, columnNum++, "消费类型");
		generateStringCell(row, columnNum++, "卡名");
		generateStringCell(row, columnNum++, "消费金额");
		generateStringCell(row, columnNum++, "消费时间");
		for (ConsumeRecordUnion union : consumeRecordUnions) {
			columnNum = 0;
			row = sheet.createRow(rowNum++);
			// 流水号
			generateStringCell(row, columnNum++, union.getConsumeRecordPo().getTradeSerialNumber());
			// 客户姓名
			generateStringCell(row, columnNum++,
					customerMapper.getById(union.getConsumeRecordPo().getCustomerId()).getName());
			// 手机号
			generateStringCell(row, columnNum++,
					customerMapper.getById(union.getConsumeRecordPo().getCustomerId()).getMobile());
			// 消费类型
			generateStringCell(row, columnNum++, "办卡");
			// 卡名
			generateStringCell(row, columnNum++, union.getConsumeRecordDetailUnion().get(0).getCardName());
			// 金额
			generateStringCell(row, columnNum++, union.getConsumeRecordPo().getConsumeMoney().toString());
			// 时间
			generateStringCell(row, columnNum++, formatter.format(union.getConsumeRecordPo().getConsumeTime()));

		}
	}

	@Override
	public Workbook exportEmployeeIntegral(Date beginTime, Date endTime) {

		List<Long> officialEmployeeIds = employeeMapper.getOfficialOperatorId(ThreadContext.getUserStoreId());
		List<Long> internEmployeeIds = employeeMapper.getInternOperatorId(ThreadContext.getUserStoreId());
		List<Long> employeeIds = officialEmployeeIds;
		employeeIds.addAll(internEmployeeIds);
		List<EmployeeIntegralUnion> employeeTotalIntegralUnions = new ArrayList<EmployeeIntegralUnion>();
		for (Long employeeId : employeeIds) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("employeeId", employeeId);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			List<EmployeeIntegralUnion> employeeIntegralUnions = consumeRecordMapper
					.getIntegralEmployeeIntegralByEmployeeId(map);
			EmployeeIntegralUnion employeeTotalIntegralUnion = new EmployeeIntegralUnion();
			BigDecimal totalIntegral = new BigDecimal(0);
			for (EmployeeIntegralUnion union : employeeIntegralUnions) {
				totalIntegral = totalIntegral.add(union.getIntegral());
				employeeTotalIntegralUnion.setEmployeeId(employeeId);
				employeeTotalIntegralUnion.setEmployeeName(union.getEmployeeName());
			}
			if (employeeIntegralUnions.size() == 0) {
				employeeTotalIntegralUnion.setEmployeeId(employeeId);
				employeeTotalIntegralUnion.setEmployeeName(employeeMapper.selectById(employeeId).getName());
			}
			employeeTotalIntegralUnion.setTotalIntegral(totalIntegral);
			employeeTotalIntegralUnions
					.add(CommonConverter.map(employeeTotalIntegralUnion, EmployeeIntegralUnion.class));

		}

		Workbook workbook = new SXSSFWorkbook();
		Sheet sheet = workbook.createSheet("绩效积分");
		if (CollectionUtils.isEmpty(employeeTotalIntegralUnions)) {
			return workbook;
		}
		buildSheetByEmployeeIntegralUnion(sheet, employeeTotalIntegralUnions);

		return workbook;
	}

	private void buildSheetByEmployeeIntegralUnion(Sheet sheet,
			List<EmployeeIntegralUnion> employeeTotalIntegralUnions) {
		int rowNum = 0;
		int columnNum = 0;
		Row row = sheet.createRow(rowNum++);
		generateStringCell(row, columnNum++, "员工姓名");
		generateStringCell(row, columnNum++, "员工积分");
		for (EmployeeIntegralUnion union : employeeTotalIntegralUnions) {
			columnNum = 0;
			row = sheet.createRow(rowNum++);
			// 姓名
			generateStringCell(row, columnNum++, union.getEmployeeName());
			// 积分
			generateStringCell(row, columnNum++, union.getTotalIntegral().toString());

		}

	}

	@Override
	public BigDecimal doCalMoney(ConsumeRecordPo consumeRecord, List<ConsumeRecordDetailPo> consumeRecordDetails,
			List<ConsumeRecordGiftPo> gifts, MemberCardPo memberCardPo) {
		BigDecimal money = new BigDecimal(0);
		Integer paymentWay = consumeRecord.getPaymentWay();
		if (paymentWay == 1) {
			money = doCalCardMoney(consumeRecord, consumeRecordDetails, gifts, memberCardPo);
		} else if (paymentWay == 31 || paymentWay == 32) {
			money = calCouponMoney(consumeRecord, consumeRecordDetails, gifts, memberCardPo);
		} else if (paymentWay == 3) {
			money = doCalCashMoney(consumeRecord, consumeRecordDetails, gifts, memberCardPo);
		} else {
			throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "支付方式选择有误");
		}

		return money;

	}

	public BigDecimal doCalCardMoney(ConsumeRecordPo consumeRecord, List<ConsumeRecordDetailPo> consumeRecordDetails,
			List<ConsumeRecordGiftPo> gifts, MemberCardPo memberCardPo) {
		if (consumeRecord.getPayWayId() == null) {
			throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "请选择会员卡");
		}
		CustomerMemberCardPo customerMemberCardPo = customerMemberCardMapper.getById(consumeRecord.getPayWayId());
		if (consumeRecord.getConsumeType() == 3) {// 做项目
			Long payWayId = consumeRecord.getPayWayId();

			List<ConsumeRecordDetailPo> byMoney = new ArrayList<ConsumeRecordDetailPo>();
			List<ConsumeRecordDetailPo> byTimes = new ArrayList<ConsumeRecordDetailPo>();
			for (int i = 0; i < consumeRecordDetails.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("customerMemberCardId", payWayId);
				map.put("relatedId", consumeRecordDetails.get(i).getProjectId());
				CustomerMemberCardContentPo contentPo = customerMemberCardContentMapper
						.getByCustomerMemberCardIdRelatedId(map);
				if (contentPo == null) {// 说明此会员卡中没有该项目，该项目需要扣储值支付
					byMoney.add(consumeRecordDetails.get(i));
				} else {// 说明此会员卡中有该项目，可以扣次数
					if (contentPo.getAmount() >= consumeRecordDetails.get(i).getAmount().intValue()) {// 会员卡中余量足够
						byTimes.add(consumeRecordDetails.get(i));
					} else {// 会员卡中余量不够
						ConsumeRecordDetailPo tmpByMoney = consumeRecordDetails.get(i);
						tmpByMoney.setAmount(consumeRecordDetails.get(i).getAmount()
								.subtract(new BigDecimal(contentPo.getAmount())));
						byMoney.add(CommonConverter.map(tmpByMoney, ConsumeRecordDetailPo.class));
						ConsumeRecordDetailPo tmpByTimes = consumeRecordDetails.get(i);
						tmpByTimes.setAmount(new BigDecimal(contentPo.getAmount()));
						byTimes.add(CommonConverter.map(tmpByTimes, ConsumeRecordDetailPo.class));

					}
				}
			}
			BigDecimal money = new BigDecimal(0);
			for (ConsumeRecordDetailPo detail : byMoney) {
				money = money.add(
						projectMapper.selectById(detail.getProjectId()).getUnitPrice().multiply(detail.getAmount()));
			}
			if (customerMemberCardPo.getProjectDiscount() != null) {
				if (customerMemberCardPo.getProjectDiscount().compareTo(new BigDecimal(0)) != 0) {
					money = money.multiply(customerMemberCardPo.getProjectDiscount());
				}
			}
			return money;
		} else if (consumeRecord.getConsumeType() == 2) {// 买产品
			BigDecimal money = new BigDecimal(0);
			for (ConsumeRecordDetailPo detail : consumeRecordDetails) {
				money = money.add(
						productMapper.selectById(detail.getProductId()).getUnitPrice().multiply(detail.getAmount()));
			}
			if (customerMemberCardPo.getProductDiscount() != null) {
				if (customerMemberCardPo.getProductDiscount().compareTo(new BigDecimal(0)) == 0) {
					return money;
				} else
					return money.multiply(customerMemberCardPo.getProductDiscount());
			} else
				return money;

		} else {
			throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "会员卡不支持此项购买");
		}
	}

	public BigDecimal calCouponMoney(ConsumeRecordPo consumeRecord, List<ConsumeRecordDetailPo> consumeRecordDetails,
			List<ConsumeRecordGiftPo> gifts, MemberCardPo memberCardPo) {
		Integer consumeType = consumeRecord.getConsumeType();
		Integer paymentWay = consumeRecord.getPaymentWay();
		Long payWayContentId = consumeRecord.getPayWayContentId();
		BigDecimal money = new BigDecimal(0);
		if (payWayContentId == null) {
			throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "请选择代金券");
		}
		// 买产品
		if (consumeType == 2) {
			// 原价总价
			for (ConsumeRecordDetailPo detail : consumeRecordDetails) {
				money = money.add(
						productMapper.selectById(detail.getProductId()).getUnitPrice().multiply(detail.getAmount()));
			}
			if (paymentWay == 31) {// 会员卡代金券+现金------------减去代金券后剩余钱数
				CustomerMemberCardContentUnion customerMemberCardContentUnion = customerMemberCardContentMapper
						.getContent(payWayContentId);
				money = money.subtract(customerMemberCardContentUnion.getContent()
						.multiply(new BigDecimal(consumeRecord.getCouponAmount())));
				return money;
			} else if (paymentWay == 32) {// 活动代金券+现金------------减去代金券后剩余钱数
				CustomerActivityContentUnion customerActivityContentUnion = customerActivityContentMapper
						.getById(payWayContentId);
				money = money.subtract(customerActivityContentUnion.getContent()
						.multiply(new BigDecimal(consumeRecord.getCouponAmount())));
				return money;
			} else {
				throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "不支持使用此支付方式");
			}
		} else if (consumeType == 3) {// 做项目
			// 原价总价
			for (ConsumeRecordDetailPo detail : consumeRecordDetails) {
				money = money.add(
						projectMapper.selectById(detail.getProductId()).getUnitPrice().multiply(detail.getAmount()));
			}
			if (paymentWay == 31) {// 会员卡代金券+现金------------减去代金券后剩余钱数
				CustomerMemberCardContentUnion customerMemberCardContentUnion = customerMemberCardContentMapper
						.getContent(payWayContentId);
				money = money.subtract(customerMemberCardContentUnion.getContent()
						.multiply(new BigDecimal(consumeRecord.getCouponAmount())));
				return money;
			} else if (paymentWay == 32) {// 活动代金券+现金------------减去代金券后剩余钱数
				CustomerActivityContentUnion customerActivityContentUnion = customerActivityContentMapper
						.getById(payWayContentId);
				money = money.subtract(customerActivityContentUnion.getContent()
						.multiply(new BigDecimal(consumeRecord.getCouponAmount())));
				return money;
			} else {
				throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "不支持使用此支付方式");
			}
		} else {
			throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "不支持使用此支付方式");
		}

	}

	public BigDecimal doCalCashMoney(ConsumeRecordPo consumeRecord, List<ConsumeRecordDetailPo> consumeRecordDetails,
			List<ConsumeRecordGiftPo> gifts, MemberCardPo memberCardPo) {
		Integer consumeType = consumeRecord.getConsumeType();
		BigDecimal money = new BigDecimal(0);
		if (consumeType == 1) { // 办卡
			money = memberCardPo.getPrice();
		} else if (consumeType == 2) { // 买产品
			for (ConsumeRecordDetailPo detail : consumeRecordDetails) {
				money = money.add(
						productMapper.selectById(detail.getProductId()).getUnitPrice().multiply(detail.getAmount()));
			}
		} else if (consumeType == 3) { // 做项目
			for (ConsumeRecordDetailPo detail : consumeRecordDetails) {
				money = money.add(
						projectMapper.selectById(detail.getProjectId()).getUnitPrice().multiply(detail.getAmount()));
			}
		} else if (consumeType == 4) { // 买活动
			ActivityUnion activityUnion = activityUnionMapper.selectById(consumeRecord.getActivityId());
			money = activityUnion.getActivityPo().getPrice();
		}
		return money;
	}

	@Override
	public Workbook exportEmployeeSale(Date beginTime, Date endTime) {
		List<EmployeePo> employees = employeeMapper.selectByWorkType(null, ThreadContext.getUserStoreId());

		List<EmployeeSaleMoney> employeeSaleMoneyList = new ArrayList<EmployeeSaleMoney>();
		List<EmployeeSaleMoney> employeeSaleTotalMoneyList = new ArrayList<EmployeeSaleMoney>();
		EmployeeSaleMoney employeeSaleMoney = new EmployeeSaleMoney();
		BigDecimal money = new BigDecimal(0);
		for (EmployeePo employee : employees) {
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("employeeId", employee.getId());
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			employeeSaleMoneyList = consumeSaleEmployeeMapper.getSaleUnionByEmployeeId(map);
			for (EmployeeSaleMoney tmp : employeeSaleMoneyList) {
				money = money.add(tmp.getMoney());
			}
			employeeSaleMoney.setEmployeeId(employee.getId());
			employeeSaleMoney.setTotalMoney(money);
			employeeSaleMoney.setEmployeeName(employee.getName());
			employeeSaleTotalMoneyList.add(CommonConverter.map(employeeSaleMoney, EmployeeSaleMoney.class));
		}
		
		Workbook workbook = new SXSSFWorkbook();
		Sheet sheet = workbook.createSheet("员工业绩");
		if (CollectionUtils.isEmpty(employeeSaleTotalMoneyList)) {
			return workbook;
		}
		buildSheetByEmployeeSale(sheet, employeeSaleTotalMoneyList);
		
		return workbook;
	}

	private void buildSheetByEmployeeSale(Sheet sheet, List<EmployeeSaleMoney> employeeSaleTotalMoneyList) {
		int rowNum = 0;
		int columnNum = 0;
		Row row = sheet.createRow(rowNum++);
		generateStringCell(row, columnNum++, "员工姓名");
		generateStringCell(row, columnNum++, "员工业绩");
		for (EmployeeSaleMoney union : employeeSaleTotalMoneyList) {
			columnNum = 0;
			row = sheet.createRow(rowNum++);
			// 姓名
			generateStringCell(row, columnNum++, union.getEmployeeName());
			// 积分
			generateStringCell(row, columnNum++, union.getTotalMoney().toString());

		}
		
	}

}
