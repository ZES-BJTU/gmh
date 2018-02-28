package com.zes.squad.gmh.service;

import java.util.List;

import com.zes.squad.gmh.entity.po.ConsumeRecordGiftPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordDetailPo;

public interface ConsumeRecordService {

	void createProductConsumeRecord(ConsumeRecordPo consumeRecord,List<ConsumeRecordDetailPo> consumeRecordProducts);
	
	void createCardConsumeRecord(ConsumeRecordPo consumeRecord,List<ConsumeRecordDetailPo> consumeRecordProducts, List<ConsumeRecordGiftPo> gists);
	
	void createProjectConsumeRecord(ConsumeRecordPo consumeRecord,List<ConsumeRecordDetailPo> consumeRecordProducts);
}
