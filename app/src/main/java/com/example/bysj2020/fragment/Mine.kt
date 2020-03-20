package com.example.bysj2020.fragment

import android.content.Intent
import android.view.View
import com.example.bysj2020.R
import com.example.bysj2020.activity.*
import com.example.bysj2020.base.BaseFragment
import com.example.bysj2020.event.LoginEvent
import com.example.bysj2020.statelayout.LoadHelper
import com.example.bysj2020.utils.LoadImageUtil
import com.example.bysj2020.utils.SpUtil
import kotlinx.android.synthetic.main.fragment_mine.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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
        EventBus.getDefault().register(this)
        initStateLayout(object : LoadHelper.EmptyClickListener {
            override fun reload() {
                getDataList()
            }
        })

        val loginToken=SpUtil.Obtain(context,"loginToken","").toString()
        if (loginToken.isBlank()) {
            f_mine_login_tv.visibility = View.VISIBLE
        } else {
            f_mine_login_tv.visibility = View.GONE
            val imgUrl=SpUtil.Obtain(context,"avatar","").toString()
            LoadImageUtil(this).loadCircularImage(f_mine_head_iv,imgUrl,R.mipmap.default_head)
        }
        LoadImageUtil(context!!).loadImage(mine_img,"https://bysj2020.oss-cn-beijing.aliyuncs.com/attractions/scene/18/small_img/18_smallimg.jpg")
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun loginEvent(event:LoginEvent) {
        when (event.code) {
            0->{
                //登录成功
                val imgUrl=SpUtil.Obtain(context,"avatar","").toString()
                LoadImageUtil(this).loadCircularImage(f_mine_head_iv,imgUrl,R.mipmap.default_head)
            }
            1->{
               //退出登录
                LoadImageUtil(this).loadCircularImage(f_mine_head_iv,"",R.mipmap.default_head)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}