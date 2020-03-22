package com.example.bysj2020.utils;

import java.util.regex.Pattern;

/**
 * Author : wxz
 * Time   : 2020/02/20
 * Desc   : 验证工具
 */
public class VerifyUtils {

    private static String rex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";

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
