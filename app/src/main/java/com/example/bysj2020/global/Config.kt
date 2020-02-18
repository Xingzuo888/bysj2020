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
    }
}