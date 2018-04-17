package com.zes.squad.gmh.entity.union;

import java.util.List;

import com.zes.squad.gmh.entity.po.ConsumeRecordPo;

import lombok.Data;
@Data
public class ConsumeRecordUnion {
	
	private ConsumeRecordPo consumeRecordPo;
	private List<ConsumeRecordDetailUnion> consumeRecordDetailUnion;
	private List<ConsumeRecordGiftUnion> consumeRecordGiftUnion;
	private List<ConsumeSaleEmployeeUnion> consumeSaleEmployees;
}
