package com.example.bysj2020.fragment

import android.os.CountDownTimer
import android.view.View
import androidx.core.content.ContextCompat
import com.example.bysj2020.R
import com.example.bysj2020.adapter.FHomeRecyclerViewAdapter
import com.example.bysj2020.base.BaseFragment
import com.example.bysj2020.statelayout.LoadHelper
import com.example.bysj2020.utils.ToastUtil
import com.stx.xhb.androidx.XBanner
import kotlinx.android.synthetic.main.fragment_home.*

/**
 *    Author : wxz
 *    Time   : 2020/02/23
 *    Desc   : 首页fragment
 */
class Home : BaseFragment() {

    private var adapter: FHomeRecyclerViewAdapter? = null
    private var timer: CountDownTimer? = null //达到20秒后，触发界面显示，则自动刷新
    private var canRefresh: Boolean = false
    private var isLoading = false//是否正在加载数据,用于防止加载中时操作崩溃

    companion object {
        fun newInstance(): Home {
            return Home()
        }
    }

    override fun loadData() {
        canRefresh = true
//        showLoading()
        getDataList()
    }

    override fun setViewClick() {
        f_home_hotCity1_lay.setOnClickListener(this)
        f_home_hotCity2_lay.setOnClickListener(this)
        f_home_hotCity3_lay.setOnClickListener(this)
        f_home_hotCity4_lay.setOnClickListener(this)
        f_home_hotCity5_lay.setOnClickListener(this)
        search_lay.setOnClickListener(this)
        f_home_moreCity_tv.setOnClickListener(this)
        f_home_moreViewPoint_tv.setOnClickListener(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initViews() {
        initStateLayout(object : LoadHelper.EmptyClickListener {
            override fun reload() {
                getDataList()
            }

        })
        //初始化刷新
        f_home_smartRefreshLayout.setOnRefreshListener {
            canRefresh = true
            getDataList()
        }
        f_home_smartRefreshLayout.setDisableContentWhenRefresh(true)
        f_home_smartRefreshLayout.setDisableContentWhenLoading(true)
        f_home_smartRefreshLayout.setEnableLoadMore(false)
        //初始化轮播图
//        f_home_xBanner.setBannerData()
        f_home_xBanner.setOnItemClickListener { xBanner: XBanner, any: Any, view: View, i: Int ->
            ToastUtil.setToast(activity, "")
        }
    }

    /**
     * 获取数据
     */
    fun getDataList() {
        showContent()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.f_home_hotCity1_lay -> {
                showLine(1)
            }
            R.id.f_home_hotCity2_lay -> {
                showLine(2)
            }
            R.id.f_home_hotCity3_lay -> {
                showLine(3)
            }
            R.id.f_home_hotCity4_lay -> {
                showLine(4)
            }
            R.id.f_home_hotCity5_lay -> {
                showLine(5)
            }
            R.id.search_lay -> {
                //跳转到搜索界面
            }
            R.id.f_home_moreCity_tv -> {
                //更多城市
            }
            R.id.f_home_moreViewPoint_tv -> {
                //更多景点
            }
        }
    }

    override fun getContentView(): View? {
        return f_home_ll
    }

    private fun showLine(showLine: Int) {
        when (showLine) {
            1 -> {
                f_home_hotCity1_tv.setTextColor(ContextCompat.getColor(context!!,R.color.black))
                f_home_hotCity1_v.visibility = View.VISIBLE
                f_home_hotCity2_tv.setTextColor(ContextCompat.getColor(context!!,R.color.gray_6))
                f_home_hotCity2_v.visibility = View.GONE
                f_home_hotCity3_tv.setTextColor(ContextCompat.getColor(context!!,R.color.gray_6))
                f_home_hotCity3_v.visibility = View.GONE
                f_home_hotCity4_tv.setTextColor(ContextCompat.getColor(context!!,R.color.gray_6))
                f_home_hotCity4_v.visibility = View.GONE
                f_home_hotCity5_tv.setTextColor(ContextCompat.getColor(context!!,R.color.gray_6))
                f_home_hotCity5_v.visibility = View.GONE
            }
            2 -> {
                f_home_hotCity1_tv.setTextColor(ContextCompat.getColor(context!!,R.color.gray_6))
                f_home_hotCity1_v.visibility = View.GONE
                f_home_hotCity2_tv.setTextColor(ContextCompat.getColor(context!!,R.color.black))
                f_home_hotCity2_v.visibility = View.VISIBLE
                f_home_hotCity3_tv.setTextColor(ContextCompat.getColor(context!!,R.color.gray_6))
                f_home_hotCity3_v.visibility = View.GONE
                f_home_hotCity4_tv.setTextColor(ContextCompat.getColor(context!!,R.color.gray_6))
                f_home_hotCity4_v.visibility = View.GONE
                f_home_hotCity5_tv.setTextColor(ContextCompat.getColor(context!!,R.color.gray_6))
                f_home_hotCity5_v.visibility = View.GONE
            }
            3 -> {
                f_home_hotCity1_tv.setTextColor(ContextCompat.getColor(context!!,R.color.gray_6))
                f_home_hotCity1_v.visibility = View.GONE
                f_home_hotCity2_tv.setTextColor(ContextCompat.getColor(context!!,R.color.gray_6))
                f_home_hotCity2_v.visibility = View.GONE
                f_home_hotCity3_tv.setTextColor(ContextCompat.getColor(context!!,R.color.black))
                f_home_hotCity3_v.visibility = View.VISIBLE
                f_home_hotCity4_tv.setTextColor(ContextCompat.getColor(context!!,R.color.gray_6))
                f_home_hotCity4_v.visibility = View.GONE
                f_home_hotCity5_tv.setTextColor(ContextCompat.getColor(context!!,R.color.gray_6))
                f_home_hotCity5_v.visibility = View.GONE
            }
            4 -> {
                f_home_hotCity1_tv.setTextColor(ContextCompat.getColor(context!!,R.color.gray_6))
                f_home_hotCity1_v.visibility = View.GONE
                f_home_hotCity2_tv.setTextColor(ContextCompat.getColor(context!!,R.color.gray_6))
                f_home_hotCity2_v.visibility = View.GONE
                f_home_hotCity3_tv.setTextColor(ContextCompat.getColor(context!!,R.color.gray_6))
                f_home_hotCity3_v.visibility = View.GONE
                f_home_hotCity4_tv.setTextColor(ContextCompat.getColor(context!!,R.color.black))
                f_home_hotCity4_v.visibility = View.VISIBLE
                f_home_hotCity5_tv.setTextColor(ContextCompat.getColor(context!!,R.color.gray_6))
                f_home_hotCity5_v.visibility = View.GONE
            }
            5 -> {
                f_home_hotCity1_tv.setTextColor(ContextCompat.getColor(context!!,R.color.gray_6))
                f_home_hotCity1_v.visibility = View.GONE
                f_home_hotCity2_tv.setTextColor(ContextCompat.getColor(context!!,R.color.gray_6))
                f_home_hotCity2_v.visibility = View.GONE
                f_home_hotCity3_tv.setTextColor(ContextCompat.getColor(context!!,R.color.gray_6))
                f_home_hotCity3_v.visibility = View.GONE
                f_home_hotCity4_tv.setTextColor(ContextCompat.getColor(context!!,R.color.gray_6))
                f_home_hotCity4_v.visibility = View.GONE
                f_home_hotCity5_tv.setTextColor(ContextCompat.getColor(context!!,R.color.black))
                f_home_hotCity5_v.visibility = View.VISIBLE
            }
        }
    }

    /**
     * 开启定时器
     */
    private fun startTimer() {
        timer = object : CountDownTimer(20 * 1000, 1000) {
            override fun onTick(l: Long) {
                canRefresh = false
            }

            override fun onFinish() {
                canRefresh = true
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()
        if (canRefresh) {
            //20秒后，显示当前页面刷新
            getDataList()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (null == timer) {
            timer!!.cancel()
        }
    }
}