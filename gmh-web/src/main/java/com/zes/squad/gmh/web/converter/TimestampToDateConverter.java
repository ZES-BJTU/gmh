package com.zes.squad.gmh.web.converter;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class TimestampToDateConverter implements Converter<Long, Date> {

    @Override
    public Date convert(Long source) {
        if (source == null) {
            return null;
        }
        Date date = new Date(source);
        return date;
    }

}
