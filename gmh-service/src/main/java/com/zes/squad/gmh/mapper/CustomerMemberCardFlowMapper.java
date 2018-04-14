package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.po.CustomerMemberCardFlowPo;

public interface CustomerMemberCardFlowMapper {
	
	void insert(CustomerMemberCardFlowPo po);
	
	List<CustomerMemberCardFlowPo> getListByConsumeRecordId(Long consumeRecordId);
}
