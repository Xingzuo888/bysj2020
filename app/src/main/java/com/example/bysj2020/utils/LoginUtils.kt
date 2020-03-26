package com.example.bysj2020.utils

import android.content.Context
import com.example.bysj2020.bean.LoginBean
import com.example.bysj2020.event.LoginEvent
import org.greenrobot.eventbus.EventBus

/**
 *    Author : wxz
 *    Time   : 2020/03/12
 *    Desc   : 登录及退出登录相关
 */
object LoginUtils {

    /**
     * 保存登录数据
     */
    fun saveLoginBean(context: Context, t: LoginBean?) {
        SpUtil.Save(context, "loginToken", t?.token)
        SpUtil.Save(context, "avatar", t?.avatar)
        SpUtil.Save(context, "axUid", t?.axUid)
        SpUtil.Save(context, "role", t?.role)
        SpUtil.Save(context, "nickname", t?.nickname)
        SpUtil.Save(context, "sex", t?.sex)
    }

    /**
     * 清空登录数据并跳转登录页面
     */
    @JvmStatic
    fun clearData(context: Context) {
        SpUtil.Save(context, "loginToken", "")
        SpUtil.Save(context, "avatar", "")
        SpUtil.Save(context, "axUid", 0)
        SpUtil.Save(context, "role", "")
        SpUtil.Save(context, "nickname", "")
        SpUtil.Save(context, "sex", 0)
        SpUtil.Save(context, "mobilePhone", "")
        EventBus.getDefault().post(LoginEvent(1))
    }
}