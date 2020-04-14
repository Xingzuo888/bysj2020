package com.example.bysj2020.fragment

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bysj2020.R
import com.example.bysj2020.activity.SceneDetails
import com.example.bysj2020.adapter.FavoriteAdapter
import com.example.bysj2020.base.BaseFragment
import com.example.bysj2020.bean.FavoriteBean
import com.example.bysj2020.bean.FavoriteRecord
import com.example.bysj2020.event.UserInfoEvent
import com.example.bysj2020.https.HttpPageResult
import com.example.bysj2020.https.RxHttp
import com.example.bysj2020.statelayout.LoadHelper
import kotlinx.android.synthetic.main.base_recyclerview.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *    Author : wxz
 *    Time   : 2020/04/12
 *    Desc   : 收藏--景点
 */
class SceneFavorite : BaseFragment() {

    private var adapter: FavoriteAdapter? = null
    private var page: Int = 0
    private lateinit var list: MutableList<FavoriteRecord>

    companion object {
        fun newInstance(): SceneFavorite {
            return SceneFavorite()
        }
    }

    override fun loadData() {
        showLoading()
        getDataList()
    }

    override fun setViewClick() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_scene_list
    }

    override fun initViews() {
        EventBus.getDefault().register(this)
        initStateLayout(object : LoadHelper.EmptyClickListener {
            override fun reload() {
                getDataList()
            }
        })
        list = ArrayList()
        smartRefreshLayout.setOnRefreshListener {
            page = 0
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

    override fun onClick(v: View?) {
    }

    override fun getContentView(): View? {
        return smartRefreshLayout
    }

    /**
     * 设置数据
     */
    private fun setData() {
        if (adapter == null) {
            adapter = FavoriteAdapter(list, context!!)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
            adapter!!.addItemClickListener { view, data, position ->
                startActivity(
                    Intent(
                        activity,
                        SceneDetails::class.java
                    ).putExtra("sceneId", data?.businessId.toString())
                )
            }
        }
        if (page == 0) {
            adapter!!.notifyDataSetChanged()
        } else {
            adapter!!.notifyItemChanged(list.size)
        }
    }

    /**
     * 获取收藏列表
     */
    private fun getDataList() {
        val rxHttp = RxHttp(activity)
        addLifecycle(rxHttp)
        val map = mutableMapOf<String, Any>()
        map["index"] = page
        map["type"] = 1 //1-景点 2-酒店 3-帖子
        rxHttp.getPageWithJson("saveList", map, object : HttpPageResult<FavoriteBean> {
            override fun OnSuccess(t: FavoriteBean?, lastPage: Int, msg: String?) {
                if (smartRefreshLayout.isRefreshing) {
                    smartRefreshLayout.finishRefresh()
                } else if (smartRefreshLayout.isLoading) {
                    smartRefreshLayout.finishLoadMore()
                }
                if (t == null) {
                    showEmpty()
                } else {
                    if (t.records.isNullOrEmpty()) {
                        showEmpty()
                    } else {
                        if (page == 0) {
                            list.removeAll(list)
                        }
                        list.addAll(t.records)
                        setData()
                        showContent()
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

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun userInfoEvent(event: UserInfoEvent) {
        when (event.type) {
            2 -> {
                //刷新收藏列表
                getDataList()
            }
        }
    }
}