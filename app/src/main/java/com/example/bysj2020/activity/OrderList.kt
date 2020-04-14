package com.example.bysj2020.activity

import android.view.View
import androidx.fragment.app.Fragment
import com.example.bysj2020.R
import com.example.bysj2020.adapter.ViewPagerAdapters
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.fragment.HotelOrder
import com.example.bysj2020.fragment.SceneOrder
import kotlinx.android.synthetic.main.activity_order_list.*

/**
 *    Author : wxz
 *    Time   : 2020/04/13
 *    Desc   : 订单列表
 */
class OrderList : BaseActivity() {
    private var tabs = listOf("景点", "酒店")
    private var dataFragment: MutableList<Fragment>? = ArrayList()
    override fun getLayoutId(): Int {
        return R.layout.activity_order_list
    }

    override fun initViews() {
        setBack()
        setTitle("订单")
        initFragment()
    }

    private fun initFragment() {
        dataFragment?.add(SceneOrder.newInstance())
        dataFragment?.add(HotelOrder.newInstance())
        orderList_viewPager.adapter = ViewPagerAdapters(supportFragmentManager, dataFragment)
        orderList_viewPager.offscreenPageLimit = 1
        orderList_slidingTabLayout.setViewPager(orderList_viewPager, tabs.toTypedArray())
    }

    override fun setViewClick() {
    }

    override fun isRegisterEventBus(): Boolean {
        return false
    }

    override fun onClick(v: View?) {
    }

}
