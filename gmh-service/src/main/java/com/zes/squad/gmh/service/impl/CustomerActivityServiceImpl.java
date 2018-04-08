package com.zes.squad.gmh.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.CustomerActivityQueryCondition;
import com.zes.squad.gmh.entity.po.CustomerPo;
import com.zes.squad.gmh.entity.union.CustomerActivityContentUnion;
import com.zes.squad.gmh.entity.union.CustomerActivityUnion;
import com.zes.squad.gmh.mapper.CustomerActivityContentMapper;
import com.zes.squad.gmh.mapper.CustomerActivityMapper;
import com.zes.squad.gmh.mapper.CustomerMapper;
import com.zes.squad.gmh.service.CustomerActivityService;

@Service("customerActivityService")
public class CustomerActivityServiceImpl implements CustomerActivityService {
	@Autowired
	private CustomerActivityMapper customerActivityMapper;
	@Autowired
	private CustomerActivityContentMapper customerActivityContentMapper;
	@Autowired
	private CustomerMapper customerMapper;
	@Override
	public PagedList<CustomerActivityUnion> listPagedCustomerActivity(CustomerActivityQueryCondition condition) {
		int pageNum = condition.getPageNum();
		int pageSize = condition.getPageSize();
		PageHelper.startPage(pageNum, pageSize);
		List<CustomerActivityUnion> customerActivityUnionList = customerActivityMapper.listByCondition(condition);
		for (CustomerActivityUnion cau : customerActivityUnionList) {
			cau.setCustomerActivityContents(getAllContentUnion(cau.getId()));
		}
		PageInfo<CustomerActivityUnion> info = new PageInfo<>(customerActivityUnionList);
		
		return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(),
				customerActivityUnionList);
	}

	private List<CustomerActivityContentUnion> getAllContentUnion(Long id) {
		List<CustomerActivityContentUnion> productList = customerActivityContentMapper
				.getProductListByCustomerActivityId(id);
		List<CustomerActivityContentUnion> projectList = customerActivityContentMapper
				.getProjectListByCustomerActivityId(id);
		List<CustomerActivityContentUnion> cardtList = customerActivityContentMapper
				.getCardListByCustomerActivityId(id);
		List<CustomerActivityContentUnion> couponList = customerActivityContentMapper
				.getCouponListByCustomerActivityId(id);

		List<CustomerActivityContentUnion> allList = new ArrayList<CustomerActivityContentUnion>();
		allList.addAll(projectList);
		allList.addAll(productList);
		allList.addAll(couponList);
		allList.addAll(cardtList);

		return allList;
	}

	@Override
	public List<CustomerActivityUnion> getCardListByMobile(String customerMobile) {
		
		Map<String,Object> map = new HashMap<String,Object>();		
		map.put("storeId", ThreadContext.getUserStoreId());
		CustomerPo customer = customerMapper.getByMobile(customerMobile);
		map.put("customerId", customer.getId());
		List<CustomerActivityUnion> customerActivityUnions = customerActivityMapper
				.getActivityListByCustomerId(map);
		for (CustomerActivityUnion cau : customerActivityUnions) {
			cau.setCustomerActivityContents(getAllContentUnion(cau.getId()));
		}
		return customerActivityUnions;
	}
}
