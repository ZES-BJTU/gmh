package com.zes.squad.gmh.entity.union;

import java.util.List;

import com.zes.squad.gmh.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.entity.po.StorePo;

import lombok.Data;
@Data
public class PrintUnion {

	private ConsumeRecordPo consumeRecordPo;
	private StorePo storePo;
	private List<ConsumeRecordDetailUnion> consumeRecordDetailUnion;
	private List<ConsumeRecordGiftUnion> consumeRecordGiftUnion;
	private List<CustomerMemberCardUnion> customerMemberCardUnions;
}
