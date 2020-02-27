package com.example.bysj2020.fragment

import android.view.View
import com.example.bysj2020.R
import com.example.bysj2020.base.BaseFragment
import com.example.bysj2020.statelayout.LoadHelper
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *    Author : wxz
 *    Time   : 2020/02/26
 *    Desc   : 我的fragment
 */
class Mine: BaseFragment() {

    companion object{
        fun newInstance():Mine {
            return Mine()
        }
    }

    override fun loadData() {
        showLoading()
        getDataList()
    }

    override fun setViewClick() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun initViews() {
        initStateLayout(object :LoadHelper.EmptyClickListener{
            override fun reload() {
                getDataList()
            }

        })
    }

    override fun onClick(v: View?) {
    }

    override fun getContentView(): View? {
        return f_mine_scrollView
    }

    private fun getDataList() {

        showContent()
    }
}