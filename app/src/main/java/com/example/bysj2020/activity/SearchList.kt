package com.example.bysj2020.activity

import android.view.View
import androidx.fragment.app.Fragment
import com.example.bysj2020.R
import com.example.bysj2020.adapter.ViewPagerAdapters
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.dialog.PopupAreaSelector
import com.example.bysj2020.event.AreaSelectorEvent
import com.example.bysj2020.fragment.SearchAttractions
import com.example.bysj2020.fragment.SearchHotel
import com.example.bysj2020.statelayout.LoadHelper
import kotlinx.android.synthetic.main.activity_search_list.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus

/**
 *    Author : wxz
 *    Time   : 2020/03/24
 *    Desc   : 搜索列表
 */
class SearchList : BaseActivity() {
    private var tabs = listOf("景点", "酒店")
    private var dataFragment: MutableList<Fragment>? = ArrayList()
    private var searchContent: String = ""
    private var popupAreaSelector: PopupAreaSelector? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_search_list
    }

    override fun initViews() {
        searchContent = intent.getStringExtra("searchContent").toString()
        setBack()
        setTitle("\"${searchContent}\"的搜索结果")
        setRightText("全国")
        initStateLayout(object : LoadHelper.EmptyClickListener {
            override fun reload() {
                getSearchResult()
            }
        })
        getSearchResult()
    }

    private fun initFragment(data: String) {
        dataFragment?.add(SearchAttractions.newInstance(data))
        dataFragment?.add(SearchHotel.newInstance(data))
        search_list_viewPager.adapter = ViewPagerAdapters(supportFragmentManager, dataFragment)
        search_list_viewPager.offscreenPageLimit = 1
        search_list_slidingTabLayout.setViewPager(search_list_viewPager, tabs.toTypedArray())
    }

    /**
     * 获取搜索结果
     */
    private fun getSearchResult() {
        initFragment(searchContent)
        showContent()
    }

    override fun setViewClick() {
        right_text.setOnClickListener(this)
    }

    override fun isRegisterEventBus(): Boolean {
        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.right_text -> {
                //目的地弹框
                if (popupAreaSelector == null) {
                    popupAreaSelector = PopupAreaSelector.Builder(this, right_text,true)
                    popupAreaSelector!!.setPopupAreaSelectorClick { provinceId, provinceName, cityId, cityName ->
                        EventBus.getDefault().post(AreaSelectorEvent(1, provinceName, cityName))
                        popupAreaSelector!!.dismiss()
                        setRightText(if (cityName == "不限") provinceName else cityName)
                    }
                }
                if (!popupAreaSelector!!.isShow) {
                    popupAreaSelector?.show()
                } else {
                    popupAreaSelector?.dismiss()
                }
            }
        }
    }

    override fun getContentView(): View {
        return search_list_rootView
    }
}
