package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.po.ConsumeRecordGiftPo;
import com.zes.squad.gmh.entity.union.ConsumeRecordGiftUnion;

public interface ConsumeRecordGiftMapper {

	void insert(ConsumeRecordGiftPo gift);

	List<ConsumeRecordGiftUnion> getRecordGiftUnionByTradeSerialNumber(String tradeSerialNumber);
	
}
