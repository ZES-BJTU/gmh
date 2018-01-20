package com.zes.squad.gmh.common.util;

import java.util.Objects;

import org.apache.commons.beanutils.PropertyUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 枚举工具类
 * 
 * @author yuhuan.zhou 2018年1月14日 下午8:42:25
 */
@Slf4j
public class EnumUtils {

    public static <K, T> String getDescByKey(K key, Class<T> enumClass) {
        if (!enumClass.isEnum()) {
            throw new IllegalArgumentException(enumClass.getName() + " is not an enum!");
        }
        T[] enumConsts = enumClass.getEnumConstants();
        try {
            Object enumKey = null;
            Object enumDesc = null;
            for (T ec : enumConsts) {
                enumKey = PropertyUtils.getProperty(ec, "key");
                enumDesc = PropertyUtils.getProperty(ec, "desc");
                if (Objects.equals(key, enumKey)) {
                    return enumDesc == null ? null : enumDesc.toString();
                }
            }
        } catch (Exception e) {
            log.error("根据枚举key:{}获取枚举描述异常", key, e);
        }
        return null;
    }

    public static <K, T> boolean containsKey(K key, Class<T> enumClass) {
        if (!enumClass.isEnum()) {
            throw new IllegalArgumentException(enumClass.getName() + " is not an enum!");
        }
        T[] enumConsts = enumClass.getEnumConstants();
        try {
            Object enumKey = null;
            for (T ec : enumConsts) {
                enumKey = PropertyUtils.getProperty(ec, "key");
                if (Objects.equals(key, enumKey)) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("根据枚举key:{}获取枚举描述异常", key, e);
        }
        return false;
    }

}
