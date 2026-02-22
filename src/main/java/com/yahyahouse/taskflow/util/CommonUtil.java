package com.yahyahouse.taskflow.util;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class CommonUtil {

    private CommonUtil() {
    }

    public static boolean hasText(String value) {
        return StringUtils.hasText(value);
    }

    public static String trimToNull(String value) {
        return hasText(value) ? value.trim() : null;
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return dateTime.format(java.time.format.DateTimeFormatter.ofPattern(pattern));
    }
    public static String formatDate(LocalDate dateTime) {
        String pattern = "yyyy-MM-dd";
        return dateTime.format(java.time.format.DateTimeFormatter.ofPattern(pattern));
    }
}
