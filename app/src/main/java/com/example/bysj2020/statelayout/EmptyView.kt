package com.example.bysj2020.statelayout

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.bysj2020.R

/**
 *    Author : wxz
 *    Time   : 2020/02/15
 *    Desc   : 空布局界面
 */
class EmptyView() : ViewLoader() {
    private var mContext: Context? = null
    private var resourceId: Int = 0
    private var title: String? = null
    private var empty_title: TextView? = null
    private var empty_bottom_btn: TextView? = null
    private var ll_empty_icon: LinearLayout? = null
    private var llBottom: LinearLayout? = null
    private var bottomTip: TextView? = null
    private var callBack: EmptyViewClickBack? = null

    constructor(mContext: Context) : this() {
        this.mContext = mContext
    }

    private lateinit var mEmptyView: View

    override fun createView(): View {
        mEmptyView = LayoutInflater.from(mContext).inflate(R.layout.empty, null)
        if (resourceId != 0) {
            val icon: ImageView = mEmptyView.findViewById(R.id.empty_icon)
            icon.setImageResource(resourceId)
        }
        if (!TextUtils.isEmpty(title)) {
            var errorTitle: TextView = mEmptyView.findViewById(R.id.empty_title)
            errorTitle.text = title
        }
        llBottom = mEmptyView.findViewById(R.id.ll_bottom)
        bottomTip = mEmptyView.findViewById(R.id.empty_bottom_tip)
        empty_title = mEmptyView.findViewById(R.id.empty_title)
        empty_bottom_btn = mEmptyView.findViewById(R.id.empty_bottom_btn)
        ll_empty_icon = mEmptyView.findViewById(R.id.ll_empty_icon)
        return mEmptyView
    }

    fun setEmptyContent(content: String) {
        empty_title!!.text = content
    }

    fun initBottomView(trip: String, btnTxt: String, callBack: EmptyViewClickBack) {
        this.callBack = callBack
        var params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        params.setMargins(0, -420, 0, 0)
        ll_empty_icon?.layoutParams = params
        llBottom!!.visibility = View.VISIBLE
        bottomTip!!.text = trip
        empty_bottom_btn!!.text = btnTxt
        empty_bottom_btn!!.setOnClickListener {
            callBack.clickText()
        }
    }

    fun goneBottomView() {
        llBottom!!.visibility = View.GONE
    }

    /**
     * 回调接口
     */
    interface EmptyViewClickBack {
        fun clickText()
    }
}