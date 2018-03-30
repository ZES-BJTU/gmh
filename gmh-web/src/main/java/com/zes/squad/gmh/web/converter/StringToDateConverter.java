package com.zes.squad.gmh.web.converter;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringToDateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        try {
            Long time = Long.valueOf(source);
            Date date = new Date(time);
            return date;
        } catch (Exception e) {
            log.error("日期格式错误", e);
            return null;
        }
    }

}
