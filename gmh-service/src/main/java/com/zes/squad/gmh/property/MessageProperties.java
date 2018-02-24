package com.zes.squad.gmh.property;

import java.io.InputStreamReader;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageProperties {

    private static Properties properties;

    static {
        properties = new Properties();
        InputStreamReader streamReader;
        try {
            streamReader = new InputStreamReader(
                    Thread.currentThread().getContextClassLoader().getResourceAsStream("message.properties"), "UTF-8");
            properties.load(streamReader);
        } catch (Exception e) {
            log.error("读取message.properties文件异常", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

}
