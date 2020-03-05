package com.example.bysj2020.fragment

import android.content.Intent
import android.view.View
import com.example.bysj2020.R
import com.example.bysj2020.activity.*
import com.example.bysj2020.base.BaseFragment
import com.example.bysj2020.statelayout.LoadHelper
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 *    Author : wxz
 *    Time   : 2020/02/26
 *    Desc   : 我的fragment
 */
class Mine : BaseFragment() {

    companion object {
        fun newInstance(): Mine {
            return Mine()
        }
    }

    override fun loadData() {
        showLoading()
        getDataList()
    }

    override fun setViewClick() {
        f_mine_login_tv.setOnClickListener(this)
        f_mine_self_info_lay.setOnClickListener(this)
        f_mine_account_security_lay.setOnClickListener(this)
        f_mine_system_notification_lay.setOnClickListener(this)
        f_mine_setting.setOnClickListener(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun initViews() {
        initStateLayout(object : LoadHelper.EmptyClickListener {
            override fun reload() {
                getDataList()
            }

        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.f_mine_login_tv -> {
                startActivity(
                    Intent(
                        activity,
                        LoginVerificationCode::class.java
                    ).putExtra("isBackArrow", true)
                )
            }
            R.id.f_mine_account_security_lay -> {
                //账户安全
                startActivity(Intent(activity, AccountSecurity::class.java))
            }
            R.id.f_mine_self_info_lay -> {
                //个人信息
                startActivity(Intent(activity, PersonalInformation::class.java))
            }
            R.id.f_mine_system_notification_lay -> {
                //系统通知
                startActivity(Intent(activity, SettingNotice::class.java))
            }
            R.id.f_mine_setting -> {
                //系统设置
                startActivity(Intent(activity, SystemSetting::class.java))
            }
        }
    }

    override fun getContentView(): View? {
        return f_mine_lay
    }

    private fun getDataList() {

        showContent()
    }
}