package com.zes.squad.gmh.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zes.squad.gmh.service.MessageService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/base.xml" })
public class MessageServiceImplTest {

    @Autowired
    private MessageService messageService;

    @Test
    public void testSendAuthCode() {
        String mobile = "15910946435";
        messageService.sendAuthCode(mobile);
    }
    
    @Test
    public void testValidateAuthCode() {
        String mobile = "15910946435";
        String authCode = "1lcnu8";
        messageService.validateAuthCode(mobile, authCode);
    }

}
