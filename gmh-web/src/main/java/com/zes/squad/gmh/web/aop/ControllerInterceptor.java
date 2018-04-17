package com.zes.squad.gmh.web.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.common.util.JsonUtils;
import com.zes.squad.gmh.web.common.JsonResults;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ControllerInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long beginTime = System.currentTimeMillis();
        try {
            Object result = invocation.proceed();
            long endTime = System.currentTimeMillis();
            log.error(">>>>>调用controller成功, 调用方法 :{}, 接口耗时:{}ms", getApiInfo(invocation),
                    JsonUtils.toJson(invocation.getArguments()), endTime - beginTime);
            return result;
        } catch (GmhException e) {
            long endTime = System.currentTimeMillis();
            log.error(">>>>>调用controller异常, 调用方法 :{}, 接口耗时:{}ms", getApiInfo(invocation),
                    JsonUtils.toJson(invocation.getArguments()), endTime - beginTime, e);
            return JsonResults.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            log.error(">>>>>调用controller异常, 调用方法 :{}, 接口耗时:{}ms", getApiInfo(invocation),
                    JsonUtils.toJson(invocation.getArguments()), endTime - beginTime, e);
            return JsonResults.fail(ErrorCodeEnum.UNKNOWN_EXCEPTION.getCode(), "服务器开小差了");
        }
    }

    private String getApiInfo(MethodInvocation invocation) {
        StringBuilder builder = new StringBuilder();
        builder.append(invocation.getThis().getClass().getName());
        builder.append("#");
        builder.append(invocation.getMethod().getName());
        return builder.toString();
    }

}
