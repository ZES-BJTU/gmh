package com.zes.squad.gmh.web.entity.param;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper = true)
public class CustomerCreateOrModifyParams extends QueryParams{

	private String name;
	private Integer gender;
	private String mobile;
	private Date birthday;
	private Integer source;
	private String remark;
}
