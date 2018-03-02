package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;

import com.zes.squad.gmh.entity.po.ConsumeRecordDetailPo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConsumeRecordDetailWithCard extends ConsumeRecordDetailPo{



	private static final long serialVersionUID = 1L;
	
	private Integer times;
	private BigDecimal money;
	
}
