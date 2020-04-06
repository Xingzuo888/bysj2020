package com.example.bysj2020.activity

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bysj2020.R
import com.example.bysj2020.adapter.SceneListAdapter
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.bean.SceneRecord
import com.example.bysj2020.bean.SearchListSceneBean
import com.example.bysj2020.dialog.PopupAreaSelector
import com.example.bysj2020.https.HttpPageResult
import com.example.bysj2020.https.RxHttp
import com.example.bysj2020.statelayout.LoadHelper
import kotlinx.android.synthetic.main.base_recyclerview.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 *    Author : wxz
 *    Time   : 2020/04/01
 *    Desc   : 景点列表
 */
class SceneList : BaseActivity() {
    private var adapter: SceneListAdapter? = null
    private lateinit var sceneRecords: MutableList<SceneRecord>
    private var searchListSceneBean: SearchListSceneBean? = null
    private var page: Int = 0
    private var city: String = ""
    private var popupAreaSelector: PopupAreaSelector? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_scene_list
    }

    override fun initViews() {
        city = intent.getStringExtra("city").toString()
        setBack()
        setTitle("景点")
        setRightText(if (city.isNotBlank()) city else "全国")
        initStateLayout(object : LoadHelper.EmptyClickListener {
            override fun reload() {
                getDataList()
            }
        })
        sceneRecords = ArrayList()
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
                        if (page == 0) {
                            sceneRecords.removeAll(sceneRecords)
                        }
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

    private fun setData() {
        if (adapter == null) {
            adapter = SceneListAdapter(sceneRecords, this)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter
            adapter!!.addItemClickListener { view, t, position ->
                startActivity(
                    Intent(
                        this@SceneList,
                        SceneDetails::class.java
                    ).putExtra("sceneId", t!!.sceneId.toString())
                )
            }
        }
        if (page == 0) {
            adapter!!.notifyDataSetChanged()
        } else {
            adapter!!.notifyItemChanged(sceneRecords.size)
        }
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
                    popupAreaSelector = PopupAreaSelector.Builder(this, right_text, true)
                    popupAreaSelector!!.setPopupAreaSelectorClick { provinceId, provinceName, cityId, cityName ->
                        setRightText(if (cityName == "不限") provinceName else cityName)
                        city = if (cityName == "不限") provinceName else cityName
                        page = 0
                        getDataList()
                        popupAreaSelector!!.dismiss()
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
