package com.zes.squad.gmh.web.entity.param;

import java.util.List;

import com.zes.squad.gmh.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordProductPo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper = true)
public class ConsumeCreateOrModifyParams extends QueryParams{
	
	private ConsumeRecordPo consumeRecordPo;
	private List<ConsumeRecordProductPo> consumeRecordProducts;
}
