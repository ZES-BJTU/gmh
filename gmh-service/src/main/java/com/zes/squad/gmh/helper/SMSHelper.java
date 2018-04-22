package com.zes.squad.gmh.helper;

import java.util.Objects;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.zes.squad.gmh.common.enums.SMSCodeEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.common.helper.LogicHelper;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.property.MessageProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SMSHelper {

    private static final String DEFAULT_TIME_OUT = "10000";

    public static String generateAuthCode(int length) {
        String digits = "0123456789abcdefjhijklmnopqrstuvwxyz";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * (digits.length()));
            builder.append(digits.substring(index, index + 1));
        }
        return builder.toString();
    }

    public static boolean sendMessage(String mobile, String templateCode, String params) {
        LogicHelper.ensureParameterExist(mobile, "手机号为空");
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", DEFAULT_TIME_OUT);
        System.setProperty("sun.net.client.defaultReadTimeout", DEFAULT_TIME_OUT);
        //初始化ascClient需要的几个参数
        //短信API产品名称(短信产品名固定，无需修改)
        final String product = "Dysmsapi";
        //短信API产品域名(接口地址固定，无需修改)
        final String domain = "dysmsapi.aliyuncs.com";
        //替换成注册AK
        final String accessKeyId = MessageProperties.get("accessKeyId");
        final String accessKeySecret = MessageProperties.get("accessKeySecret");
        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e) {
            log.error("发送短信异常", e);
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
        request.setPhoneNumbers(mobile);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(MessageProperties.get("sign"));
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam(params);
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            if (sendSmsResponse == null) {
                log.error("发送短信失败, mobile is {}, response is null");
                throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_FAILED, "短信服务不可用");
            }
            if (sendSmsResponse.getCode() == null) {
                log.error("发送短信失败, mobile is {}, code is null");
                throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_FAILED, "短信服务不可用");
            }
            if (!Objects.equals(sendSmsResponse.getCode(), "OK")) {
                log.error("发送短信失败, mobile is {}, code is {}, message is {}", sendSmsResponse.getCode(),
                        sendSmsResponse.getMessage());
                if (EnumUtils.containsKey(sendSmsResponse.getCode(), SMSCodeEnum.class)) {
                    throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_FAILED,
                            EnumUtils.getDescByKey(sendSmsResponse.getCode(), SMSCodeEnum.class));
                } else {
                    throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_FAILED, "短信服务不可用");
                }
            }
            return true;
        } catch (Exception e) {
            log.error("发送短信异常", e);
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_FAILED, "短信服务不可用");
        }
    }

}
