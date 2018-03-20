package com.zes.squad.gmh.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.enums.GenderEnum;
import com.zes.squad.gmh.common.enums.UserRoleEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.union.UserUnion;
import com.zes.squad.gmh.service.UserService;
import com.zes.squad.gmh.web.constant.WebConsts;
import com.zes.squad.gmh.web.entity.vo.UserVo;

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

    public void unBind() {
        if (ThreadContext.getCurrentUser() != null) {
            ThreadContext.removeUser();
        }
    }

    public UserVo buildUserVoByUnion(UserUnion union) {
        UserVo vo = CommonConverter.map(union.getUserPo(), UserVo.class);
        vo.setRole(EnumUtils.getDescByKey(union.getUserPo().getRole().intValue(), UserRoleEnum.class));
        vo.setGender(EnumUtils.getDescByKey(union.getUserPo().getGender().intValue(), GenderEnum.class));
        if (union.getUserTokenPo() != null) {
            vo.setToken(union.getUserTokenPo().getToken());
        }
        if (union.getStorePo() != null) {
            vo.setStoreName(union.getStorePo().getName());
        }
        return vo;
    }

}
