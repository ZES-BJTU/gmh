package com.zes.squad.gmh.common.util;

import java.security.MessageDigest;

import lombok.extern.slf4j.Slf4j;

/**
 * 加密工具类
 * 
 * @author yuhuan.zhou 2018年1月14日 下午8:27:42
 */
@Slf4j
public class EncryptUtils {

    private static final String DEFAULT_ENCRYPT_ALGORITHM = "MD5";

    private static final char[] hexDigits                 = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
            'B', 'C', 'D', 'E', 'F' };

    public static String md5(String input) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(DEFAULT_ENCRYPT_ALGORITHM);
            byte[] bytes = digest.digest(input.getBytes("UTF-8"));
            return byteArrayToString(bytes);
        } catch (Exception e) {
            log.error("使用md5加密信息异常", e);
            return null;
        }
    }

    private static String byteArrayToString(byte[] bytes) {
        char[] cs = new char[bytes.length * 2];
        int i = 0;
        for (byte b : bytes) {
            cs[i++] = hexDigits[(b >>> 4) & 0xf];
            cs[i++] = hexDigits[b & 0xf];
        }
        return new String(cs);
    }

}
