package com.zes.squad.gmh.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.enums.YesOrNoEnum;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.CustomerMemberCardQueryCondition;
import com.zes.squad.gmh.entity.union.CustomerMemberCardUnion;
import com.zes.squad.gmh.service.CustomerMemberCardService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.CustomerMemberCardQueryParams;
import com.zes.squad.gmh.web.entity.vo.CustomerMemberCardVo;
import com.zes.squad.gmh.web.helper.CheckHelper;

@RequestMapping(path = "/customerMemberCard")
@RestController
public class CustomerMemberCardController {
	
	@Autowired
    private CustomerMemberCardService customerMemberCardService;
	
	 @RequestMapping(path = "/list", method = { RequestMethod.PUT })
	    public JsonResult<PagedList<CustomerMemberCardVo>> doListPagedConsumeRecord(@RequestBody CustomerMemberCardQueryParams params) {
	        CheckHelper.checkPageParams(params);
	        CustomerMemberCardQueryCondition customerMemberCardQueryCondition = CommonConverter.map(params,
	        		CustomerMemberCardQueryCondition.class);
	        customerMemberCardQueryCondition.setStoreId(ThreadContext.getUserStoreId());
	        PagedList<CustomerMemberCardUnion> pagedUnions = customerMemberCardService.listPagedCustomerMemberCard(customerMemberCardQueryCondition);
	        if (CollectionUtils.isEmpty(pagedUnions.getData())) {
	            return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize()));
	        }
	        List<CustomerMemberCardVo> customerMemberCardVos = new ArrayList<CustomerMemberCardVo>();
	        for (CustomerMemberCardUnion customerMemberCardUnion : pagedUnions.getData()) {
	        	CustomerMemberCardVo customerMemberCardVo = buildAppointmentVoByUnion(customerMemberCardUnion);
	        	customerMemberCardVos.add(customerMemberCardVo);
	        }
	        return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
	                pagedUnions.getTotalCount(), customerMemberCardVos));
	    }

	private CustomerMemberCardVo buildAppointmentVoByUnion(CustomerMemberCardUnion customerMemberCardUnion) {
		
		CustomerMemberCardVo vo = CommonConverter.map(customerMemberCardUnion, CustomerMemberCardVo.class);
		vo.setIsReturned(EnumUtils.getDescByKey(customerMemberCardUnion.getIsReturned(), YesOrNoEnum.class));
		vo.setIsValid(EnumUtils.getDescByKey(customerMemberCardUnion.getIsValid(), YesOrNoEnum.class));
		vo.setIsTurned(EnumUtils.getDescByKey(customerMemberCardUnion.getIsTurned(), YesOrNoEnum.class));
		return vo;
	}
}
