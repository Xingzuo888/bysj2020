package com.example.bysj2020.statelayout

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.bysj2020.R

/**
 *    Author : wxz
 *    Time   : 2020/02/15
 *    Desc   : 错误布局界面
 */
class ErrorView(val mContext: Context, val resourceId: Int, val title: String,val content:String) : ViewLoader() {
    constructor(mContext: Context) : this(mContext, 0, "","")

    private lateinit var mErrorView: View
    override fun createView(): View {
        mErrorView = LayoutInflater.from(mContext).inflate(R.layout.error, null)
        if (resourceId != 0) {
            val icon: ImageView = mErrorView.findViewById(R.id.error_icon)
            icon.setImageResource(resourceId)
        }
        if (!TextUtils.isEmpty(title)) {
            var errorTitle: TextView = mErrorView.findViewById(R.id.error_title)
            errorTitle.text = title
        }
        if (!TextUtils.isEmpty(content)) {
            var errorContent: TextView = mErrorView.findViewById(R.id.error_content)
            errorContent.text = content
        }
        return mErrorView
    }

}