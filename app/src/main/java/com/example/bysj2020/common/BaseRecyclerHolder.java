package com.example.bysj2020.common;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bysj2020.R;
import com.example.bysj2020.global.Config;
import com.example.bysj2020.utils.DensityUtil;
import com.example.bysj2020.utils.TextViewUtil;

/**
 * Author : wxz
 * Time   : 2020/02/14
 * Desc   : RecyclerView基本构造者
 */
public class BaseRecyclerHolder extends RecyclerView.ViewHolder {

    SparseArray<View> mView;
    private Context context;

    public BaseRecyclerHolder(View itemView, int itemLength, Context context) {
        super(itemView);
        this.context = context;
        this.mView = new SparseArray<>(itemLength);
    }

    public static BaseRecyclerHolder getBaseRecyclerHolder(View itemView, int itemLength, Context context) {
        return new BaseRecyclerHolder(itemView, itemLength, context);
    }

    /**
     * 通过id获取对应的控件，如果没有则加入views中
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mView.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mView.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置TextView控件的内容
     *
     * @param viewId
     * @param text
     */
    public void setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(TextViewUtil.ToDBC(text));
    }

    /**
     * 设置TextView控件的内容的颜色
     *
     * @param viewId
     * @param color
     */
    public void setTextColor(int viewId, String color) {
        TextView tv = getView(viewId);
        tv.setTextColor(Color.parseColor(color));
    }

    /**
     * 设置圆形图片
     *
     * @param viewId 控件id
     * @param url    图片地址
     */
    public void setCircleImageResource(int viewId, String url) {
        ImageView iv = getView(viewId);
        RequestOptions requestOptions = new RequestOptions().placeholder(Config.defaultResId).error(Config.defaultResId).transform(new GlideCircleTransform());
        Glide.with(context).load(url).apply(requestOptions).into(iv);
    }

    /**
     * 设置矩形图片
     *
     * @param viewId 控件id
     * @param url    图片地址
     */
    public void getRoundImageResource(int viewId, String url) {
        ImageView iv = getView(viewId);
        RequestOptions requestOptions = new RequestOptions().placeholder(R.mipmap.default_picture).error(R.mipmap.default_picture).transform(new GlideRoundTransform(5));
        Glide.with(context).load(url).apply(requestOptions).into(iv);
    }

    /**
     * 设置矩形图片
     *
     * @param viewId    控件id
     * @param url       图片地址
     * @param defaultId 默认图片地址
     */
    public void getRoundImageResource(int viewId, String url, int defaultId) {
        ImageView iv = getView(viewId);
        RequestOptions requestOptions = new RequestOptions().placeholder(defaultId).error(defaultId).transform(new GlideRoundTransform(5));
        Glide.with(context).load(url).apply(requestOptions).into(iv);
    }

    /**
     * 设置图片
     *
     * @param viewId 控件id
     * @param url    图片地址
     */
    public void setImageResource(int viewId, String url) {
        ImageView iv = getView(viewId);
        RequestOptions requestOptions = new RequestOptions().placeholder(Config.defaultResId).error(Config.defaultResId);
        Glide.with(context).load(url).apply(requestOptions).into(iv);
    }

    /**
     * 设置圆形图片
     *
     * @param viewId 控件id
     * @param url    图片地址
     * @param resId  默认图片地址
     */
    public void setCircleImageResource(int viewId, String url, int resId) {
        ImageView iv = getView(viewId);
        RequestOptions requestOptions = new RequestOptions().placeholder(resId).error(resId).transform(new GlideCircleTransform());
        Glide.with(context).load(url).apply(requestOptions).into(iv);
    }

    /**
     * 设置圆形图片
     *
     * @param viewId 控件id
     * @param resId  图片地址
     */
    public void setCircleImageResource(int viewId, int resId) {
        ImageView iv = getView(viewId);
        RequestOptions requestOptions = new RequestOptions().transform(new GlideCircleTransform());
        Glide.with(context).load(resId).apply(requestOptions).into(iv);
    }

    /**
     * 设置图片
     *
     * @param viewId 控件id
     * @param resId  图片地址
     */
    public void setImageResource(int viewId, int resId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
    }

    /**
     * 设置Margin边距
     *
     * @param horizontal 横向 （左右）
     * @param vertical   纵向 （上下）
     */
    public void setMargin(int horizontal, int vertical) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int d1 = DensityUtil.dp2px(horizontal);
        int d2 = DensityUtil.dp2px(vertical);
        layoutParams.setMargins(d1, d2, d1, d2);//4个参数按顺序分别是左上右下
        this.itemView.setLayoutParams(layoutParams);
    }

    /**
     * 设置MarginTop边距
     *
     * @param top 上
     */
    public void setMarginTop(int top) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int d = DensityUtil.dp2px(top);
        layoutParams.setMargins(0, d, 0, 0);//4个参数按顺序分别是左上右下
        this.itemView.setLayoutParams(layoutParams);
    }

    /**
     * 设置MarginBottom边距
     *
     * @param bottom 下
     */
    public void setMarginBottom(int bottom) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int d = DensityUtil.dp2px(15);
        int d2 = DensityUtil.dp2px(20);
        int d3 = DensityUtil.dp2px(bottom);
        layoutParams.setMargins(d, d2, d, d3);//4个参数按顺序分别是左上右下
        this.itemView.setLayoutParams(layoutParams);
    }

    /**
     * 设置Margin边距
     *
     * @param left   左
     * @param top    上
     * @param right  右
     * @param bottom 下
     */
    public void setMargin(int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int d1 = DensityUtil.dp2px(left);
        int d2 = DensityUtil.dp2px(top);
        int d3 = DensityUtil.dp2px(right);
        int d4 = DensityUtil.dp2px(bottom);
        layoutParams.setMargins(d1, d2, d3, d4);//4个参数按顺序分别是左上右下
        this.itemView.setLayoutParams(layoutParams);
    }
}
