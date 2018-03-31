package com.zes.squad.gmh.entity.union;

import java.util.List;

import lombok.Data;
@Data
public class CustomerActivityUnion {
	private Long id;
	private String customerName;
	private String activityName;
	private List<CustomerActivityContentUnion> customerActivityContents;
}
