package com.zes.squad.gmh.entity.union;

import com.zes.squad.gmh.entity.po.ActivityContentPo;
import com.zes.squad.gmh.entity.po.MemberCardPo;
import com.zes.squad.gmh.entity.po.ProductPo;
import com.zes.squad.gmh.entity.po.ProjectPo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ActivityContentUnion extends Union {

    private static final long serialVersionUID = 1L;

    private ActivityContentPo activityContentPo;
    private ProjectPo         projectPo;
    private ProductPo         productPo;
    private MemberCardPo      memberCardPo;

}
