package com.example.bysj2020.utils;

import android.util.Log;

import com.example.bysj2020.BuildConfig;

/**
 * Author : wxz
 * Time   : 2020/02/13
 * Desc   : 日志打印工具
 */
public class LogUtil {

    private static final String TAG = "毕设=";
    public static final boolean isDebug = BuildConfig.DEBUG;

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(TAG + tag, msg);
        }
    }

    public static void e(String tag, Object msg) {
        if (isDebug && msg != null) {
            Log.e(TAG + tag, msg.toString());
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
        }
    }

    public static void e(Class cls, String msg) {
        if (isDebug) {
            Log.e(TAG + cls.getSimpleName(), msg);
        }
    }
}
