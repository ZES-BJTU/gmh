package com.zes.squad.gmh.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.zes.squad.gmh.cache.CacheService;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.constant.CacheConsts;
import com.zes.squad.gmh.helper.SMSHelper;
import com.zes.squad.gmh.service.MessageService;

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
        String authCode = SMSHelper.generateAuthCode(6);
        String content = "";//TODO 文案后续修改
        SMSHelper.sendMessage(mobile, content);
        String cacheKey = String.format(CACHE_KEY_AUTH_CODE_PREFIX, mobile);
        //验证码有效期默认2分钟
        cacheService.put(cacheKey, authCode, CacheConsts.TWO_MINUTE);
    }

    @Override
    public void validateAuthCode(String mobile, String authCode) {
        boolean isCacheValid = cacheService.isValid();
        if (!isCacheValid) {
            throw new GmhException(ErrorCodeEnum.CACHE_EXCEPTION, "验证码服务不可用");
        }
        String cacheKey = String.format(CACHE_KEY_AUTH_CODE_PREFIX, mobile);
        String cacheAuthCode = cacheService.get(cacheKey);
        if (Strings.isNullOrEmpty(cacheAuthCode)) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION, "验证码已失效,请重新获取");
        }
        if (!Objects.equals(authCode, cacheAuthCode)) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION, "验证码错误,请重新输入");
        }
    }

}
