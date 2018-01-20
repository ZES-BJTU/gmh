package com.zes.squad.gmh.web.helper;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckHelperTest {
    
    @Test
    public void testIsValidMobile() {
        String mobile = "1234567890";
        boolean checkResult = CheckHelper.isValidMobile(mobile);
        assertTrue(!checkResult);
        mobile = "123456789011";
        checkResult = CheckHelper.isValidMobile(mobile);
        assertTrue(!checkResult);
        mobile = "28811439837";
        checkResult = CheckHelper.isValidMobile(mobile);
        assertTrue(!checkResult);
        mobile = "12345678901";
        checkResult = CheckHelper.isValidMobile(mobile);
        assertTrue(checkResult);
    }

}
