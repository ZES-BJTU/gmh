package com.zes.squad.gmh.common.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.Test;

public class EncryptUtilsTest {

    @Test
    public void testMd5() {
        String input = "gmh" + "123456" + UUID.randomUUID().toString().replaceAll("-", "");
        String result = EncryptUtils.md5(input);
        assertNotNull(result);
        assertEquals(32, result.length());
    }

}
