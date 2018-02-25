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
import com.zes.squad.gmh.common.enums.GenderEnum;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.entity.condition.CustomerQueryCondition;
import com.zes.squad.gmh.entity.po.CustomerPo;
import com.zes.squad.gmh.service.CustomerService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.CustomerCreateOrModifyParams;
import com.zes.squad.gmh.web.entity.param.CustomerQueryParams;
import com.zes.squad.gmh.web.entity.vo.CustomerVo;
import com.zes.squad.gmh.web.helper.CheckHelper;

@RequestMapping(path = "/customer")
@RestController
public class CustomerController {

	@Autowired
    private CustomerService customerService;
	
	 @RequestMapping(path = "/create", method = { RequestMethod.PUT })
	    public JsonResult<Void> doCreateCustomer(@RequestBody CustomerCreateOrModifyParams params) {
		 CustomerPo customer = CommonConverter.map(params, CustomerPo.class);
		 	customerService.insert(customer);
	        return JsonResults.success();
	    }
	 @RequestMapping(path = "/modify", method = { RequestMethod.PUT })
	    public JsonResult<Void> doModifyCustomer(@RequestBody CustomerCreateOrModifyParams params) {
		 CustomerPo customer = CommonConverter.map(params, CustomerPo.class);
		 	customerService.update(customer);
	        return JsonResults.success();
	    }
	 @RequestMapping(path = "/delete", method = { RequestMethod.PUT })
	    public JsonResult<Void> doDeleteCustomer(@RequestBody Long id) {
		 
		 	customerService.delete(id);
	        return JsonResults.success();
	    }
	 @RequestMapping(path = "/list", method = { RequestMethod.PUT })
	    public JsonResult<PagedList<CustomerVo>> doListPagedAppointment(@RequestBody CustomerQueryParams params) {
	        CheckHelper.checkPageParams(params);
	        CustomerQueryCondition condition = CommonConverter.map(params, CustomerQueryCondition.class);
	        PagedList<CustomerPo> customerPos = customerService.listPagedCustomerPo(condition);
	        if (CollectionUtils.isEmpty(customerPos.getData())) {
	            return JsonResults.success(PagedLists.newPagedList(customerPos.getPageNum(), customerPos.getPageSize()));
	        }
	        List<CustomerVo> vos = new ArrayList<CustomerVo>();
	        for (CustomerPo po : customerPos.getData()) {
	        	CustomerVo vo = buildCustomerVoByPo(po);
	            vos.add(vo);
	        }
	        return JsonResults.success(PagedLists.newPagedList(customerPos.getPageNum(), customerPos.getPageSize(),
	        		customerPos.getTotalCount(), vos));
	    }
	private CustomerVo buildCustomerVoByPo(CustomerPo po) {
		CustomerVo vo = CommonConverter.map(po, CustomerVo.class);
		vo.setGender(EnumUtils.getDescByKey(po.getGender(), GenderEnum.class));
		//TODO source确认枚举
		return vo;
	}
}
