package com.zes.squad.gmh.common.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class GmhExceptionTest {

    @Test
    public void test() {
        GmhException exception = new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION);
        assertNotNull(exception.getMessage());
        assertEquals(ErrorCodeEnum.BUSINESS_EXCEPTION.getMessage(), exception.getMessage());
    }

}
