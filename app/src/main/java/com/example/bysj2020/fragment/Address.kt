package com.example.bysj2020.fragment

import android.view.View
import com.example.bysj2020.R
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
class Address :BaseFragment() {

    companion object{
        fun newInstance():Address{
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
        initStateLayout(object :LoadHelper.EmptyClickListener{
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
            R.id.f_address_positionName_tv->{
                //选择城市
            }
            R.id.f_address_search->{
                //搜索
            }
            R.id.f_address_moreViewPoint_tv->{
                //更多景点
            }
            R.id.f_address_moreHotel_tv->{
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
    }
}