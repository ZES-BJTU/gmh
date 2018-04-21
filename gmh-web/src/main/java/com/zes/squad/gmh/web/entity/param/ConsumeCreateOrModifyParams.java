package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;
import java.util.List;

import com.zes.squad.gmh.entity.po.ConsumeRecordDetailPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordGiftPo;
import com.zes.squad.gmh.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.entity.po.ConsumeSaleEmployeePo;
import com.zes.squad.gmh.entity.po.MemberCardPo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper = true)
public class ConsumeCreateOrModifyParams extends QueryParams{
	
	
	private ConsumeRecordPo consumeRecordPo;	
	private String validStr;
	private List<ConsumeRecordDetailPo> consumeRecordDetails;
	private MemberCardPo memberCardPo;
	private List<ConsumeRecordGiftPo> gifts;
	private List<ConsumeSaleEmployeePo> consumeSaleEmployees;
	private List<Long> employeeIds;
	private List<BigDecimal> percents;
}
