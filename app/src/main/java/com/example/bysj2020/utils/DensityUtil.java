package com.example.bysj2020.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Author : wxz
 * Time   : 2020/02/11
 * Desc   : 分辨率像素转换
 */
public class DensityUtil {
    float density;

    public DensityUtil() {
        density = Resources.getSystem().getDisplayMetrics().density;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dpValue
     * @return
     */
    public static int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param pxValue
     * @return
     */
    public static float px2dp(float pxValue) {
        return pxValue / Resources.getSystem().getDisplayMetrics().density;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dpValue
     * @return
     */
    public int dip2px(float dpValue) {
        return (int) (0.5f + dpValue * dpValue);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param pxValue
     * @return
     */
    public float px2dip(float pxValue) {
        return pxValue / density;
    }

    /**
     * 获取屏幕密度
     *
     * @param mContext
     * @return
     */
    public static float getDensity(Context mContext) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = mContext.getResources().getDisplayMetrics();
        float density = dm.density;
        return density;
    }
}
