package com.zes.squad.gmh.web.entity.param;

import lombok.Data;

@Data
public class UserChangePasswordParams {

    private String originalPassword;
    private String newPassword;

}
