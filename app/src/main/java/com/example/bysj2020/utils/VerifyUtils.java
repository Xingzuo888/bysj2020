package com.example.bysj2020.utils;

import java.util.regex.Pattern;

/**
 * Author : wxz
 * Time   : 2020/02/20
 * Desc   : 验证工具
 */
public class VerifyUtils {

    private static String rex = "^1\\d{10}$";

    /**
     * 验证手机号码
     *
     * @param phone
     * @return
     */
    public static boolean verifyPhone(String phone) {
        Pattern pattern = Pattern.compile(rex);
        return pattern.matcher(phone).matches();
    }

    /**
     * 验证密码
     *
     * @param pwd
     * @return
     */
    public static boolean verifyPassword(String pwd) {
        return StringUtil.isEmpty(pwd);
    }
}
