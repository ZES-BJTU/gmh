package com.zes.squad.gmh.service.impl;

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
import com.zes.squad.gmh.entity.po.CustomerMemberCardPo;
import com.zes.squad.gmh.entity.po.ProductAmountPo;
import com.zes.squad.gmh.entity.po.ProductFlowPo;
import com.zes.squad.gmh.entity.po.ProjectStockPo;
import com.zes.squad.gmh.entity.po.StockFlowPo;
import com.zes.squad.gmh.entity.union.ConsumeRecordDetailUnion;
import com.zes.squad.gmh.entity.union.ConsumeRecordGiftUnion;
import com.zes.squad.gmh.entity.union.ConsumeRecordUnion;
import com.zes.squad.gmh.mapper.ActivityContentMapper;
import com.zes.squad.gmh.mapper.ConsumeRecordDetailMapper;
import com.zes.squad.gmh.mapper.ConsumeRecordDetailUnionMapper;
import com.zes.squad.gmh.mapper.ConsumeRecordGiftMapper;
import com.zes.squad.gmh.mapper.ConsumeRecordMapper;
import com.zes.squad.gmh.mapper.CustomerActivityContentMapper;
import com.zes.squad.gmh.mapper.CustomerActivityMapper;
import com.zes.squad.gmh.mapper.CustomerMemberCardMapper;
import com.zes.squad.gmh.mapper.ProductFlowMapper;
import com.zes.squad.gmh.mapper.ProjectStockMapper;
import com.zes.squad.gmh.mapper.StockFlowMapper;
import com.zes.squad.gmh.mapper.TradeSerialNumberMapper;
import com.zes.squad.gmh.service.ConsumeRecordService;
import com.zes.squad.gmh.service.MemberCardService;
import com.zes.squad.gmh.service.ProductService;
import com.zes.squad.gmh.service.ProjectService;
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
	private ProductFlowMapper productFlowMapper;
	@Autowired
	private ConsumeRecordGiftMapper giftMapper;
	@Autowired
	private CustomerMemberCardMapper memberCardMapper;
	@Autowired
	private ProductService productService;
	@Autowired
	private MemberCardService memberCardService;
	@Autowired
	private ProjectService projectSercice;
	@Autowired
	private ActivityContentMapper activityContentMapper;
	@Autowired
	private CustomerActivityContentMapper customerActivityContentMapper;
	@Autowired
	private CustomerActivityMapper customerActivityMapper;
	@Autowired
	private StockFlowMapper stockFlowMapper;
	@Autowired
	private StockService stockService;
	
	
	@SuppressWarnings("null")
	@Override
	public void createProductConsumeRecord(Map<String,Object> map,ConsumeRecordPo consumeRecord,
			List<ConsumeRecordDetailPo> consumeRecordProducts) {
		String tradeSerialNumber = (String) map.get("tradeSerialNumber");
		Integer oldNumber = (Integer)map.get("oldNumber");
		consumeRecord.setTradeSerialNumber(tradeSerialNumber);
		consumeRecord.setStoreId(ThreadContext.getUserStoreId());
		consumeRecord.setIsModified(0);
		
		consumeRecordMapper.insert(consumeRecord);
		tradeSerialNumberMapper.productNumberAdd(oldNumber + 1);
		
		// TODO 根据支付方式扣除会员卡或赠内容
		for (ConsumeRecordDetailPo crpp : consumeRecordProducts) {
			ProductAmountPo productAmountPo = new ProductAmountPo();
			if (crpp.getProductCode() != null) {
				productAmountPo = productService.queryProductAmountByCode(crpp.getProductCode());
				if (productAmountPo == null) {
					crpp.setProductId(productAmountPo.getId());
				}
			}
			crpp.setTradeSerialNumber(tradeSerialNumber);
			consumeRecordDetailMapper.insert(crpp);
			// TODO 产品中扣除相应数量
			ProductFlowPo pfPo = new ProductFlowPo();
			pfPo.setAmount(crpp.getAmount());
			pfPo.setProductId(crpp.getProductId());
			pfPo.setRecordId(consumeRecord.getId());
			pfPo.setStatus(1);
			pfPo.setType(3);
			productFlowMapper.insert(pfPo);
			productService.reduceProductAmount(pfPo);
		}
	}

	public Map<String,Object> getTradeSerialNumber(String type) {
		String tradeSerialNumber = type + ThreadContext.getUserStoreId();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		tradeSerialNumber = tradeSerialNumber + sdf.format(date);
		Integer oldNumber = 0;
		if(type=="A"){
			oldNumber = tradeSerialNumberMapper.getActivityNumber();
		}else if(type=="B"){
			oldNumber = tradeSerialNumberMapper.getProductNumber();
		}else if(type=="C"){
			oldNumber = tradeSerialNumberMapper.getCardNumber();
		}else if(type=="D"){
			oldNumber = tradeSerialNumberMapper.getProjectNumber();
		}		
		Integer tmpNumber = 10000 + oldNumber;
		String number = tmpNumber.toString().substring(1, 5);
		tradeSerialNumber = tradeSerialNumber + number;	
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tradeSerialNumber", tradeSerialNumber);
		map.put("oldNumber", oldNumber);
		return map;
	}

	@SuppressWarnings("null")
	@Override
	public void createCardConsumeRecord(Map<String,Object> map,ConsumeRecordPo consumeRecord,
			List<ConsumeRecordDetailPo> consumeRecordProducts, List<ConsumeRecordGiftPo> gifts) {
		String tradeSerialNumber = (String) map.get("tradeSerialNumber");
		Integer oldNumber = (Integer)map.get("oldNumber");
		consumeRecord.setTradeSerialNumber(tradeSerialNumber);
		consumeRecord.setStoreId(ThreadContext.getUserStoreId());
		consumeRecord.setIsModified(0);
		consumeRecordMapper.insert(consumeRecord);
		tradeSerialNumberMapper.cardNumberAdd(oldNumber + 1);
		CustomerMemberCardPo memberCardPo = new CustomerMemberCardPo();
		for (ConsumeRecordDetailPo crdp : consumeRecordProducts) {
			memberCardPo.setCustomerId(consumeRecord.getCustomerId());
			Long memberCardId = memberCardService.queryMemberCardIdByCode(crdp.getCardCode());
			if (memberCardId != null) {
				memberCardPo.setMemberCardId(memberCardId);
			}
			memberCardPo.setMemberCardId(crdp.getCardId());

			memberCardMapper.insert(memberCardPo);
		}

		for (ConsumeRecordDetailPo crpp : consumeRecordProducts) {
			crpp.setTradeSerialNumber(tradeSerialNumber);
			ProductAmountPo productAmountPo = new ProductAmountPo();
			if (crpp.getProductCode() != null) {
				productAmountPo = productService.queryProductAmountByCode(crpp.getProductCode());
				if (productAmountPo == null) {
					crpp.setProductId(productAmountPo.getId());
				}
			}
			consumeRecordDetailMapper.insert(crpp);
		}
		for (ConsumeRecordGiftPo gift : gifts) {
			gift.setTradeSerialNumber(tradeSerialNumber);
			ProductAmountPo productAmountPo = new ProductAmountPo();
			if (gift.getProductCode() != null) {
				productAmountPo = productService.queryProductAmountByCode(gift.getProductCode());
				if (productAmountPo == null) {
					gift.setProductId(productAmountPo.getId());
				}
			}
			Long projectId = projectSercice.queryProjectByCode(gift.getProductCode());
			if (projectId != null) {
				gift.setProjectId(projectId);
			}
			giftMapper.insert(gift);
		}

	}

	@Override
	public void createProjectConsumeRecord(Map<String,Object> map,ConsumeRecordPo consumeRecord,
			List<ConsumeRecordDetailPo> consumeRecordProducts) {
		String tradeSerialNumber = (String) map.get("tradeSerialNumber");
		Integer oldNumber = (Integer)map.get("oldNumber");
		consumeRecord.setTradeSerialNumber(tradeSerialNumber);
		consumeRecord.setStoreId(ThreadContext.getUserStoreId());
		consumeRecord.setIsModified(0);

		consumeRecordMapper.insert(consumeRecord);
		tradeSerialNumberMapper.projectNumberAdd(oldNumber + 1);
		// TODO 根据支付方式扣除会员卡或赠内容
		for (ConsumeRecordDetailPo crpp : consumeRecordProducts) {
			crpp.setTradeSerialNumber(tradeSerialNumber);
			Long projectId = projectSercice.queryProjectByCode(crpp.getProductCode());
			if (projectId != null) {
				crpp.setProjectId(projectId);
			}
			consumeRecordDetailMapper.insert(crpp);
		}
		for (ConsumeRecordDetailPo crpp : consumeRecordProducts) {
			List<ProjectStockPo> psps = projectStockMapper.getProjectStockByProId(crpp.getProductId());
			for (ProjectStockPo psp : psps) {

				StockFlowPo stockFlowPo = new StockFlowPo();
				stockFlowPo.setAmount(psp.getStockConsumptionAmount());
				stockFlowPo.setStockId(psp.getStockId());
				stockFlowPo.setRecordId(consumeRecord.getId());
				stockFlowPo.setStatus(1);
				stockFlowPo.setType(3);
				stockFlowMapper.insert(stockFlowPo);
				stockService.reduceStockAmount(stockFlowPo);
			}
		}

	}

	@Override
    public void createActivityConsumeRecord(Map<String,Object> map,ConsumeRecordPo consumeRecord,List<ConsumeRecordDetailPo> consumeRecordProducts) {
		String tradeSerialNumber = (String) map.get("tradeSerialNumber");
		Integer oldNumber = (Integer)map.get("oldNumber");
        consumeRecord.setTradeSerialNumber(tradeSerialNumber);
        consumeRecord.setStoreId(ThreadContext.getUserStoreId());
        consumeRecord.setIsModified(0);

        consumeRecordMapper.insert(consumeRecord);
        tradeSerialNumberMapper.activityNumberAdd(oldNumber + 1);
        
        CustomerActivityPo caPo = new CustomerActivityPo();
        caPo.setCustomerId(consumeRecord.getCustomerId());
        caPo.setActivityId(consumeRecord.getActivityId());
        caPo.setStoreId(ThreadContext.getUserStoreId());
        customerActivityMapper.insert(caPo);
        List<ActivityContentPo> acList = activityContentMapper.selectByActivityId(caPo.getId());

        for(ActivityContentPo ac : acList){
        	CustomerActivityContentPo cacPo = CommonConverter.map(ac,CustomerActivityContentPo.class);
        	cacPo.setCustomerActivityId(caPo.getId());
        	customerActivityContentMapper.insert(cacPo);
        }
        
        
        
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
	public void modify(ConsumeRecordPo consumeRecord, List<ConsumeRecordDetailPo> consumeRecordProducts,List<ConsumeRecordGiftPo> gifts, Long id) {
		consumeRecordMapper.modify(id);
		Map<String,Object> map = new HashMap<String,Object>();
		if(consumeRecord.getConsumeType()==1){
			map = getTradeSerialNumber("C");
			map.replace("tradeSerialNumber", consumeRecord.getTradeSerialNumber());
			createCardConsumeRecord(map,consumeRecord,consumeRecordProducts,gifts);
		}else if(consumeRecord.getConsumeType()==2){
			map = getTradeSerialNumber("B");
			map.replace("tradeSerialNumber", consumeRecord.getTradeSerialNumber());
			createProductConsumeRecord(map,consumeRecord,consumeRecordProducts);
		}else if(consumeRecord.getConsumeType()==3){
			map = getTradeSerialNumber("D");
			map.replace("tradeSerialNumber", consumeRecord.getTradeSerialNumber());
			createProjectConsumeRecord(map,consumeRecord,consumeRecordProducts);
		}else if(consumeRecord.getConsumeType()==4){
			map = getTradeSerialNumber("A");
			map.replace("tradeSerialNumber", consumeRecord.getTradeSerialNumber());
			createActivityConsumeRecord(map,consumeRecord,consumeRecordProducts);
		}
		
		
	}
}
