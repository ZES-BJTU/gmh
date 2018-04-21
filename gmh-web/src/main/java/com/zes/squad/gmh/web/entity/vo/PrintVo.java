package com.zes.squad.gmh.web.entity.vo;

import java.util.List;

import com.zes.squad.gmh.entity.union.CustomerMemberCardUnion;

import lombok.Data;

@Data
public class PrintVo {

    private ConsumeRecordVo               consumeRecordVo;
    private StoreVo                       storeVo;
    private List<CustomerMemberCardUnion> customerMemberCardUnions;

}
