package com.zes.squad.gmh.web.entity.vo;

import java.util.Date;

import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression.DateTime;
@Data
public class CustomerVo {

	private String name;
	private String gender;
	private String mobile;
	private Date birthday;
	private DateTime inputTime;
	private String source;
	private String remark;
}
