package com.zes.squad.gmh.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.ConsumeRecordQueryCondition;
import com.zes.squad.gmh.entity.po.ConsumeRecordDetailPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordGiftPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.entity.po.MemberCardPo;
import com.zes.squad.gmh.entity.union.ConsumeRecordUnion;
import com.zes.squad.gmh.entity.union.PrintUnion;

public interface ConsumeRecordService {

	void createProductConsumeRecord(Map<String, Object> map, ConsumeRecordPo consumeRecord,
			List<ConsumeRecordDetailPo> consumeRecordProducts);

	void createCardConsumeRecord(Map<String, Object> map, ConsumeRecordPo consumeRecord,
			List<ConsumeRecordDetailPo> consumeRecordProducts, List<ConsumeRecordGiftPo> gists,MemberCardPo memberCardPo);

	void createProjectConsumeRecord(Map<String, Object> map, ConsumeRecordPo consumeRecord,
			List<ConsumeRecordDetailPo> consumeRecordProducts);

	PagedList<ConsumeRecordUnion> listPagedConsumeRecords(ConsumeRecordQueryCondition consumeRecordQueryCondition);

	void createActivityConsumeRecord(Map<String, Object> map, ConsumeRecordPo consumeRecord,
			List<ConsumeRecordDetailPo> consumeRecordProducts);

	void modify(ConsumeRecordPo consumeRecord, List<ConsumeRecordDetailPo> consumeRecordProducts,
			List<ConsumeRecordGiftPo> gifts, Long id,MemberCardPo memberCardPo);

	public Map<String, Object> getTradeSerialNumber(String type);

	PagedList<ConsumeRecordUnion> changedListPagedConsumeRecords(ConsumeRecordQueryCondition condition);

	public void createConsumeRecord(ConsumeRecordPo consumeRecord, List<ConsumeRecordDetailPo> consumeRecordProducts,List<ConsumeRecordGiftPo> gifts,MemberCardPo memberCardPo);

	PrintUnion getPrint(Long consumeRecordId);

	Workbook exportConsumeRecord(Date beginTime, Date endTime);

	Workbook exportEmployeeIntegral(Date beginTime, Date endTime);

}
