package com.zes.squad.gmh.mapper;

import java.util.List;
import java.util.Map;

import com.zes.squad.gmh.entity.po.CustomerMemberCardContentPo;
import com.zes.squad.gmh.entity.union.CustomerMemberCardContentUnion;

public interface CustomerMemberCardContentMapper {

	void insert(CustomerMemberCardContentPo po);
	
	List<CustomerMemberCardContentUnion> getContentList(Long customerMemberCardId);
	
	CustomerMemberCardContentUnion getContent(Long id);
	
	void calAmount(Map<String,Object> map);
	
	List<CustomerMemberCardContentUnion> getCouponContentList(Long customerMemberCardId);
	
	List<CustomerMemberCardContentUnion> getProjectContentList(Long customerMemberCardId);

	CustomerMemberCardContentPo getByCustomerMemberCardIdRelatedId(Map<String, Object> map);

}
