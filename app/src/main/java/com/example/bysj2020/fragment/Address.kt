package com.example.bysj2020.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import com.example.bysj2020.R
import com.example.bysj2020.activity.Area
import com.example.bysj2020.activity.Search
import com.example.bysj2020.base.BaseFragment
import com.example.bysj2020.statelayout.LoadHelper
import kotlinx.android.synthetic.main.fragment_address.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *    Author : wxz
 *    Time   : 2020/02/25
 *    Desc   : 目的地fragment
 */
class Address : BaseFragment() {

    private var cityId: String = "0"
    private var cityName: String = ""
    private var provincesId: String = ""
    private var provincesName: String = ""

    companion object {
        fun newInstance(): Address {
            return Address()
        }
    }

    override fun loadData() {
        showLoading()
        getDataList()
    }

    override fun setViewClick() {
        f_address_positionName_tv.setOnClickListener(this)
        f_address_search.setOnClickListener(this)
        f_address_moreViewPoint_tv.setOnClickListener(this)
        f_address_moreHotel_tv.setOnClickListener(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_address
    }

    override fun initViews() {
        initStateLayout(object : LoadHelper.EmptyClickListener {
            override fun reload() {
                getDataList()
            }

        })
        //初始化刷新
        f_address_smartRefreshLayout.setOnRefreshListener {
            getDataList()
        }
        f_address_smartRefreshLayout.setDisableContentWhenRefresh(true)
        f_address_smartRefreshLayout.setDisableContentWhenLoading(true)
        f_address_smartRefreshLayout.setEnableLoadMore(false)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.f_address_positionName_tv -> {
                //选择城市
                startActivityForResult(
                    Intent(activity, Area::class.java).putExtra(
                        "PreciseChoice",
                        false
                    ), 1
                )
            }
            R.id.f_address_search -> {
                //搜索
                startActivity(Intent(activity, Search::class.java))
            }
            R.id.f_address_moreViewPoint_tv -> {
                //更多景点
            }
            R.id.f_address_moreHotel_tv -> {
                //更多酒店
            }
        }
    }

    override fun getContentView(): View? {
        return f_address_ll
    }

    /**
     * 获取数据
     */
    fun getDataList() {
        showContent()
        if (f_address_smartRefreshLayout.isRefreshing) {
            f_address_smartRefreshLayout.finishRefresh()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                1 -> {
                    if (data == null) {
                        return
                    }
                    cityId = data.getStringExtra("cityId")
                    cityName = data.getStringExtra("cityName")
                    provincesId = data.getStringExtra("provincesId")
                    provincesName = data.getStringExtra("provincesName")
                    f_address_positionName_tv.text =
                        if (cityName == "不限") provincesName else cityName
                }
            }
        }
    }
}