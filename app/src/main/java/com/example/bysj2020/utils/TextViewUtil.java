package com.example.bysj2020.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

/**
 * Author : wxz
 * Time   : 2020/02/13
 * Desc   : 修改字符串样式
 */
public class TextViewUtil {
    private SpannableStringBuilder style;
    private String parent;

    public TextViewUtil(String parent) {
        this.parent = parent;
        this.style = new SpannableStringBuilder(parent);
    }

    /**
     * 改变字符串中指定子串的文字颜色
     *
     * @param child
     * @param color
     * @return
     */
    public TextViewUtil setTextColor(String child, int color) {
        int start = parent.indexOf(child);
        int end = start + child.length();
        style.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 改变字符串中指定子串的文字大小
     *
     * @param child
     * @param size
     * @return
     */
    public TextViewUtil setTextSize(String child, int size) {
        int start = parent.indexOf(child);
        int end = start + child.length();
        style.setSpan(new AbsoluteSizeSpan(size, true), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    public SpannableStringBuilder create() {
        return style;
    }

    public static String ToDBC(String input) {
        if (TextUtils.isEmpty(input)) {
            return "";
        } else {
            char[] c = input.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if (c[i] == 12288) {
                    //全角空格为12288，半角空格为32
                    c[i] = (char) 32;
                    continue;
                }
                if (c[i] > 65280 && c[i] < 65375) {
                    //其他字符半角（33-126）与全角（65281-65374）的对应关系是：均相差65248
                    c[i] = (char) (c[i] - 65248);
                }
            }
            return new String(c);
        }
    }
}
