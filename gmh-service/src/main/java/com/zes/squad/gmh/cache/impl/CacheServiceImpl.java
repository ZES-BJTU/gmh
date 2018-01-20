package com.zes.squad.gmh.cache.impl;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zes.squad.gmh.cache.CacheService;
import com.zes.squad.gmh.common.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("cacheService")
public class CacheServiceImpl implements CacheService {

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public boolean isValid() {
        try {
            redisTemplate.opsForValue().get("gmh");
            return true;
        } catch (Exception e) {
            log.error("缓存不可用", e);
            return false;
        }
    }

    @Override
    public void put(String key, Serializable value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void put(String key, Serializable value, long expireSecond) {
        redisTemplate.opsForValue().set(key, value, expireSecond, TimeUnit.SECONDS);
    }

    @Override
    public void putList(String key, List<Serializable> values) {
        String jsonValue = JsonUtils.toJson(values);
        redisTemplate.opsForValue().set(key, jsonValue);
    }

    @Override
    public void putList(String key, List<Serializable> values, long expireSecond) {
        String jsonValue = JsonUtils.toJson(values);
        redisTemplate.opsForValue().set(key, jsonValue, expireSecond, TimeUnit.SECONDS);
    }

    @Override
    public String get(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    @Override
    public <V extends Serializable> V get(String key, Class<V> classOfValue) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        V v = classOfValue.cast(value);
        return v;
    }

    @Override
    public <V extends Serializable> List<V> getList(String key, Class<V> classOfValue) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        String json = value.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<V> values = objectMapper.readValue(json, new TypeReference<List<V>>() {});
            return values;
        } catch (Exception e) {
            log.error("从缓存获取列表对象异常", e);
            return null;
        }
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

}
