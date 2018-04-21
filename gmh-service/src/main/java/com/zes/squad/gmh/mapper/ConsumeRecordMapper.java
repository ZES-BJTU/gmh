package com.zes.squad.gmh.mapper;

import java.util.List;
import java.util.Map;

import com.zes.squad.gmh.entity.condition.ConsumeRecordQueryCondition;
import com.zes.squad.gmh.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.entity.union.EmployeeIntegralUnion;

public interface ConsumeRecordMapper {

    int insert(ConsumeRecordPo consumeRecordPo);

    List<ConsumeRecordPo> listConsumeRecordByCondition(ConsumeRecordQueryCondition condition);

    List<ConsumeRecordPo> changedListConsumeRecordByCondition(ConsumeRecordQueryCondition condition);

    void modify(Long id);

    ConsumeRecordPo getById(Long id);

    List<EmployeeIntegralUnion> getIntegralEmployeeIntegralByEmployeeId(Map<String, Object> map);

    List<Long> getConsumeIdListByTime(Map<String, Object> map);
}
