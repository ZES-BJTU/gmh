package com.zes.squad.gmh.service;

import org.apache.poi.ss.usermodel.Workbook;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.CustomerQueryCondition;
import com.zes.squad.gmh.entity.po.CustomerPo;

public interface CustomerService {

    int insert(CustomerPo customerPo);

    int update(CustomerPo customerPo);

    int delete(Long id);

    PagedList<CustomerPo> listPagedCustomerPo(CustomerQueryCondition condition);

    Workbook exportCustomerRecord();
}
