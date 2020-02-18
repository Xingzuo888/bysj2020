package com.example.bysj2020.Interface;

import android.view.View;

/**
 * Author : wxz
 * Time   : 2020/02/15
 * Desc   :点击事件接口
 */
public interface ItemClick<T> {

    /**
     * 点击事件接口回调
     * @param view
     * @param t
     * @param position
     */
    void onItemClick(View view, T t, int position);
}
