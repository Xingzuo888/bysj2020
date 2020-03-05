package com.example.bysj2020.activity

import android.view.View
import com.example.bysj2020.R
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.statelayout.LoadHelper
import kotlinx.android.synthetic.main.activity_account_security.*

/**
 *    Author : wxz
 *    Time   : 2020/03/01
 *    Desc   : 账户安全
 */
class AccountSecurity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_account_security
    }

    override fun initViews() {
        setBack()
        setTitle("账户安全")
        initStateLayout(object :LoadHelper.EmptyClickListener{
            override fun reload() {
                getDataList()
            }
        })
        getDataList()
    }

    override fun setViewClick() {
    }

    override fun onClick(v: View?) {
    }

    override fun getContentView(): View {
        return account_security_scrollView
    }

    private fun getDataList() {
        showContent()
    }

}
