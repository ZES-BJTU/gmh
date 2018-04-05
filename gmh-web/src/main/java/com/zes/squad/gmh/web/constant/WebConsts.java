package com.zes.squad.gmh.web.constant;

public class WebConsts {

    /**
     * 正常需要拦截请求的请求头
     */
    public static final String X_TOKEN = "X-token";
    /**
     * 所有导出请求,因为使用的是超链接下载导出excel,采用URI拼接token验证身份
     */
    public static final String TOKEN   = "token";

}
