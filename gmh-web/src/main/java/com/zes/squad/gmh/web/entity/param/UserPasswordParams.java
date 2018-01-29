package com.zes.squad.gmh.web.entity.param;

import lombok.Data;

@Data
public class UserPasswordParams {

    private String originalPassword;
    private String newPassword;

}
