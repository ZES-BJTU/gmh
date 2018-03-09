package com.zes.squad.gmh.web.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({ GmhException.class })
    public JsonResult<Void> handleGmhException(GmhException gmhException) {
        log.error("controller层捕获异常", gmhException);
        return JsonResults.fail(gmhException.getCode(), gmhException.getMessage());
    }

    @ExceptionHandler({ Throwable.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonResult<Void> handleException(Throwable throwable) {
        log.error("controller层捕获异常", throwable);
        return JsonResults.fail(ErrorCodeEnum.UNKNOWN_EXCEPTION.getCode(), "服务器开小差啦");
    }

}
