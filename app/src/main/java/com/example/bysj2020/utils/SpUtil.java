package com.example.bysj2020.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class SpUtil {

    private static SharedPreferences getSP(Context context) {
        SharedPreferences sp = context.getSharedPreferences("bysj_common_data", 0);
        return sp;
    }

    /**
     * 保存数据到sharedPreference
     *
     * @param context
     * @param key
     * @param value
     */
    public static void Save(Context context, String key, Object value) {
        SharedPreferences sharedPreferences = getSP(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof String) {
            editor.putString(key, value.toString());
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Set) {
            editor.putStringSet(key, (Set<String>) value);
        }
        editor.commit();
    }

    /**
     * SharedPreferences取出数据
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static Object Obtain(Context context, String key, Object defaultValue) {
        SharedPreferences sharedPreferences = getSP(context);
        if (defaultValue instanceof String) {
            return sharedPreferences.getString(key, defaultValue.toString());
        } else if (defaultValue instanceof Boolean) {
            return sharedPreferences.getBoolean(key, (Boolean) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return sharedPreferences.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Float) {
            return sharedPreferences.getFloat(key, (Float) defaultValue);
        } else if (defaultValue instanceof Long) {
            return sharedPreferences.getLong(key, (Long) defaultValue);
        } else if (defaultValue instanceof Set) {
            return sharedPreferences.getStringSet(key, (Set<String>) defaultValue);
        }
        return null;
    }
}
