package com.example.bysj2020.fragment

import android.view.View
import com.example.bysj2020.R
import com.example.bysj2020.base.BaseFragment

/**
 *    Author : wxz
 *    Time   : 2020/03/24
 *    Desc   : 搜索结果--酒店
 */
class SearchHotel:BaseFragment() {

    companion object{
        fun newInstance():SearchHotel{
            return SearchHotel()
        }
    }
    override fun loadData() {

    }

    override fun setViewClick() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search_hotel
    }

    override fun initViews() {
    }

    override fun onClick(v: View?) {
    }
}