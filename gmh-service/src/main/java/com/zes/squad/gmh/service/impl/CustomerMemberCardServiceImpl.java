package com.zes.squad.gmh.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.CustomerMemberCardQueryCondition;
import com.zes.squad.gmh.entity.union.CustomerMemberCardUnion;
import com.zes.squad.gmh.mapper.CustomerMemberCardMapper;
import com.zes.squad.gmh.service.CustomerMemberCardService;
@Service("customerMemberCardService")
public class CustomerMemberCardServiceImpl implements CustomerMemberCardService{

	@Autowired
	private CustomerMemberCardMapper customerMemberCardMapper;
	@Override
	public PagedList<CustomerMemberCardUnion> listPagedCustomerMemberCard(
			CustomerMemberCardQueryCondition condition) {
		int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<CustomerMemberCardUnion> customerMemberCardUnions = customerMemberCardMapper.listCustomerMemberCardByCondition(condition);
        PageInfo<CustomerMemberCardUnion> info = new PageInfo<>(customerMemberCardUnions);
		return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), customerMemberCardUnions);
	}

}
