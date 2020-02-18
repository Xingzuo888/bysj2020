package com.example.bysj2020.statelayout

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.bysj2020.R

/**
 *    Author : wxz
 *    Time   : 2020/02/15
 *    Desc   : 加载界面
 */
class LoadingView(private val mContext: Context, private val title: String) : ViewLoader() {
    constructor(mContext: Context) : this(mContext, "")

    private lateinit var mLoadingView: View
    override fun createView(): View {
        mLoadingView = LayoutInflater.from(mContext).inflate(R.layout.loading, null)
        if (!TextUtils.isEmpty(title)) {
            val errorTitle: TextView = mLoadingView.findViewById(R.id.loading_title)
            errorTitle.text = title
        }
        return mLoadingView
    }
}