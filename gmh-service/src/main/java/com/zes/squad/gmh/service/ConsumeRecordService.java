package com.zes.squad.gmh.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.ConsumeRecordQueryCondition;
import com.zes.squad.gmh.entity.po.ConsumeRecordDetailPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordGiftPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.entity.po.ConsumeSaleEmployeePo;
import com.zes.squad.gmh.entity.po.MemberCardPo;
import com.zes.squad.gmh.entity.union.ConsumeRecordUnion;
import com.zes.squad.gmh.entity.union.PrintUnion;

public interface ConsumeRecordService {

    void createProductConsumeRecord(Map<String, Object> map, ConsumeRecordPo consumeRecord,
                                    List<ConsumeRecordDetailPo> consumeRecordProducts,
                                    List<ConsumeSaleEmployeePo> consumeSaleEmployees);

    void createCardConsumeRecord(Map<String, Object> map, ConsumeRecordPo consumeRecord,
                                 List<ConsumeRecordDetailPo> consumeRecordProducts, List<ConsumeRecordGiftPo> gists,
                                 MemberCardPo memberCardPo, List<ConsumeSaleEmployeePo> consumeSaleEmployees);

    void createProjectConsumeRecord(Map<String, Object> map, ConsumeRecordPo consumeRecord,
                                    List<ConsumeRecordDetailPo> consumeRecordProducts,
                                    List<ConsumeSaleEmployeePo> consumeSaleEmployees);

    PagedList<ConsumeRecordUnion> listPagedConsumeRecords(ConsumeRecordQueryCondition consumeRecordQueryCondition);

    void createActivityConsumeRecord(Map<String, Object> map, ConsumeRecordPo consumeRecord,
                                     List<ConsumeRecordDetailPo> consumeRecordProducts,
                                     List<ConsumeSaleEmployeePo> consumeSaleEmployees);

    void modify(ConsumeRecordPo consumeRecord, List<ConsumeRecordDetailPo> consumeRecordProducts,
                List<ConsumeRecordGiftPo> gifts, Long id, MemberCardPo memberCardPo,
                List<ConsumeSaleEmployeePo> consumeSaleEmployees);

    public Map<String, Object> getTradeSerialNumber(String type);

    PagedList<ConsumeRecordUnion> changedListPagedConsumeRecords(ConsumeRecordQueryCondition condition);

    public void createConsumeRecord(ConsumeRecordPo consumeRecord, List<ConsumeRecordDetailPo> consumeRecordProducts,
                                    List<ConsumeRecordGiftPo> gifts, MemberCardPo memberCardPo,
                                    List<ConsumeSaleEmployeePo> consumeSaleEmployees);

    PrintUnion getPrint(Long consumeRecordId);

    Workbook exportConsumeRecord(Integer consumeType, Date beginTime, Date endTime);

    Workbook exportEmployeeIntegral(Date beginTime, Date endTime);

    BigDecimal doCalMoney(ConsumeRecordPo consumeRecordPo, List<ConsumeRecordDetailPo> consumeRecordDetails,
                          List<ConsumeRecordGiftPo> gifts, MemberCardPo memberCardPo);

    Workbook exportEmployeeSale(Date beginTime, Date endTime);

}
