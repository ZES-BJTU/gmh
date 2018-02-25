package com.zes.squad.gmh.service.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.CustomerQueryCondition;
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

	@Override
	public int update(CustomerPo customerPo) {
		customerPo.setStoreId(ThreadContext.getUserStoreId());
		int i = customerMapper.update(customerPo);
		return i;
	}

	@Override
	public int delete(Long id) {
		int i = customerMapper.delete(id);
		return i;
	}

	@Override
	public PagedList<CustomerPo> listPagedCustomerPo(CustomerQueryCondition condition) {
		int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<CustomerPo> unions = customerMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<CustomerPo> info = new PageInfo<>(unions);
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), unions);
	}

}
