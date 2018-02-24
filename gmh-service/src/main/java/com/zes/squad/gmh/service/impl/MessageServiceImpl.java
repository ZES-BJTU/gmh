package com.zes.squad.gmh.service.impl;

import java.text.MessageFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.zes.squad.gmh.cache.CacheService;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.common.helper.LogicHelper;
import com.zes.squad.gmh.constant.CacheConsts;
import com.zes.squad.gmh.entity.po.UserPo;
import com.zes.squad.gmh.helper.SMSHelper;
import com.zes.squad.gmh.mapper.UserMapper;
import com.zes.squad.gmh.property.MessageProperties;
import com.zes.squad.gmh.service.MessageService;

/**
 * 短信服务需要做防刷处理
 * 
 * @author zhouyuhuan 2018年2月24日 下午11:44:03
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService {

    private static final String CACHE_KEY_AUTH_CODE_PREFIX = "_cache_key_auth_code_%s";

    private static final int    DEFAULT_VALID_MINUTES      = 2;

    @Autowired
    private CacheService        cacheService;
    @Autowired
    private UserMapper          userMapper;

    @Override
    public void sendAuthCode(String mobile) {
        UserPo po = userMapper.selectByMobile(mobile);
        LogicHelper.ensureEntityExist(po, "非系统用户,请更换手机号重试");
        boolean isCacheValid = cacheService.isValid();
        if (!isCacheValid) {
            throw new GmhException(ErrorCodeEnum.CACHE_EXCEPTION, "验证码服务不可用");
        }
        String cacheKey = String.format(CACHE_KEY_AUTH_CODE_PREFIX, mobile);
        String cacheAuthCode = cacheService.get(cacheKey);
        if (!Strings.isNullOrEmpty(cacheAuthCode)) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED,
                    "短信验证码" + DEFAULT_VALID_MINUTES + "分钟内只能获取一次,请稍后再试");
        }
        String authCode = SMSHelper.generateAuthCode(6);
        String content = MessageFormat.format(MessageProperties.get("auth.code.content"), authCode,
                DEFAULT_VALID_MINUTES);
        SMSHelper.sendMessage(mobile, content);
        //验证码有效期默认2分钟
        cacheService.put(cacheKey, authCode, CacheConsts.TWO_MINUTE);
    }

    @Override
    public void validateAuthCode(String mobile, String authCode) {
        UserPo po = userMapper.selectByMobile(mobile);
        LogicHelper.ensureEntityExist(po, "非系统用户,请更换手机号重试");
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
    }

}
