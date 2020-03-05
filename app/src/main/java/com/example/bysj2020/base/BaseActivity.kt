package com.example.bysj2020.base

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleObserver
import com.example.bysj2020.R
import com.example.bysj2020.activity.Home
import com.example.bysj2020.event.BaseEvent
import com.example.bysj2020.statelayout.EmptyView
import com.example.bysj2020.statelayout.ErrorView
import com.example.bysj2020.statelayout.LoadHelper
import com.example.bysj2020.statelayout.LoadingView
import com.example.bysj2020.utils.ActivityManagerUtil
import com.gyf.immersionbar.ImmersionBar
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *    Author : wxz
 *    Time   : 2020/02/15
 *    Desc   : baseActivity
 */
abstract class BaseActivity : AppCompatActivity(), View.OnTouchListener, View.OnClickListener {
    private lateinit var content: View
    private lateinit var helper: LoadHelper
    var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        ImmersionBar.with(this).statusBarColor(R.color.green).statusBarDarkFont(true).init()
        ActivityManagerUtil.addDestroyActivity(this, javaClass.name)
        EventBus.getDefault().register(this)
        initViews()
        setViewClick()
    }

    override fun onDestroy() {
        ActivityManagerUtil.destroyActivity(javaClass.name)
        compositeDisposable.clear()
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> v!!.alpha = 0.5f
            MotionEvent.ACTION_UP -> v!!.alpha = 1.0f
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val extraMap = intent.getStringExtra("extraMap")
                if (null != extraMap) {
                    startActivity(Intent(this, Home::class.java))
                }
                hideKeyboard()
                finish()
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    /**
     * 空布局、网络异常布局、点击事件初始化
     */
    fun initStateLayout(click: LoadHelper.EmptyClickListener) {
        helper = LoadHelper.Builder(this)
            .setContentView(getContentView())
            .setEmptyView(getEmptyView())
            .setErrorView(getErrorView())
            .setLoadingView(getLoadingView())
            .build()
        helper.init()
        helper.addEmptyClickListener(click)
    }

    /**
     * 加入生命周期管理
     * @param observer
     */
    fun addLifecycle(observer: LifecycleObserver) {
        lifecycle.addObserver(observer)
    }

    /**
     * 标题透明
     */
    fun setTransToolbar() {
        toolbar.setBackgroundResource(R.color.transparent)
    }

    /**
     * 菜单栏
     */
    fun setMenu(resId: Int) {
        toolbar.inflateMenu(resId)
    }

    /**
     * 设置返回
     */
    fun setBack() {
        toolbar.title = ""
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * 设置返回箭头颜色
     * @param color 颜色
     */
    fun setBackColor(color: Int) {
        val upArrow = ContextCompat.getDrawable(this, R.drawable.ic_keyboard_arrow_left_black)
        upArrow?.setColorFilter(ContextCompat.getColor(this, color), PorterDuff.Mode.SRC_ATOP)
        supportActionBar?.setHomeAsUpIndicator(upArrow)
    }

    /**
     * 设置title
     * @param s title内容
     */
    fun setTitle(s: String) {
        toolbar_title.text = s
    }

    /**
     * 设置文字颜色
     * @param context
     * @param resId
     */
    fun setTitleColors(context: Context, resId: Int) {
        toolbar_title.setTextColor(ContextCompat.getColor(context, resId))
    }

    /**
     * 设置subtitle
     * @param s subtitle内容
     */
    fun setSubTitle(s: String) {
        toolbar.subtitle = s
    }

    /**
     * 设置右边文字
     * @param string
     */
    fun setRightText(string: String) {
        right_text.visibility = View.VISIBLE
        right_text.text = string
    }

    /**
     * 设置右边图标
     * @param resourceId
     */
    fun setRightIcon(resourceId: Int) {
        right_icon_lay.visibility = View.VISIBLE
        right_icon.setImageResource(resourceId)
    }

    /**
     * 设置右边第二个图标
     * @param resourceId
     */
    fun setRightIcons(resourceId: Int) {
        right_icons.visibility = View.VISIBLE
        right_icons.setImageResource(resourceId)
    }

    /**
     * 设置toolbar背景颜色
     * @param color
     */
    fun setToolbarColor(color: Int) {
        toolbar.setBackgroundColor(ContextCompat.getColor(this, color))
    }

    /**
     * toast提示
     * @param msg 提示信息
     */
    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    /**
     * 页面布局id
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 初始化
     */
    protected abstract fun initViews()

    /**
     * 设置点击事件
     */
    protected abstract fun setViewClick()

    /**
     * 空布局
     */
    private fun getEmptyView(): EmptyView {
        return EmptyView(this)
    }

    /**
     * 空布局
     */
    fun getCurrEmptyView(): EmptyView {
        return helper.getEmptyView()
    }

    /**
     * 网络错误布局
     */
    private fun getErrorView(): ErrorView {
        return ErrorView(this)
    }

    /**
     * 加载中布局
     */
    private fun getLoadingView(): LoadingView {
        return LoadingView(this)
    }

    /**
     * 设置内容视图布局
     */
    open fun getContentView(): View {
        return content
    }

    /**
     * 显示内容
     */
    fun showContent() {
        helper.showContent()
    }

    /**
     * 显示错误布局
     */
    fun showError() {
        helper.showError()
    }

    /**
     * 显示空布局
     */
    fun showEmpty() {
        helper.showEmpty()
    }

    /**
     * 显示加载中布局
     */
    fun showLoading() {
        helper.showLoading()
    }

    /**
     * 是否正常显示内容
     */
    fun isShowContent(): Boolean {
        return helper.isShowContent()
    }

    /**
     * 隐藏软键盘
     */
    fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 隐藏软键盘
     */
    fun hideKeyboard() {
        val view = currentFocus
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun baseEvent(event: BaseEvent) {

    }

    override fun onBackPressed() {
        val extraMap = intent.getStringExtra("extraMap")
        if (null == extraMap) {
            super.onBackPressed()
        } else {
            //应用外登录，接收通知
            startActivity(Intent(this,Home::class.java))
            finish()
        }
    }
}