package com.zes.squad.gmh.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.entity.po.CustomerPo;
import com.zes.squad.gmh.service.CustomerService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.CustomerCreateOrModifyParams;

@RequestMapping(path = "/customer")
@RestController
public class CustomerController {

	@Autowired
    private CustomerService customerService;
	
	 @RequestMapping(path = "/create", method = { RequestMethod.PUT })
	    public JsonResult<Void> doCreateCardConsume(@RequestBody CustomerCreateOrModifyParams params) {
		 CustomerPo customer = CommonConverter.map(params, CustomerPo.class);
		 	customerService.insert(customer);
	        return JsonResults.success();
	    }
}
