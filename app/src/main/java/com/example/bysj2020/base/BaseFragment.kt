package com.example.bysj2020.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import com.example.bysj2020.statelayout.EmptyView
import com.example.bysj2020.statelayout.ErrorView
import com.example.bysj2020.statelayout.LoadHelper
import com.example.bysj2020.statelayout.LoadingView
import io.reactivex.disposables.CompositeDisposable

/**
 *    Author : wxz
 *    Time   : 2020/02/16
 *    Desc   : BaseFragment
 */
abstract class BaseFragment : Fragment(), View.OnTouchListener, View.OnClickListener {
    /**
     * 控件是否初始化完成
     */
    private var isViewInitiated: Boolean = false
    /**
     * 页面是否显示
     */
    private var isVisibleToUser: Boolean = false
    /**
     * 数据是否加载
     */
    var isDataInitiated: Boolean = false
    private var mActivity: AppCompatActivity? = null
    private var rootView: View? = null
    private var helper: LoadHelper? = null
    var compositeDisposable = CompositeDisposable()

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isResumed) {
            onResume()
        }
        this.isVisibleToUser = isVisibleToUser
        prepareFetchData(false)
    }

    private fun prepareFetchData(forceUpdate: Boolean) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            loadData()
            isDataInitiated = true
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mActivity = context as AppCompatActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(getLayoutId(), container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isViewInitiated = true
        prepareFetchData(false)
        setViewClick()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> v?.alpha = 0.5f
            MotionEvent.ACTION_UP -> v?.alpha = 1.0f
        }
        return false
    }

    /**
     * 加入生命周期管理
     */
    fun addLifecycle(observer: LifecycleObserver) {
        lifecycle.addObserver(observer)
    }

    /**
     * 空布局、网络异常布局、点击事件初始化
     */
    fun initStateLayout(click: LoadHelper.EmptyClickListener) {
        helper = mActivity?.let {
            LoadHelper.Builder(it)
                    .setContentView(getContentView())
                    .setEmptyView(getEmptyView())
                    .setErrorView(getErrorView())
                    .setLoadingView(getLoadingView())
                    .build()
        }
        helper?.init()
        helper?.addEmptyClickListener(click)
    }

    /**
     * 设置内容视图布局
     */
    open fun getContentView(): View? {
        return if (rootView is ViewGroup) {
            (rootView as ViewGroup).getChildAt(0)
        } else {
            rootView
        }
    }

    /**
     * 空布局
     */
    private fun getEmptyView(): EmptyView {
        return EmptyView(mActivity!!)
    }

    /**
     * 空布局
     */
    fun getCurrEmptyView(): EmptyView {
        return helper!!.getEmptyView()
    }

    /**
     * 网络错误布局
     */
    private fun getErrorView(): ErrorView {
        return ErrorView(mActivity!!)
    }

    /**
     * 加载中布局
     */
    private fun getLoadingView(): LoadingView {
        return LoadingView(mActivity!!)
    }

    /**
     * 显示内容布局
     */
    fun showContent() {
        helper?.showContent()
    }

    /**
     * 显示错误布局
     */
    fun showError() {
        helper?.showError()
    }

    /**
     * 显示错误布局
     */
    fun showError(title:String,content:String) {
        helper?.showError(title,content)
    }

    /**
     * 显示空布局
     */
    fun showEmpty() {
        helper?.showEmpty()
    }

    /**
     * 显示加载中布局
     */
    fun showLoading() {
        helper?.showLoading()
    }

    /**
     * 是否正常显示内容
     */
    fun isShowContent(): Boolean {
        return helper!!.isShowContent()
    }

    /**
     * toast提示
     * @param msg 提示内容
     */
    fun showToast(msg: String) {
        if (null != mActivity) {
            Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 加载数据
     */
    abstract fun loadData()

    /**
     * 设置监听
     */
    abstract fun setViewClick()

    /**
     * 页面布局id
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 视图初始化
     */
    protected abstract fun initViews()

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    /**
     * 隐藏软键盘
     */
    fun hideKeyboard(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 隐藏软键盘
     */
    fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity?.window?.decorView?.windowToken, 0)
    }
}