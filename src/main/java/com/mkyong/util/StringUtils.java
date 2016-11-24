package com.mkyong.util;

/**
 * @author Michael.Wang
 * @date 2016/11/24
 */
public class StringUtils {
    public static boolean isEmptyOrNull(String str) {
        return str == null || "".equals(str);
    }
}
