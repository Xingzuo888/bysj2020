package com.example.bysj2020.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.bysj2020.R

/**
 *    Author : wxz
 *    Time   : 2020/02/13
 *    Desc   : 加载图片
 */
class LoadImageUtil {
    private var activity: Activity? = null
    private var fragment: Fragment? = null
    private var context: Context? = null
    private var manager: RequestManager? = null

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    constructor(activity: Activity) {
        this.activity = activity
        if (!activity.isDestroyed) {
            manager = Glide.with(activity)
        }
    }

    constructor(fragment: Fragment) {
        this.fragment = fragment
        if (null != fragment.activity) {
            manager = Glide.with(fragment.activity!!)
        }
    }

    constructor(context: Context) {
        this.context = context
        manager = Glide.with(context)
    }

    /**
     * 获取RequestManager对象
     */
    fun getManager(): RequestManager {
        return manager!!
    }

    /**
     * 加载圆形图片
     * @param view
     * @param url
     */
    fun loadImage(view: ImageView, url: String?) {
        val requestOptions = RequestOptions().placeholder(R.mipmap.default_picture).error(R.mipmap.default_picture)
        manager?.applyDefaultRequestOptions(requestOptions)?.load(url)?.into(view)
    }

    /**
     * 加载普通图片
     * @param view
     * @param url
     * @param resId
     */
    fun loadImage(view: ImageView, url: String?, resId: Int) {
        val requestOptions = RequestOptions().placeholder(resId).error(resId)
        manager?.applyDefaultRequestOptions(requestOptions)?.load(url)?.into(view)
    }

    /**
     * 加载圆形图片
     * @param view
     * @param url
     */
    fun loadCircularImage(view: ImageView, url: String?) {
        val requestOptions = RequestOptions().placeholder(R.mipmap.default_picture).error(R.mipmap.default_picture)
        manager?.applyDefaultRequestOptions(requestOptions)?.load(url)?.into(view)
    }

    /**
     * 加载圆形图片
     * @param view
     * @param url
     * @param resId
     */
    fun loadCircularImage(view: ImageView, url: String?, resId: Int) {
        val requestOptions = RequestOptions().placeholder(resId).error(resId)
        manager?.applyDefaultRequestOptions((requestOptions))?.load(url)?.into(view)
    }
}