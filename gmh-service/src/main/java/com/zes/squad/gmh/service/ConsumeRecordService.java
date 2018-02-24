package com.zes.squad.gmh.service;

import java.util.List;

import com.zes.squad.gmh.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordProductPo;

public interface ConsumeRecordService {

	void createProductConsumeRecord(ConsumeRecordPo consumeRecord,List<ConsumeRecordProductPo> consumeRecordProducts);
}
