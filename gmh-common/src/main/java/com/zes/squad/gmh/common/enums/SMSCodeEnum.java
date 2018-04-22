package com.zes.squad.gmh.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SMSCodeEnum {

    OK("OK", "短信发送成功"),
    ISP_RAM_PERMISSION_DENY("isp.RAM_PERMISSION_DENY", "权限不足,请稍后再试"),
    ISV_OUT_OF_SERVICE("isv.OUT_OF_SERVICE", "短信服务不可用"),
    ISV_PRODUCT_UN_SUBSCRIPT("isv.PRODUCT_UN_SUBSCRIPT", "短信服务未开通"),
    ISV_PRODUCT_UNSUBSCRIBE("isv.PRODUCT_UNSUBSCRIBE", "短信服务未开通"),
    ISV_ACCOUNT_NOT_EXISTS("isv.ACCOUNT_NOT_EXISTS", "短信账户不存在"),
    ISV_ACCOUNT_ABNORMAL("isv.ACCOUNT_ABNORMAL", "短信账户异常"),
    ISV_SMS_TEMPLATE_ILLEGAL("isv.SMS_TEMPLATE_ILLEGAL", "短信模板不合法"),
    ISV_SMS_SIGNATURE_ILLEGAL("isv.SMS_SIGNATURE_ILLEGAL", "短信签名不合法"),
    ISV_INVALID_PARAMETERS("isv.INVALID_PARAMETERS", "输入非法,请核对输入内容"),
    ISP_SYSTEM_ERROR("isp.SYSTEM_ERROR", "短信服务不可用"),
    ISV_MOBILE_NUMBER_ILLEGAL("isv.MOBILE_NUMBER_ILLEGAL", "手机号非法,请核对输入手机号"),
    /**
     * 针对批量发送
     */
    ISV_MOBILE_COUNT_OVER_LIMIT("isv.MOBILE_COUNT_OVER_LIMIT", "手机号码数量超出限制"),
    /**
     * 模板缺少变量
     */
    ISV_TEMPLATE_MISSING_PARAMETERS("isv.TEMPLATE_MISSING_PARAMETERS", "短信模板内容不完整"),
    /**
     * 对于验证码类服务限制一个手机号一定时间内最多发送几条
     */
    ISV_BUSINESS_LIMIT_CONTROL("isv.BUSINESS_LIMIT_CONTROL", "短信发送量超出限制"),
    ISV_INVALID_JSON_PARAM("isv.INVALID_JSON_PARAM", "输入非法,请核对输入内容"),
    ISV_BLACK_KEY_CONTROL_LIMIT("isv.BLACK_KEY_CONTROL_LIMIT", "黑名单管控"),
    ISV_PARAM_LENGTH_LIMIT("isv.PARAM_LENGTH_LIMIT", "输入非法,请核对输入内容"),
    ISV_PARAM_NOT_SUPPORT_URL("isv.PARAM_NOT_SUPPORT_URL", "短信内容不支持链接"),
    ISV_AMOUNT_NOT_ENOUGH("isv.AMOUNT_NOT_ENOUGH", "短信账户余额不足,请及时充值");

    private String key;
    private String desc;
}
