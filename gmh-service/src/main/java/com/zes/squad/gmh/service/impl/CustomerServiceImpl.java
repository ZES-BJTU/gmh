package com.zes.squad.gmh.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.po.CustomerPo;
import com.zes.squad.gmh.mapper.CustomerMapper;
import com.zes.squad.gmh.service.CustomerService;
@Service("customerService")
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	private CustomerMapper customerMapper;

	public int insert(CustomerPo customerPo) {
		customerPo.setStoreId(ThreadContext.getUserStoreId());
		int i = customerMapper.insert(customerPo);
		return i;
	}

}
