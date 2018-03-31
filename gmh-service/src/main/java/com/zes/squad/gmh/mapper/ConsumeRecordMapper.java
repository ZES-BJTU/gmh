package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.condition.ConsumeRecordQueryCondition;
import com.zes.squad.gmh.entity.po.ConsumeRecordPo;

public interface ConsumeRecordMapper {

	int insert(ConsumeRecordPo consumeRecordPo);

	List<ConsumeRecordPo> listConsumeRecordByCondition(ConsumeRecordQueryCondition condition);
	
	List<ConsumeRecordPo> changedListConsumeRecordByCondition(ConsumeRecordQueryCondition condition);
	
	void modify(Long id);
}
