package com.example.bysj2020.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bysj2020.Interface.ItemClick
import com.example.bysj2020.R
import com.example.bysj2020.adapter.HotelListAdapter
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.bean.HotelRecord
import com.example.bysj2020.bean.SearchListHotelBean
import com.example.bysj2020.dialog.PopupAreaSelector
import com.example.bysj2020.https.HttpPageResult
import com.example.bysj2020.https.RxHttp
import com.example.bysj2020.statelayout.LoadHelper
import kotlinx.android.synthetic.main.base_recyclerview.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 *    Author : wxz
 *    Time   : 2020/04/01
 *    Desc   : 酒店列表
 */
class HotelList : BaseActivity() {

    private var adapter: HotelListAdapter? = null
    private lateinit var hotelRecord: MutableList<HotelRecord>
    private var searchListHotelBean: SearchListHotelBean? = null
    private var page: Int = 0
    private var city: String = ""
    private var popupAreaSelector: PopupAreaSelector? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_hotel_list
    }

    override fun initViews() {
        setBack()
        setTitle("景点")
        setRightText("全国")
        initStateLayout(object : LoadHelper.EmptyClickListener {
            override fun reload() {
                getDataList()
            }
        })
        hotelRecord = ArrayList()
        //禁止下拉刷新或上滑加载时操作列表
        smartRefreshLayout.setDisableContentWhenRefresh(true)
        smartRefreshLayout.setDisableContentWhenLoading(true)
        //启动上滑加载功能
        smartRefreshLayout.setEnableLoadMore(true)

        smartRefreshLayout.setOnRefreshListener {
            page = 0
            getDataList()
        }
        smartRefreshLayout.setOnLoadMoreListener {
            page++
            getDataList()
        }
        getDataList()
    }

    /**
     * 获取列表数据
     */
    private fun getDataList() {
        val rxHttp = RxHttp(this)
        addLifecycle(rxHttp)
        val map = mutableMapOf<String, Any>()
        map["index"] = page
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
                    if (page == 0) {
                        hotelRecord.removeAll(hotelRecord)
                    }
                    searchListHotelBean = t
                    if (t.records.isNotEmpty()) {
                        hotelRecord.addAll(t.records)
                        setData()
                        showContent()
                    } else {
                        showEmpty()
                    }
                }
            }

            override fun OnFail(code: Int, msg: String?) {
                showToast(msg!!)
                showError()
            }
        })
    }

    private fun setData() {
        if (adapter == null) {
            adapter = HotelListAdapter(hotelRecord, this)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter
            adapter!!.addItemClickListener(object : ItemClick<HotelRecord> {
                override fun onItemClick(view: View?, t: HotelRecord?, position: Int) {
                    showToast(t!!.name)
                }
            })
        }
        adapter!!.notifyDataSetChanged()
    }

    override fun setViewClick() {
        right_text.setOnClickListener(this)
    }

    override fun isRegisterEventBus(): Boolean {
        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.right_text -> {
                //目的地弹框
                if (popupAreaSelector == null) {
                    popupAreaSelector = PopupAreaSelector.Builder(this, right_text)
                    popupAreaSelector!!.setPopupAreaSelectorClick { provinceId, provinceName, cityId, cityName ->
                        setRightText(if (cityName == "不限") provinceName else cityName)
                        city = if (cityName == "不限") provinceName else cityName
                        popupAreaSelector!!.dismiss()
                        getDataList()
                    }
                }
                if (!popupAreaSelector!!.isShow) {
                    popupAreaSelector?.show()
                } else {
                    popupAreaSelector?.dismiss()
                }
            }
        }
    }

    override fun getContentView(): View {
        return smartRefreshLayout
    }
}
