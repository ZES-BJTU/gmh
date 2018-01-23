package com.zes.squad.gmh.service;

public interface MessageService {

    /**
     * 发送验证码
     * 
     * @param mobile
     */
    void sendAuthCode(String mobile);

    /**
     * 验证验证码
     * 
     * @param mobile
     * @param authCode
     */
    void validateAuthCode(String mobile, String authCode);

}
