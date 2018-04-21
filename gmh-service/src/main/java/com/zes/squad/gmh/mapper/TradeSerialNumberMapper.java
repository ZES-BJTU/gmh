package com.zes.squad.gmh.mapper;

public interface TradeSerialNumberMapper {

    Integer getProductNumber();

    Integer getCardNumber();

    Integer getProjectNumber();

    Integer getActivityNumber();

    void productNumberAdd(Integer number);

    void cardNumberAdd(Integer number);

    void projectNumberAdd(Integer number);

    void activityNumberAdd(Integer number);
}
