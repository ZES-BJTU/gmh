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
import com.zes.squad.gmh.entity.po.ConsumeRecordDetailPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordGiftPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.entity.po.CustomerMemberCardPo;
import com.zes.squad.gmh.entity.po.ProductAmountPo;
import com.zes.squad.gmh.entity.po.ProjectStockPo;
import com.zes.squad.gmh.entity.po.StockPo;
import com.zes.squad.gmh.entity.union.ConsumeRecordDetailUnion;
import com.zes.squad.gmh.entity.union.ConsumeRecordGiftUnion;
import com.zes.squad.gmh.entity.union.ConsumeRecordUnion;
import com.zes.squad.gmh.mapper.ConsumeRecordDetailMapper;
import com.zes.squad.gmh.mapper.ConsumeRecordDetailUnionMapper;
import com.zes.squad.gmh.mapper.ConsumeRecordGiftMapper;
import com.zes.squad.gmh.mapper.ConsumeRecordMapper;
import com.zes.squad.gmh.mapper.CustomerMemberCardMapper;
import com.zes.squad.gmh.mapper.ProjectStockMapper;
import com.zes.squad.gmh.mapper.StockMapper;
import com.zes.squad.gmh.mapper.TradeSerialNumberMapper;
import com.zes.squad.gmh.service.ConsumeRecordService;
import com.zes.squad.gmh.service.MemberCardService;
import com.zes.squad.gmh.service.ProductService;
import com.zes.squad.gmh.service.ProjectService;

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
	private StockMapper stockMapper;
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
	@SuppressWarnings("null")
	@Override
	public void createProductConsumeRecord(ConsumeRecordPo consumeRecord,
			List<ConsumeRecordDetailPo> consumeRecordProducts) {
		String tradeSerialNumber = "B" + ThreadContext.getUserStoreId();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		tradeSerialNumber = tradeSerialNumber + sdf.format(date);
		Integer oldNumber = tradeSerialNumberMapper.getProductNumber();
		Integer tmpNumber = 10000 + oldNumber;
		String number = tmpNumber.toString().substring(1, 5);
		tradeSerialNumber = tradeSerialNumber + number;
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
				if(productAmountPo==null){
					crpp.setProductId(productAmountPo.getId());
				}
			}
			crpp.setTradeSerialNumber(tradeSerialNumber);
			consumeRecordDetailMapper.insert(crpp);
			// TODO 产品中扣除相应数量
		}
	}

	@SuppressWarnings("null")
	@Override
	public void createCardConsumeRecord(ConsumeRecordPo consumeRecord,
			List<ConsumeRecordDetailPo> consumeRecordProducts, List<ConsumeRecordGiftPo> gifts) {
		String tradeSerialNumber = "C" + ThreadContext.getUserStoreId();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		tradeSerialNumber = tradeSerialNumber + sdf.format(date);
		Integer oldNumber = tradeSerialNumberMapper.getProductNumber();
		Integer tmpNumber = 10000 + oldNumber;
		String number = tmpNumber.toString().substring(1, 5);
		tradeSerialNumber = tradeSerialNumber + number;
		consumeRecord.setTradeSerialNumber(tradeSerialNumber);
		consumeRecord.setStoreId(ThreadContext.getUserStoreId());
		consumeRecord.setIsModified(0);
		consumeRecordMapper.insert(consumeRecord);
		tradeSerialNumberMapper.cardNumberAdd(oldNumber + 1);
		CustomerMemberCardPo memberCardPo = new CustomerMemberCardPo();
		for (ConsumeRecordDetailPo crdp : consumeRecordProducts) {
			memberCardPo.setCustomerId(consumeRecord.getCustomerId());
			Long memberCardId = memberCardService.queryMemberCardIdByCode(crdp.getCardCode());
			if(memberCardId!=null){
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
				if(productAmountPo==null){
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
				if(productAmountPo==null){
					gift.setProductId(productAmountPo.getId());
				}
			}
			Long projectId = projectSercice.queryProjectByCode(gift.getProductCode());
			if(projectId!=null){
				gift.setProjectId(projectId);
			}
			giftMapper.insert(gift);
		}

	}

	@Override
	public void createProjectConsumeRecord(ConsumeRecordPo consumeRecord,
			List<ConsumeRecordDetailPo> consumeRecordProducts) {
		String tradeSerialNumber = "B" + ThreadContext.getUserStoreId();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		tradeSerialNumber = tradeSerialNumber + sdf.format(date);
		Integer oldNumber = tradeSerialNumberMapper.getProductNumber();
		Integer tmpNumber = 10000 + oldNumber;
		String number = tmpNumber.toString().substring(1, 5);
		tradeSerialNumber = tradeSerialNumber + number;
		consumeRecord.setTradeSerialNumber(tradeSerialNumber);
		consumeRecord.setStoreId(ThreadContext.getUserStoreId());
		consumeRecord.setIsModified(0);

		consumeRecordMapper.insert(consumeRecord);
		tradeSerialNumberMapper.productNumberAdd(oldNumber + 1);
		// TODO 根据支付方式扣除会员卡或赠内容
		for (ConsumeRecordDetailPo crpp : consumeRecordProducts) {
			crpp.setTradeSerialNumber(tradeSerialNumber);
			Long projectId = projectSercice.queryProjectByCode(crpp.getProductCode());
			if(projectId!=null){
				crpp.setProjectId(projectId);
			}
			consumeRecordDetailMapper.insert(crpp);
		}
		for (ConsumeRecordDetailPo crpp : consumeRecordProducts) {
			List<ProjectStockPo> psps = projectStockMapper.getProjectStockByProId(crpp.getProductId());
			for (ProjectStockPo psp : psps) {
				StockPo stock = stockMapper.getById(psp.getStockId());
				Map<String, Number> map = new HashMap<String, Number>();
				map.put("id", stock.getId());
				map.put("totalAmount", stock.getTotalAmount().subtract(psp.getStockConsumptionAmount()));
				stockMapper.updateTotalAmount(map);
			}
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

}
