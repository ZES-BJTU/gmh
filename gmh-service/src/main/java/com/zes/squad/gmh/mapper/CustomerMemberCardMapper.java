package com.zes.squad.gmh.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.zes.squad.gmh.entity.condition.CustomerMemberCardQueryCondition;
import com.zes.squad.gmh.entity.po.CustomerMemberCardPo;
import com.zes.squad.gmh.entity.union.CustomerMemberCardUnion;

public interface CustomerMemberCardMapper {

    int insert(CustomerMemberCardPo po);

    List<CustomerMemberCardUnion> listCustomerMemberCardByCondition(CustomerMemberCardQueryCondition condition);

    List<CustomerMemberCardUnion> changedListCustomerMemberCardByCondition(CustomerMemberCardQueryCondition condition);

    int returnCard(CustomerMemberCardPo po);

    int turnCard(CustomerMemberCardPo po);

    CustomerMemberCardPo getById(Long id);

    BigDecimal getBalanceById(Long id);

    void calRemainMoney(Map<String, Object> map);

    void changeStore(Map<String, Object> map);

    List<CustomerMemberCardUnion> getCardListByCustomerId(Map<String, Object> map);

    List<CustomerMemberCardUnion> listCustomerMemberCardByCustomerId(Map<String, Object> map);

    void changeValidDate(Map<String, Object> map);

    void setInvalid(String unique_identifier);
}
