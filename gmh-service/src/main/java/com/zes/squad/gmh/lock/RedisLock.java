package com.zes.squad.gmh.lock;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("redisLock")
public class RedisLock implements InitializingBean, DisposableBean {

    @Value("${redis.host}")
    private String         address;
    @Value("${redis.port}")
    private String         port;
    @Value("${redis.default.db}")
    private Integer        database;
    @Value("${redis.password}")
    private String         password;
    @Value("${redis.timeout}")
    private Integer        timeout;

    private RedissonClient redissonClient;

    public RedissonClient getRedissonClient() {
        if (redissonClient != null) {
            return redissonClient;
        }
        log.error("获取redisson客户端失败, redissonClient is null");
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + address + ":" + port).setDatabase(database)
                .setPassword(password).setConnectTimeout(timeout);
        redissonClient = Redisson.create(config);
        log.info(">>>>>生成redisson客户端成功");
    }

    @Override
    public void destroy() throws Exception {
        if (redissonClient != null && !redissonClient.isShutdown()) {
            redissonClient.shutdown();
            log.info(">>>>>销毁redisson客户端成功");
        }
    }

}
