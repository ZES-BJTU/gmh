package com.zes.squad.gmh.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final String DEFAULT_DATE_PATTERN     = "yyyy-MM-dd";
    private static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String formatDate(Date date) {
        return format(date, DEFAULT_DATE_PATTERN);
    }

    public static String formatDateTime(Date date) {
        return format(date, DEFAULT_DATETIME_PATTERN);
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

}
