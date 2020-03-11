package com.example.bysj2020.adapter

import android.content.Context
import com.example.bysj2020.R
import com.example.bysj2020.bean.SettingNoticeInfoBean
import com.example.bysj2020.common.BaseAdapter
import com.example.bysj2020.common.BaseRecyclerHolder

/**
 *    Author : wxz
 *    Time   : 2020/03/03
 *    Desc   : 系统通知信息适配器
 */
class SettingNoticeInfoAdapter(mList: MutableList<SettingNoticeInfoBean>?, context: Context?) :
    BaseAdapter<SettingNoticeInfoBean>(mList, context) {
    override fun covert(
        holder: BaseRecyclerHolder?,
        mList: MutableList<SettingNoticeInfoBean>?,
        position: Int
    ) {
        holder?.setText(R.id.setting_notice_tv, mList!![position]?.info)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_setting_notice
    }

    override fun getContentLength(): Int {
        return 10
    }
}