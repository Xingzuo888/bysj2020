package com.example.bysj2020.activity

import android.view.View
import androidx.fragment.app.Fragment
import com.example.bysj2020.R
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.fragment.SearchAttractions
import com.example.bysj2020.fragment.SearchHotel
import com.example.bysj2020.statelayout.LoadHelper
import kotlinx.android.synthetic.main.activity_search_list.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 *    Author : wxz
 *    Time   : 2020/03/24
 *    Desc   : 搜索列表
 */
class SearchList : BaseActivity() {
    private var tabs= listOf("景点","酒店")
    private var dataFragment:MutableList<Fragment>?=ArrayList()
    private var searchContent:String?=""
    override fun getLayoutId(): Int {
        return R.layout.activity_search_list
    }

    override fun initViews() {
        searchContent=intent.getStringExtra("searchContent").toString()
        setBack()
        setTitle("${searchContent}的搜索结果")
        setRightText("目的地")
        initStateLayout(object :LoadHelper.EmptyClickListener{
            override fun reload() {
                getSearchResult()
            }
        })
        getSearchResult()
    }

    private fun initFragment() {
        dataFragment?.add(SearchAttractions.newInstance())
        dataFragment?.add(SearchHotel.newInstance())
    }

    /**
     * 获取搜索结果
     */
    private fun getSearchResult() {

    }

    override fun setViewClick() {
        right_text.setOnClickListener(this)
    }

    override fun isRegisterEventBus(): Boolean {
        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.right_text->{
                //目的地弹框

            }
        }
    }

    override fun getContentView(): View {
        return search_list_rootView
    }
}
