package com.zes.squad.gmh.entity.union;

import com.zes.squad.gmh.entity.po.MemberCardPo;
import com.zes.squad.gmh.entity.po.ProjectPo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberCardUnion extends Union {

    private static final long serialVersionUID = 1L;

    private MemberCardPo      memberCardPo;
    private ProjectPo         projectPo;

}
