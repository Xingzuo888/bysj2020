package com.example.bysj2020.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bysj2020.R
import com.example.bysj2020.adapter.SettingNoticeInfoAdapter
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.bean.SettingNoticeInfoBean
import com.example.bysj2020.statelayout.LoadHelper
import kotlinx.android.synthetic.main.base_recyclerview.*

/**
 *    Author : wxz
 *    Time   : 2020/03/01
 *    Desc   : 系统通知
 */

class SettingNotice : BaseActivity() {

    private var list:MutableList<SettingNoticeInfoBean> = ArrayList()
    private var adapter:SettingNoticeInfoAdapter?=null

    override fun getLayoutId(): Int {
        return R.layout.activity_setting_notice
    }

    override fun isRegisterEventBus(): Boolean {
        return false
    }

    override fun initViews() {
        setBack()
        setTitle("系统通知")
        initStateLayout(object :LoadHelper.EmptyClickListener{
            override fun reload() {
                getDataList()
            }

        })
        smartRefreshLayout.setOnRefreshListener {
            getDataList()
        }
        smartRefreshLayout.setOnLoadMoreListener {
            getDataList()
        }
        smartRefreshLayout.setDisableContentWhenRefresh(true)
        smartRefreshLayout.setDisableContentWhenLoading(true)
        smartRefreshLayout.setEnableLoadMore(true)

        recyclerView.layoutManager=LinearLayoutManager(this)
        adapter=SettingNoticeInfoAdapter(list,this)
        recyclerView.adapter=adapter
        getDataList()
    }

    override fun setViewClick() {
    }

    override fun onClick(v: View?) {
    }

    override fun getContentView(): View {
        return smartRefreshLayout
    }

    private fun getDataList() {
        list?.add(SettingNoticeInfoBean("gkrflsedgnlnfdlnrgskjhratg"))
        list?.add(SettingNoticeInfoBean("方大化工一后撒花姑娘开始的风格"))
        list?.add(SettingNoticeInfoBean("阿热压罐热爱亚尔"))
        list?.add(SettingNoticeInfoBean("are委员会然而又惹他问过"))
        list?.add(SettingNoticeInfoBean("爱问其他与天津银行开局号"))
        list?.add(SettingNoticeInfoBean("很过分可以尽快是个IGVB女 "))
        list?.add(SettingNoticeInfoBean("dfsrhfdsgdsfg "))
        list?.add(SettingNoticeInfoBean("飞鞋点金韩国小分队 "))
        list?.add(SettingNoticeInfoBean("巅峰赛入户柜分销价 "))
        list?.add(SettingNoticeInfoBean("地方找师傅的学号 "))
        list?.add(SettingNoticeInfoBean("视图赶紧看看龙 "))
        list?.add(SettingNoticeInfoBean("一我U币开奖号狗很遗憾客家话GV看  "))
        list?.add(SettingNoticeInfoBean("IG羊头峪鸡腿肉发个电话关键词 "))
        list?.add(SettingNoticeInfoBean("浮点型黄金分割参加过好吃是放几天福星华府加工费纪梵希 "))
        adapter?.notifyDataSetChanged()
        showContent()
        if (smartRefreshLayout.isRefreshing) {
            smartRefreshLayout.finishRefresh()
        }
        if (smartRefreshLayout.isLoading) {
            smartRefreshLayout.finishLoadMore()
        }
    }
}

