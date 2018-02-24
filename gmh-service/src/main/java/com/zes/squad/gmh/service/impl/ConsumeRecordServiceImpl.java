package com.zes.squad.gmh.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordProductPo;
import com.zes.squad.gmh.entity.po.ProjectStockPo;
import com.zes.squad.gmh.entity.po.StockPo;
import com.zes.squad.gmh.mapper.ConsumeRecordMapper;
import com.zes.squad.gmh.mapper.ConsumeRecordProductMapper;
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
	private ConsumeRecordProductMapper consumeRecordProductMapper;
	@Autowired
	private ProjectStockMapper projectStockMapper;
	@Autowired
	private StockMapper stockMapper;
	@Override
	public void createProductConsumeRecord(ConsumeRecordPo consumeRecord,
			List<ConsumeRecordProductPo> consumeRecordProducts) {
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
			for (ConsumeRecordProductPo crpp : consumeRecordProducts) {
				crpp.setTradeSerialNumber(tradeSerialNumber);
				consumeRecordProductMapper.insert(crpp);
			}
		if(consumeRecord.getConsumeType()==0){
			// TODO 在product中减去数量
		}else if(consumeRecord.getConsumeType()==1){
			for(ConsumeRecordProductPo crpp : consumeRecordProducts){
				List<ProjectStockPo> psps = projectStockMapper.getProjectStockByProId(crpp.getProductId());
				for(ProjectStockPo psp : psps){
					StockPo stock = stockMapper.getById(psp.getStockId());
					Map<String, Number> map = new HashMap<String, Number>();
					map.put("id", stock.getId());
					map.put("totalAmount",stock.getTotalAmount().subtract(psp.getStockConsumeAmount()));
					stockMapper.updateTotalAmount(map);
				}
			}
		}else if(consumeRecord.getConsumeType()==2){
			//在会员-会员卡中添加信息
		}
	}

}
