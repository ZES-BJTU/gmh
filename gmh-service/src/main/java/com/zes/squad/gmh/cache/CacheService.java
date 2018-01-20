package com.zes.squad.gmh.cache;

import java.io.Serializable;
import java.util.List;

/**
 * 缓存接口
 * 
 * @author yuhuan.zhou 2018年1月15日 下午10:07:18
 */
public interface CacheService {

    /**
     * 缓存是否有效
     * 
     * @return
     */
    boolean isValid();

    /**
     * 存储数据
     * 
     * @param key
     * @param value
     */
    void put(String key, Serializable value);

    /**
     * 存储数据(带过期)
     * 
     * @param key
     * @param value
     * @param expireSecond
     */
    void put(String key, Serializable value, long expireSecond);

    /**
     * 存储列表对象
     * 
     * @param key
     * @param values
     */
    void putList(String key, List<Serializable> values);

    /**
     * 存储列表对象(带过期)
     * 
     * @param key
     * @param values
     * @param expireSecond
     */
    void putList(String key, List<Serializable> values, long expireSecond);

    /**
     * 获取单个值(字符串)
     * 
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 获取单个对象
     * 
     * @param key
     * @param classOfValue
     * @return
     */
    <V extends Serializable> V get(String key, Class<V> classOfValue);

    /**
     * 获取列表对象
     * 
     * @param key
     * @param classOfValue
     * @return
     */
    <V extends Serializable> List<V> getList(String key, Class<V> classOfValue);

    /**
     * 删除单个值
     * 
     * @param key
     */
    void delete(String key);

}
