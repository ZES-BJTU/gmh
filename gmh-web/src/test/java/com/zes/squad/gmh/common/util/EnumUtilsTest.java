package com.zes.squad.gmh.common.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.zes.squad.gmh.common.enums.GenderEnum;

public class EnumUtilsTest {
    
    @Test
    public void testGetDescByKey() {
        int key = 1;
        String desc = EnumUtils.getDescByKey(key, GenderEnum.class);
        assertEquals(GenderEnum.MALE.getDesc(), desc);
    }
    
    @Test
    public void testContainsKey() {
        int key = 0;
        boolean result = EnumUtils.containsKey(key, GenderEnum.class);
        assertTrue(result);
    }

}
