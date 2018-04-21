package com.zes.squad.gmh.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.zes.squad.gmh.cache.CacheService;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.constant.CacheConsts;
import com.zes.squad.gmh.helper.SMSHelper;
import com.zes.squad.gmh.property.MessageProperties;
import com.zes.squad.gmh.service.MessageService;

import lombok.extern.slf4j.Slf4j;

/**
 * 短信服务需要做防刷处理
 * 
 * @author zhouyuhuan 2018年2月24日 下午11:44:03
 */
@Slf4j
@Service("messageService")
public class MessageServiceImpl implements MessageService {

    private static final String CACHE_KEY_AUTH_CODE_PREFIX = "_cache_key_auth_code_%s";

    @Autowired
    private CacheService        cacheService;

    @Override
    public void sendAuthCode(String mobile) {
        boolean isCacheValid = cacheService.isValid();
        if (!isCacheValid) {
            throw new GmhException(ErrorCodeEnum.CACHE_EXCEPTION, "验证码服务不可用");
        }
        //验证码接口防刷处理
        //针对同一个手机号防刷
        String mobileCacheKey = String.format(CACHE_KEY_AUTH_CODE_PREFIX, mobile);
        String authCode = SMSHelper.generateAuthCode(6);
        SMSHelper.sendMessage(mobile, MessageProperties.get("template.auth.code"), buildAuthCodeContent(authCode));
        //验证码有效期默认5分钟
        cacheService.put(mobileCacheKey, authCode, CacheConsts.FIVE_MINUTE);
    }

    @Override
    public void validateAuthCode(String mobile, String authCode) {
        boolean isCacheValid = cacheService.isValid();
        if (!isCacheValid) {
            throw new GmhException(ErrorCodeEnum.CACHE_EXCEPTION, "验证码服务不可用,请稍后再试");
        }
        String cacheKey = String.format(CACHE_KEY_AUTH_CODE_PREFIX, mobile);
        String cacheAuthCode = cacheService.get(cacheKey);
        if (Strings.isNullOrEmpty(cacheAuthCode)) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION, "验证码已失效,请重新获取");
        }
        if (!Objects.equals(authCode, cacheAuthCode)) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION, "验证码错误,请重新输入");
        }
        log.info("验证码验证成功");
        cacheService.delete(cacheKey);
    }

    private String buildAuthCodeContent(String code) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", code);
        return jsonObject.toString();
    }

}
