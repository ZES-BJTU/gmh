package com.zes.squad.gmh.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomerActivityPo extends Po {

    private static final long serialVersionUID = 1L;

    private Long              consumeRecordId;
    private Long              customerId;
    private Integer           isValid;
    private Long              activityId;
    private Long              storeId;
}
