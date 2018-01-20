package com.zes.squad.gmh.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.entity.union.UserUnion;
import com.zes.squad.gmh.service.UserService;
import com.zes.squad.gmh.web.constant.WebConsts;

public class BaseController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserService        userService;

    public UserUnion getUser() {
        String token = request.getHeader(WebConsts.X_TOKEN);
        UserUnion union = userService.queryUserByToken(token);
        if (union == null) {
            throw new GmhException(ErrorCodeEnum.WEB_EXCEPTION_AUTH_FAILED, "登录已过期,请重新登录");
        }
        return union;
    }

}
