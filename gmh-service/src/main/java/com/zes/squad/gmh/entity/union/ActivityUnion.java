package com.zes.squad.gmh.entity.union;

import java.util.List;

import com.zes.squad.gmh.entity.po.ActivityPo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ActivityUnion extends Union {

    private static final long          serialVersionUID = 1L;

    private ActivityPo                 activityPo;
    private List<ActivityContentUnion> activityContentUnions;

}
