package com.example.bysj2020.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bysj2020.R
import com.example.bysj2020.activity.HotelDetails
import com.example.bysj2020.adapter.SearchListHotelAdapter
import com.example.bysj2020.base.BaseFragment
import com.example.bysj2020.bean.HotelRecord
import com.example.bysj2020.bean.SearchListHotelBean
import com.example.bysj2020.event.AreaSelectorEvent
import com.example.bysj2020.https.HttpPageResult
import com.example.bysj2020.https.RxHttp
import com.example.bysj2020.statelayout.LoadHelper
import kotlinx.android.synthetic.main.base_recyclerview.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *    Author : wxz
 *    Time   : 2020/03/24
 *    Desc   : 搜索结果--酒店
 */
class SearchHotel : BaseFragment() {

    private var searchContent: String = ""
    private var page: Int = 1
    private var city: String = ""
    private var searchListHotelBean: SearchListHotelBean? = null
    private lateinit var hotelRecords: MutableList<HotelRecord>
    private var adapter: SearchListHotelAdapter? = null

    companion object {
        fun newInstance(data: String): SearchHotel {
            val fragment = SearchHotel()
            val args = Bundle()
            args.putString("data", data)
            fragment.arguments = args
            return fragment
        }
    }

    override fun loadData() {
        showLoading()
        getDataList()
    }

    override fun setViewClick() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search_hotel
    }

    override fun getContentView(): View? {
        return smartRefreshLayout
    }

    override fun initViews() {
        EventBus.getDefault().register(this)
        searchContent = arguments!!.getString("data").toString()
        initStateLayout(object : LoadHelper.EmptyClickListener {
            override fun reload() {
                getDataList()
            }
        })
        hotelRecords = ArrayList()
        smartRefreshLayout.setOnRefreshListener {
            page = 1
            getDataList()
        }
        smartRefreshLayout.setOnLoadMoreListener {
            page++
            getDataList()
        }
        //禁止下拉刷新或上滑加载时操作列表
        smartRefreshLayout.setDisableContentWhenRefresh(true)
        smartRefreshLayout.setDisableContentWhenLoading(true)
        //是否启用越界拖动（仿苹果效果）
        smartRefreshLayout.setEnableOverScrollDrag(true)
    }

    private fun setData() {
        if (adapter == null) {
            adapter = SearchListHotelAdapter(hotelRecords, context!!)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
            adapter!!.addItemClickListener { view, t, position ->
                startActivity(
                    Intent(
                        activity,
                        HotelDetails::class.java
                    ).putExtra("hotelId", t!!.hotelId.toString())
                )
            }
        }
        if (page == 1) {
            adapter!!.notifyDataSetChanged()
        } else {
            adapter!!.notifyItemChanged(hotelRecords.size)
        }
    }

    /**
     * 获取景点列表数据
     */
    private fun getDataList() {
        val rxHttp = RxHttp(context)
        addLifecycle(rxHttp)
        val map = mutableMapOf<String, Any>()
        map["index"] = page
        if (searchContent.isNotBlank()) {
            map["searchContent"] = searchContent
        }
        if (city.isNotBlank()) {
            map["city"] = city
        }
        rxHttp.getPageWithJson("searchHotel", map, object : HttpPageResult<SearchListHotelBean> {
            override fun OnSuccess(t: SearchListHotelBean?, lastPage: Int, msg: String?) {
                if (smartRefreshLayout.isRefreshing) {
                    smartRefreshLayout.finishRefresh()
                } else if (smartRefreshLayout.isLoading) {
                    smartRefreshLayout.finishLoadMore()
                }
                if (t == null) {
                    showEmpty()
                } else {
                    searchListHotelBean = t
                    if (t.records.isNotEmpty()) {
                        if (page == 1) {
                            hotelRecords.removeAll(hotelRecords)
                        }
                        hotelRecords.addAll(t.records)
                        setData()
                        showContent()
                        //是否需要启动上滑加载功能
                        smartRefreshLayout.setEnableLoadMore(!t.lastPage)
                    } else {
                        showEmpty()
                    }
                }
            }

            override fun OnFail(code: Int, msg: String?) {
                if (smartRefreshLayout.isRefreshing) {
                    smartRefreshLayout.finishRefresh()
                } else if (smartRefreshLayout.isLoading) {
                    smartRefreshLayout.finishLoadMore()
                }
                showToast(msg!!)
                showError()
            }
        })
    }

    override fun onClick(v: View?) {
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun AreaSelectorEvent(event: AreaSelectorEvent) {
        when (event.code) {
            1 -> {
                city = if (event.cityName == "不限") event.provinceName else event.cityName
                page = 1
                getDataList()
            }
        }
    }

}