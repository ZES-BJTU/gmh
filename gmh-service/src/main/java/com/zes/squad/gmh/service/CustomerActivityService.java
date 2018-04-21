package com.zes.squad.gmh.service;

import java.util.List;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.CustomerActivityQueryCondition;
import com.zes.squad.gmh.entity.union.CustomerActivityUnion;

public interface CustomerActivityService {

    PagedList<CustomerActivityUnion> listPagedCustomerActivity(CustomerActivityQueryCondition customerActivityQueryCondition);

    List<CustomerActivityUnion> getAcitvityListByMobile(Integer paymentWay, String customerMobile);

}
