package com.example.bysj2020.activity

import android.view.View
import androidx.fragment.app.Fragment
import com.example.bysj2020.R
import com.example.bysj2020.adapter.ViewPagerAdapters
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.fragment.HotelFavorite
import com.example.bysj2020.fragment.SceneFavorite
import kotlinx.android.synthetic.main.activity_favorite.*

/**
 * Author : wxz
 * Time   : 2020/04/12
 * Desc   : 收藏列表
 */
class Favorite : BaseActivity() {
    private var tabs = listOf("景点", "酒店")
    private var dataFragment: MutableList<Fragment>? = ArrayList()
    override fun getLayoutId(): Int {
        return R.layout.activity_favorite
    }

    override fun initViews() {
        setBack()
        setTitle("收藏")
        initFragment()
    }

    private fun initFragment() {
        dataFragment?.add(SceneFavorite.newInstance())
        dataFragment?.add(HotelFavorite.newInstance())
        favorite_viewPager.adapter = ViewPagerAdapters(supportFragmentManager, dataFragment)
        favorite_viewPager.offscreenPageLimit = 1
        favorite_slidingTabLayout.setViewPager(favorite_viewPager, tabs.toTypedArray())
    }

    override fun setViewClick() {
    }

    override fun isRegisterEventBus(): Boolean {
        return false
    }

    override fun onClick(v: View?) {
    }

}
