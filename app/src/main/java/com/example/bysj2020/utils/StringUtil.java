package com.example.bysj2020.utils;

/**
 * Author : wxz
 * Time   : 2020/02/13
 * Desc   :
 */
public class StringUtil {
    public StringUtil() {
    }

    public static boolean isEmpty(String var0) {
        return var0 == null || var0.length() == 0;
    }

    public static boolean isBlank(String var0) {
        return var0 == null || var0.trim().length() == 0;
    }
}
