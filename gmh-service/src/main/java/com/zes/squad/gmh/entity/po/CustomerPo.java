package com.zes.squad.gmh.entity.po;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression.DateTime;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomerPo extends Po{

	private static final long serialVersionUID = 1L;
	
	private String name;
	private Integer gender;
	private String mobile;
	private Date birthday;
	private DateTime inputTime;
	private Integer source;
	private Long storeId;
	private String remark;
}
