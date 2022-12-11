package com.delphian.bush.util;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static LocalDateTime nowFormatted() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.parse(localDateTime.format(dateTimeFormatter), dateTimeFormatter);
    }

    public static LocalDateTime parse(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.parse(
                ZonedDateTime.parse(date).toLocalDateTime().format(dateTimeFormatter),
                dateTimeFormatter);
    }

}
