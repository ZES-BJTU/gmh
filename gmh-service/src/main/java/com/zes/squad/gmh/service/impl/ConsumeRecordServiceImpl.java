package com.zes.squad.gmh.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.po.ConsumeRecordGiftPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordDetailPo;
import com.zes.squad.gmh.entity.po.ProjectStockPo;
import com.zes.squad.gmh.entity.po.StockPo;
import com.zes.squad.gmh.mapper.ConsumeRecordGiftMapper;
import com.zes.squad.gmh.mapper.ConsumeRecordMapper;
import com.zes.squad.gmh.mapper.ConsumeRecordDetailMapper;
import com.zes.squad.gmh.mapper.ProjectStockMapper;
import com.zes.squad.gmh.mapper.StockMapper;
import com.zes.squad.gmh.mapper.TradeSerialNumberMapper;
import com.zes.squad.gmh.service.ConsumeRecordService;

@Service("consumeRecordService")
public class ConsumeRecordServiceImpl implements ConsumeRecordService {

	@Autowired
	private ConsumeRecordMapper consumeRecordMapper;
	@Autowired
	private TradeSerialNumberMapper tradeSerialNumberMapper;
	@Autowired
	private ConsumeRecordDetailMapper consumeRecordProductMapper;
	@Autowired
	private ProjectStockMapper projectStockMapper;
	@Autowired
	private StockMapper stockMapper;
	@Autowired
	private ConsumeRecordGiftMapper giftMapper;

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
		//TODO 根据支付方式扣除会员卡或赠内容
		for (ConsumeRecordDetailPo crpp : consumeRecordProducts) {
			crpp.setTradeSerialNumber(tradeSerialNumber);
			consumeRecordProductMapper.insert(crpp);
			//TODO 产品中扣除相应数量
		}
	}

	@Override
	public void createCardConsumeRecord(ConsumeRecordPo consumeRecord, List<ConsumeRecordDetailPo> consumeRecordProducts, List<ConsumeRecordGiftPo> gifts) {
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
		//TODO 根据支付方式扣除会员卡或赠内容
		for (ConsumeRecordDetailPo crpp : consumeRecordProducts) {
			crpp.setTradeSerialNumber(tradeSerialNumber);
			consumeRecordProductMapper.insert(crpp);
		}
		for(ConsumeRecordGiftPo gift : gifts){
			gift.setTradeSerialNumber(tradeSerialNumber);
			giftMapper.insert(gift);
		}
		
	}

	@Override
	public void createProjectConsumeRecord(ConsumeRecordPo consumeRecord, List<ConsumeRecordDetailPo> consumeRecordProducts) {
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
		//TODO 根据支付方式扣除会员卡或赠内容
		for (ConsumeRecordDetailPo crpp : consumeRecordProducts) {
			crpp.setTradeSerialNumber(tradeSerialNumber);
			consumeRecordProductMapper.insert(crpp);
		}
		for (ConsumeRecordDetailPo crpp : consumeRecordProducts) {
			List<ProjectStockPo> psps = projectStockMapper.getProjectStockByProId(crpp.getProductId());
			for (ProjectStockPo psp : psps) {
				StockPo stock = stockMapper.getById(psp.getStockId());
				Map<String, Number> map = new HashMap<String, Number>();
				map.put("id", stock.getId());
				map.put("totalAmount", stock.getTotalAmount().subtract(psp.getStockConsumeAmount()));
				stockMapper.updateTotalAmount(map);
			}
		}
		
	}

}
