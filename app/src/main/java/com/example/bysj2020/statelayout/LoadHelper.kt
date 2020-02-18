package com.example.bysj2020.statelayout

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.example.bysj2020.R

/**
 *    Author : wxz
 *    Time   : 2020/02/15
 *    Desc   : 加载布局助手
 */
class LoadHelper(var builder: Builder) : View.OnClickListener {
    private var context: Context = builder.context
    private var contentView: View = builder.contentView
    private var emptyView: View = builder.emptyView
    private var emptyViewLoader: EmptyView = builder.emptyViewLoader
    private var errorView: View = builder.errorView
    private var loadingView: View = builder.loadingView
    private var isShowContent: Boolean = true
    private var tipEventView: TextView? = null
    private var clickListener: EmptyClickListener? = null
    private var tipClickListener: TipEventClickListener? = null

    fun init() {
        load()
        showLoading()
    }

    fun load() {
        var parent: ViewGroup = contentView.parent as ViewGroup
        //获取当前view的位置
        var index: Int = parent.indexOfChild(contentView)
        var targetParams = contentView.layoutParams
        parent.removeView(contentView)
        val emptyContainer: ViewGroup
        if (targetParams is ViewGroup.MarginLayoutParams) {
            emptyContainer = FrameLayout(context)
            emptyContainer.id = contentView.id
            contentView.id = -1
            val layoutParams: ViewGroup.MarginLayoutParams = targetParams
            emptyContainer.run {
                setLayoutParams(layoutParams)
            }
            parent.addView(emptyContainer, index, layoutParams)
        } else {
            emptyContainer = FrameLayout(context)
            emptyContainer.layoutParams = targetParams
            parent.addView(emptyContainer, index, targetParams)
        }
        val params = ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.MATCH_PARENT)
        emptyContainer.addView(contentView, params)
        emptyContainer.addView(emptyView, params)
        emptyContainer.addView(errorView, params)
        emptyContainer.addView(loadingView, params)
        tipEventView = emptyView.findViewById(R.id.empty_bottom_btn)
        tipEventView?.setOnClickListener(this)
        emptyView.setOnClickListener(this)
        errorView.setOnClickListener(this)
    }

    /**
     * 显示内容布局
     */
    fun showContent() {
        contentView.visibility = View.VISIBLE
        emptyView.visibility = View.GONE
        errorView.visibility = View.GONE
        loadingView.visibility = View.GONE
        isShowContent = true
    }

    /**
     * 显示空布局
     */
    fun showEmpty() {
        contentView.visibility = View.GONE
        emptyView.visibility = View.VISIBLE
        errorView.visibility = View.GONE
        loadingView.visibility = View.GONE
        isShowContent = false
    }

    /**
     * 显示错误布局
     */
    fun showError() {
        contentView.visibility = View.GONE
        emptyView.visibility = View.GONE
        errorView.visibility = View.VISIBLE
        loadingView.visibility = View.GONE
        isShowContent = false
    }

    /**
     * 显示加载布局
     */
    fun showLoading() {
        contentView.visibility = View.GONE
        emptyView.visibility = View.GONE
        errorView.visibility = View.GONE
        loadingView.visibility = View.VISIBLE
        isShowContent = false
    }

    /**
     * 是否正常显示内容
     * @return boolean
     */
    fun isShowContent(): Boolean {
        return isShowContent
    }

    /**
     * 获取空布局
     * @return EmptyView
     */
    fun getEmptyView(): EmptyView {
        return emptyViewLoader
    }

    /**
     * 布局点击事件
     */
    override fun onClick(v: View?) {
        if (v?.id == R.id.empty_bottom_btn) {
            tipClickListener?.myClick()
        } else {
            showLoading()
            clickListener?.reload()
        }
    }

    /**
     * 设置监听
     * @param clickListener
     * @return
     */
    fun addEmptyClickListener(clickListener: EmptyClickListener) {
        this.clickListener = clickListener
    }

    /**
     * 设置监听
     * @param clickListener
     * @return
     */
    fun addTipClickListener(clickListener: TipEventClickListener) {
        this.tipClickListener = clickListener
        this.clickListener = null
    }

    /**
     * 构造类
     */
    class Builder(context: Context) {
        lateinit var contentView: View
        lateinit var emptyView: View
        lateinit var errorView: View
        lateinit var loadingView: View
        lateinit var emptyViewLoader: EmptyView
        var context: Context = context

        fun setContentView(contentView: View?): Builder {
            this.contentView = contentView!!
            return this
        }

        fun setEmptyView(emptyView: ViewLoader): Builder {
            this.emptyViewLoader = emptyView as EmptyView
            this.emptyView = emptyView.getView()
            return this
        }

        fun setLoadingView(loadingView: ViewLoader): Builder {
            this.loadingView = loadingView.getView()
            return this
        }

        fun setErrorView(errorView: ViewLoader): Builder {
            this.errorView = errorView.getView()
            return this
        }

        fun build(): LoadHelper {
            return LoadHelper((this))
        }
    }

    /**
     * 接口回调
     */
    interface EmptyClickListener {
        fun reload()
    }

    /**
     * 接口回调
     */
    interface TipEventClickListener {
        fun myClick()
    }
}