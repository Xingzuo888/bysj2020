package com.example.bysj2020.fragment

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bysj2020.R
import com.example.bysj2020.activity.SceneOrderDetails
import com.example.bysj2020.adapter.OrderListAdapter
import com.example.bysj2020.base.BaseFragment
import com.example.bysj2020.bean.OrderListBean
import com.example.bysj2020.bean.OrderListRecord
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
 *    Desc   : 订单--景点
 */
class SceneOrder : BaseFragment() {

    private var adapter: OrderListAdapter? = null
    private var page: Int = 1
    private lateinit var list: MutableList<OrderListRecord>

    companion object {
        fun newInstance(): SceneOrder {
            return SceneOrder()
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
            adapter = OrderListAdapter(list, context!!)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
            adapter!!.addItemClickListener { view, data, position ->
                startActivity(
                    Intent(
                        activity,
                        SceneOrderDetails::class.java
                    ).putExtra("orderId", data.orderId.toString())
                )
            }
        }
        if (page == 1) {
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
        map["type"] = 1 //1-景点 2-酒店
        rxHttp.getPageWithJson("orderList", map, object : HttpPageResult<OrderListBean> {
            override fun OnSuccess(t: OrderListBean?, lastPage: Int, msg: String?) {
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
                        if (page == 1) {
                            list.removeAll(list)
                        }
                        list.addAll(t.records)
                        setData()
                        showContent()
                        //是否需要启动上滑加载功能
                        smartRefreshLayout.setEnableLoadMore(!t.lastPage)
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
            3 -> {
                //刷新景点订单列表
                page = 1
                getDataList()
            }
        }
    }
}