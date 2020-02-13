package com.example.bysj2020.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Author : wxz
 * Time   : 2020/02/13
 * Desc   : toast对话框
 */
public class ToastUtil {
    public static Toast toast;

    public static void setToast(Context context, String string) {
        if (toast == null) {
            toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        } else {
            toast.setText(string);
        }
        toast.show();
    }
}
