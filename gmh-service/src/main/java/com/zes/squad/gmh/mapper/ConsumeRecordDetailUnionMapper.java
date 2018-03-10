package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.union.ConsumeRecordDetailUnion;

public interface ConsumeRecordDetailUnionMapper {

	List<ConsumeRecordDetailUnion> getRecordDetailUnionByTradeSerialNumber(String tradeSerialNumber);
	
}
