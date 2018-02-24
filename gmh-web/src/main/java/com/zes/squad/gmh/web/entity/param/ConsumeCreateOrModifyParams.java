package com.zes.squad.gmh.web.entity.param;

import java.util.List;

import com.zes.squad.gmh.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordProductPo;

import lombok.Data;

@Data
public class ConsumeCreateOrModifyParams {
	
	private ConsumeRecordPo consumeRecordPo;
	private List<ConsumeRecordProductPo> consumeRecordProducts;
}
