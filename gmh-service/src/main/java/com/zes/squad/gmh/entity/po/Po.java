package com.zes.squad.gmh.entity.po;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Po implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long              id;
    private Date              createdTime;
    private Date              modifiedTime;

}
