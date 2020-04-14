package com.example.bysj2020.utils;

import java.util.regex.Pattern;

/**
 * Author : wxz
 * Time   : 2020/02/20
 * Desc   : 验证工具
 */
public class VerifyUtils {

    //手机号校验
    private static String phoneRex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
    //身份证校验
    private static String identityRex = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";

    /**
     * 验证手机号码
     *
     * @param phone
     * @return
     */
    public static boolean verifyPhone(String phone) {
        Pattern pattern = Pattern.compile(phoneRex);
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

    /**
     * 验证身份证
     *
     * @param identity
     * @return
     */
    public static boolean verifyIdentity(String identity) {
        Pattern pattern = Pattern.compile(identityRex);
        return pattern.matcher(identity).matches();
    }
}
