package com.zes.squad.gmh.common.helper;

import static com.zes.squad.gmh.common.exception.ErrorCodeEnum.BUSINESS_EXCEPTION_CONDITION_NOT_SUPPORTED;
import static com.zes.squad.gmh.common.exception.ErrorCodeEnum.BUSINESS_EXCEPTION_EMPTY_COLLECTION;
import static com.zes.squad.gmh.common.exception.ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND;
import static com.zes.squad.gmh.common.exception.ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETER;

import java.util.Collection;
import java.util.Map;

import com.zes.squad.gmh.common.exception.GmhException;

/**
 * 逻辑帮助类
 * 
 * @author yuhuan.zhou 2018年1月15日 下午10:49:15
 */
public class LogicHelper {

    public static boolean checkParameterExist(Object parameter) {
        if (parameter == null) {
            return false;
        }
        if (parameter instanceof String && parameter.toString().isEmpty()) {
            return false;
        }
        return true;
    }

    public static void ensureParameterExist(Object parameter, String message) {
        if (parameter == null) {
            throw new GmhException(BUSINESS_EXCEPTION_INVALID_PARAMETER, message);
        }
        if (parameter instanceof String && parameter.toString().isEmpty()) {
            throw new GmhException(BUSINESS_EXCEPTION_INVALID_PARAMETER, message);
        }
    }

    public static void ensureParameterNotExist(Object parameter, String message) {
        if (parameter != null) {
            throw new GmhException(BUSINESS_EXCEPTION_INVALID_PARAMETER, message);
        }
        if (parameter instanceof String && !parameter.toString().isEmpty()) {
            throw new GmhException(BUSINESS_EXCEPTION_INVALID_PARAMETER, message);
        }
    }

    public static void ensureParameterValid(boolean condition, String message) {
        ensureConditionSatisfied(condition, message);
    }

    public static void ensureAttributeExist(Object attribute, String message) {
        if (attribute == null) {
            throw new GmhException(BUSINESS_EXCEPTION_ENTITY_NOT_FOUND, message);
        }
    }

    public static void ensureEntityExist(Object entity, String message) {
        if (entity == null) {
            throw new GmhException(BUSINESS_EXCEPTION_ENTITY_NOT_FOUND, message);
        }
    }

    public static void ensureEntityNotExist(Object entity, String message) {
        if (entity != null) {
            throw new GmhException(BUSINESS_EXCEPTION_ENTITY_NOT_FOUND, message);
        }
    }

    public static void ensureArrayNotEmpty(Object[] objects, String message) {
        if (objects == null || objects.length == 0) {
            throw new GmhException(BUSINESS_EXCEPTION_EMPTY_COLLECTION, message);
        }
    }

    public static <V> void ensureCollectionEmpty(Collection<V> objects, String message) {
        if (objects != null && !objects.isEmpty()) {
            throw new GmhException(BUSINESS_EXCEPTION_EMPTY_COLLECTION, message);
        }
    }
    
    public static <V> void ensureCollectionNotEmpty(Collection<V> objects, String message) {
        if (objects == null || objects.isEmpty()) {
            throw new GmhException(BUSINESS_EXCEPTION_EMPTY_COLLECTION, message);
        }
    }

    public static <K, V> void ensureMapNotEmpty(Map<K, V> map, String message) {
        if (map == null || map.isEmpty()) {
            throw new GmhException(BUSINESS_EXCEPTION_EMPTY_COLLECTION, message);
        }
    }

    public static void ensureConditionSatisfied(boolean condition, String message) {
        if (!condition) {
            throw new GmhException(BUSINESS_EXCEPTION_CONDITION_NOT_SUPPORTED, message);
        }
    }

}
