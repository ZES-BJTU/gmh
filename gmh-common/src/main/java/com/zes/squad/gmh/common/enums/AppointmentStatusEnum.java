package com.zes.squad.gmh.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zpf
 * 预约状态枚举
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AppointmentStatusEnum {

	 	TODO(1, "未完成"),
	    FINISH(2, "已完成"),
		CANCEL(3, "已取消");

	    private int    key;
	    private String desc;
}
