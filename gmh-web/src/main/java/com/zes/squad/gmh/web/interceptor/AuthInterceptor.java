package com.zes.squad.gmh.web.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.common.base.Strings;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.util.JsonUtils;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.union.UserUnion;
import com.zes.squad.gmh.service.UserService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.constant.WebConsts;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService         userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        try {
            String token = request.getHeader(WebConsts.X_TOKEN);
            if (Strings.isNullOrEmpty(token)) {
                log.error("token为空, request uri is {}", request.getRequestURI());
                sendJsonResponse(response,
                        JsonResults.fail(ErrorCodeEnum.WEB_EXCEPTION_AUTH_FAILED.getCode(), "token为空"));
                return false;
            }
            UserUnion union = userService.queryUserByToken(token);
            if (union == null) {
                log.error("token验证失败, request uri is {}", request.getRequestURI());
                sendJsonResponse(response,
                        JsonResults.fail(ErrorCodeEnum.WEB_EXCEPTION_AUTH_FAILED.getCode(), "登录已过期,请重新登录"));
                return false;
            }
            ThreadContext.setUser(union.getUserPo());
            return true;
        } catch (Exception e) {
            log.error("检验用户身份异常, request uri is {}", request.getRequestURI(), e);
            sendJsonResponse(response,
                    JsonResults.fail(ErrorCodeEnum.WEB_EXCEPTION_AUTH_FAILED.getCode(), "登录已过期,请重新登录"));
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView)
            throws Exception {
        try {
            if (ThreadContext.getCurrentUser() != null) {
                ThreadContext.removeUser();
            }
        } catch (Exception e) {
            log.error("清除ThreadLocal中用户登录信息异常", e);
            sendJsonResponse(response, JsonResults.fail(ErrorCodeEnum.UNKNOWN_EXCEPTION.getCode(), "服务器开小差"));
        }
    }

    private void sendJsonResponse(HttpServletResponse response, Object message) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            @Cleanup
            PrintWriter writer = response.getWriter();
            writer.write(JsonUtils.toJson(message));
            writer.flush();
        } catch (Exception e) {
            log.error("发送json数据到前端异常", e);
        }
    }

}
