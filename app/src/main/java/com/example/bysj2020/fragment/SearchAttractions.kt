package com.example.bysj2020.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bysj2020.Interface.ItemClick
import com.example.bysj2020.R
import com.example.bysj2020.adapter.SearchListSceneAdapter
import com.example.bysj2020.base.BaseFragment
import com.example.bysj2020.bean.SceneRecord
import com.example.bysj2020.bean.SearchListSceneBean
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
 *    Desc   : 搜索结果--景点
 */
class SearchAttractions : BaseFragment() {

    private var searchContent: String = ""
    private var page: Int = 0
    private var city: String = ""
    private var searchListSceneBean: SearchListSceneBean? = null
    private lateinit var sceneRecords: MutableList<SceneRecord>
    private var adapter: SearchListSceneAdapter? = null

    companion object {
        fun newInstance(data: String): SearchAttractions {
            val fragment = SearchAttractions()
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
        return R.layout.fragment_search_attractions
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
        sceneRecords = ArrayList()
        smartRefreshLayout.setOnRefreshListener {
            page = 0
            sceneRecords.removeAll(sceneRecords)
            getDataList()
        }
        smartRefreshLayout.setOnLoadMoreListener {
            page++
            getDataList()
        }
        //禁止下拉刷新或上滑加载时操作列表
        smartRefreshLayout.setDisableContentWhenRefresh(true)
        smartRefreshLayout.setDisableContentWhenLoading(true)
        //启动上滑加载功能
        smartRefreshLayout.setEnableLoadMore(true)
    }

    private fun setData() {
        if (adapter == null) {
            adapter = SearchListSceneAdapter(sceneRecords, context!!)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
            adapter!!.addItemClickListener(object : ItemClick<SceneRecord> {
                override fun onItemClick(view: View?, t: SceneRecord?, position: Int) {
                    showToast(t!!.name)
                }
            })
        }
        adapter!!.notifyDataSetChanged()
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
        rxHttp.getPageWithJson("searchScene", map, object : HttpPageResult<SearchListSceneBean> {
            override fun OnSuccess(t: SearchListSceneBean?, lastPage: Int, msg: String?) {
                if (smartRefreshLayout.isRefreshing) {
                    smartRefreshLayout.finishRefresh()
                } else if (smartRefreshLayout.isLoading) {
                    smartRefreshLayout.finishLoadMore()
                }
                if (t == null) {
                    showEmpty()
                } else {
                    searchListSceneBean = t
                    if (t.records.isNotEmpty()) {
                        sceneRecords.addAll(t.records)
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
                getDataList()
            }
        }
    }
}