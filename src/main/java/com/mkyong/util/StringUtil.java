package com.mkyong.util;

/**
 * @author Michael.Wang
 * @date 2016/11/24
 */
public class StringUtil {
    public static boolean isEmptyOrNull(String str) {
        return str == null || "".equals(str);
    }

    public static String quote(String str) {
        return "\"".concat(str).concat("\"");
    }
}
