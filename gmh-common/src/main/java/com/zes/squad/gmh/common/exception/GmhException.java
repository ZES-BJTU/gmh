package com.zes.squad.gmh.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 全局通用业务异常
 * 
 * @author yuhuan.zhou 2018年1月13日 下午11:39:47
 */
@Getter
@AllArgsConstructor
public class GmhException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int               code;
    private String            message;

    public GmhException(ErrorCodeEnum errorCodeEnum) {
        this(errorCodeEnum.getCode(), errorCodeEnum.getMessage());
    }

    public GmhException(ErrorCodeEnum errorCodeEnum, String message) {
        this(errorCodeEnum.getCode(), message);
    }

}
