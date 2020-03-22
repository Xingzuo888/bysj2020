package com.example.bysj2020.global

import android.os.Environment
import com.example.bysj2020.R

/**
 *    Author : wxz
 *    Time   : 2020/02/13
 *    Desc   : 配置文件
 */
class Config {
    companion object {
        //默认图片资源
        const val defaultResId = R.mipmap.default_picture
        //缓存路径
        val CACHE = Environment.getExternalStorageDirectory().toString() + "/bysj2020/cache/"
        //手机号格式不正确
        const val PHONE_FORMAT_NOT_CORRECT = "手机号格式不正确"
        //密码格式不正确
        const val PASSWORD_FORMAT_INCORRECT = "密码格式不正确"
        //验证码已发送
        const val VERIFY_CODE = "验证码已发送"
    }
}