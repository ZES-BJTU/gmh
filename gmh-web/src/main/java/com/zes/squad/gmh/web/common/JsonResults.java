package com.zes.squad.gmh.web.common;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * json返回对象操作类
 * 
 * @author yuhuan.zhou 2018年1月14日 上午12:02:32
 */
public class JsonResults {

    private static final int DEFAULT_SUCCESS_CODE = 0;

    public static <T> JsonResult<T> success() {
        return success(null);
    }

    public static <T> JsonResult<T> success(T data) {
        JsonResult<T> result = new JsonResult<>();
        result.setCode(DEFAULT_SUCCESS_CODE);
        result.setData(data);
        return result;
    }

    public static <T> JsonResult<T> fail(int code, String message) {
        JsonResult<T> result = new JsonResult<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class JsonResult<T> {
        private int    code;
        private String message;
        private T      data;
    }

}
