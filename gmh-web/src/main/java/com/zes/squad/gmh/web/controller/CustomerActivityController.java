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
import com.zes.squad.gmh.common.enums.ActivityContentTypeEnum;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.CustomerActivityQueryCondition;
import com.zes.squad.gmh.entity.union.CustomerActivityContentUnion;
import com.zes.squad.gmh.entity.union.CustomerActivityUnion;
import com.zes.squad.gmh.service.CustomerActivityService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.CustomerActivityQueryParams;
import com.zes.squad.gmh.web.entity.vo.CustomerActivityContentVo;
import com.zes.squad.gmh.web.entity.vo.CustomerActivityVo;
import com.zes.squad.gmh.web.helper.CheckHelper;

@RequestMapping(path = "/customerActivity")
@RestController
public class CustomerActivityController {
	@Autowired
	private CustomerActivityService customerActivityService;
	@RequestMapping(path = "/list", method = { RequestMethod.PUT })
	public JsonResult<PagedList<CustomerActivityVo>> doListPagedConsumeRecord(
			@RequestBody CustomerActivityQueryParams params) {
		CheckHelper.checkPageParams(params);
		CustomerActivityQueryCondition customerActivityQueryCondition = CommonConverter.map(params,
				CustomerActivityQueryCondition.class);
		customerActivityQueryCondition.setStoreId(ThreadContext.getUserStoreId());
		PagedList<CustomerActivityUnion> pagedUnions = customerActivityService
				.listPagedCustomerActivity(customerActivityQueryCondition);
		if (CollectionUtils.isEmpty(pagedUnions.getData())) {
			return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize()));
		}
		List<CustomerActivityVo> customerMemberCardVos = new ArrayList<CustomerActivityVo>();
		for (CustomerActivityUnion caUnion : pagedUnions.getData()) {
			CustomerActivityVo customerActivitydVo = buildAppointmentVoByUnion(caUnion);
			customerMemberCardVos.add(customerActivitydVo);
		}
		return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
				pagedUnions.getTotalCount(), customerMemberCardVos));
	}
	private CustomerActivityVo buildAppointmentVoByUnion(CustomerActivityUnion caUnion) {
		CustomerActivityVo vo = CommonConverter.map(caUnion, CustomerActivityVo.class);
		List<CustomerActivityContentVo> cacvList = new ArrayList<CustomerActivityContentVo>();
		for(CustomerActivityContentUnion cacu : caUnion.getCustomerActivityContents()){
			CustomerActivityContentVo cacv = CommonConverter.map(cacu, CustomerActivityContentVo.class);			
			cacv.setType(EnumUtils.getDescByKey(cacu.getType(), ActivityContentTypeEnum.class));
			cacvList.add(cacv);
		}
		vo.setCustomerActivityContents(cacvList);
		return vo;
	}
}
