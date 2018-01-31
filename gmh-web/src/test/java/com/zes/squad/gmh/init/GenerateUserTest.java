package com.zes.squad.gmh.init;

import java.util.UUID;

import org.junit.Test;

import com.zes.squad.gmh.common.util.EncryptUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenerateUserTest {

    @Test
    public void testGenerateUser() {
        String account = "1120726720@qq.com";
        String salt = UUID.randomUUID().toString().replaceAll("-", "");
        log.info("salt is {}", salt);
        String password = "123456";
        String encryptPassword = EncryptUtils.md5(account + salt + password);
        log.info("password is {}", encryptPassword);
    }

}
