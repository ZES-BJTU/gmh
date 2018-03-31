package com.zes.squad.gmh.mapper;

import java.util.List;

import com.zes.squad.gmh.entity.po.CustomerActivityContentPo;
import com.zes.squad.gmh.entity.union.CustomerActivityContentUnion;

public interface CustomerActivityContentMapper {

	void insert(CustomerActivityContentPo customerActivityContentPo);
	
	List<CustomerActivityContentUnion> getProductListByCustomerActivityId(Long customerActivityId);
	
	List<CustomerActivityContentUnion> getProjectListByCustomerActivityId(Long customerActivityId);
	
	List<CustomerActivityContentUnion> getCardListByCustomerActivityId(Long customerActivityId);
	
	List<CustomerActivityContentUnion> getCouponListByCustomerActivityId(Long customerActivityId);
}
