package com.example.bysj2020.fragment

import android.view.View
import com.example.bysj2020.R
import com.example.bysj2020.base.BaseFragment

/**
 *    Author : wxz
 *    Time   : 2020/03/24
 *    Desc   : 搜索结果--景点
 */
class SearchAttractions:BaseFragment() {

    companion object{
        fun newInstance():SearchAttractions{
            return SearchAttractions()
        }
    }
    override fun loadData() {

    }

    override fun setViewClick() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search_attractions
    }

    override fun initViews() {
    }

    override fun onClick(v: View?) {
    }
}