package com.example.bysj2020.activity

import android.content.Intent
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
    override fun isRegisterEventBus(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_account_security
    }

    override fun initViews() {
        setBack()
        setTitle("账户安全")
        initStateLayout(object : LoadHelper.EmptyClickListener {
            override fun reload() {
                getDataList()
            }
        })
        getDataList()
    }

    override fun setViewClick() {
        account_security_password_lay.setOnClickListener(this)
        account_security_phone_lay.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.account_security_password_lay -> {
                //修改密码
                startActivity(Intent(this, ModifyPassword::class.java).putExtra("titleId", 1))
            }
            R.id.account_security_phone_lay -> {
                //修改手机号
                startActivity(Intent(this, ModifyPhone::class.java))
            }
        }
    }

    override fun getContentView(): View {
        return account_security_scrollView
    }

    private fun getDataList() {
        showContent()
    }

}
