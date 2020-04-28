package com.example.bysj2020.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.bysj2020.R
import com.example.bysj2020.activity.*
import com.example.bysj2020.adapter.FHomeRecyclerViewAdapter
import com.example.bysj2020.base.BaseFragment
import com.example.bysj2020.bean.FHomeBean
import com.example.bysj2020.bean.FHomeSceneBean
import com.example.bysj2020.bean.SearchRecommendBean
import com.example.bysj2020.bean.XBannerBean
import com.example.bysj2020.event.LocationEvent
import com.example.bysj2020.https.HttpResult
import com.example.bysj2020.https.RxHttp
import com.example.bysj2020.statelayout.LoadHelper
import com.example.bysj2020.utils.SpUtil
import com.example.bysj2020.view.flow.FlowTagLayout
import com.example.bysj2020.view.flow.TextAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *    Author : wxz
 *    Time   : 2020/02/23
 *    Desc   : 首页fragment
 */
class Home : BaseFragment() {

    private var adapter: FHomeRecyclerViewAdapter? = null
    private var timer: CountDownTimer? = null //达到20秒后，触发界面显示，则自动刷新
    private var canRefresh: Boolean = false
    private var isLoading = false//是否正在加载数据,用于防止加载中时操作崩溃
    private lateinit var fHomeBean: FHomeBean
    private lateinit var fHomeSceneBean: FHomeSceneBean
    private var list = mutableListOf<XBannerBean>()
    private lateinit var textAdapter: TextAdapter<String>
    private var searchRecommendBean: SearchRecommendBean? = null

    private var cityId: String = "0"
    private var cityName: String = ""
    private var provincesId: String = ""
    private var provincesName: String = ""

    companion object {
        fun newInstance(): Home {
            return Home()
        }
    }

    override fun loadData() {
        canRefresh = true
        showLoading()
        getHotSearch()
        getDataList()
    }

    override fun setViewClick() {
        f_home_position.setOnClickListener(this)
        search_lay.setOnClickListener(this)
        f_home_moreViewPoint_tv.setOnClickListener(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initViews() {
        EventBus.getDefault().register(this)
        initStateLayout(object : LoadHelper.EmptyClickListener {
            override fun reload() {
                getHotSearch()
                getDataList()
            }
        })
        val city = SpUtil.Obtain(context, "city", "").toString()
        if (city.isNotBlank()) {
            f_home_position.text = city
        }
        //初始化刷新
        f_home_smartRefreshLayout.setOnRefreshListener {
            canRefresh = true
            getHotSearch()
            getDataList()
        }
        f_home_smartRefreshLayout.setDisableContentWhenRefresh(true)
        f_home_smartRefreshLayout.setDisableContentWhenLoading(true)
        f_home_smartRefreshLayout.setEnableLoadMore(false)

        textAdapter = TextAdapter(activity)
        f_home_hot_search.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_NONE)
        f_home_hot_search.adapter = textAdapter
        f_home_hot_search.setOnTagClickListener { parent, view, position ->
            startActivity(
                Intent(
                    activity,
                    SearchList::class.java
                ).putExtra("searchContent", parent!!.adapter.getItem(position) as String)
            )
        }
    }

    /**
     * 初始化热搜
     */
    private fun initHotSearch() {
        if (searchRecommendBean != null) {
            f_home_hot_lay.visibility = View.VISIBLE
            textAdapter.clearAndAddAll(searchRecommendBean!!.recommendTagList)
        }
    }

    /**
     * 获取热搜
     */
    private fun getHotSearch() {
        val rxHttp = RxHttp(context)
        addLifecycle(rxHttp)
        val map = mutableMapOf<String, Any>()
        map["num"] = 5
        rxHttp.getWithJson("searchRecommend", map, object : HttpResult<SearchRecommendBean> {
            override fun OnSuccess(t: SearchRecommendBean?, msg: String?) {
                if (t != null) {
                    searchRecommendBean = t
                    initHotSearch()
                }
            }

            override fun OnFail(code: Int, msg: String?) {
                showToast(msg!!)
            }
        })
    }

    /**
     * 获取数据
     */
    fun getDataList() {
        if (!canRefresh || isLoading) {
            if (f_home_smartRefreshLayout != null) {
                f_home_smartRefreshLayout.finishRefresh()
            }
            return
        }
        isLoading = true
        val rxHttp = RxHttp(context)
        addLifecycle(rxHttp)
        rxHttp.getWithJson("homeIndex", mapOf<String, String>(), object : HttpResult<FHomeBean> {
            override fun OnSuccess(t: FHomeBean?, msg: String?) {
                isLoading = false
                if (t == null) {
                    showEmpty()
                } else {
                    fHomeBean = t
                    setData()
                    showContent()
                    getSceneData()
                }
                if (f_home_smartRefreshLayout.isRefreshing) {
                    f_home_smartRefreshLayout.finishRefresh()
                }
                startTimer()
            }

            override fun OnFail(code: Int, msg: String?) {
                isLoading = false
                showError()
                showToast(msg!!)
            }
        })
    }

    /**
     * 获取景点数据
     */
    private fun getSceneData() {
        f_home_mustPlay_lay.visibility = View.GONE
        val rxHttp = RxHttp(context)
        addLifecycle(rxHttp)
        var map = mutableMapOf<String, String>()
        map["city"] = f_home_position.text.toString()
        rxHttp.getWithJson("homeIndexScene", map, object : HttpResult<FHomeSceneBean> {
            override fun OnSuccess(t: FHomeSceneBean?, msg: String?) {
                if (t != null) {
                    f_home_mustPlay_lay.visibility = View.VISIBLE
                    fHomeSceneBean = t
                    setSceneData()
                }
            }

            override fun OnFail(code: Int, msg: String?) {
                showToast(msg!!)
            }
        })
    }

    /**
     * 设置城市和轮播图
     */
    private fun setData() {
        list.removeAll(list)
        for (indexLoopInfo in fHomeBean.indexLoopInfoList) {
            list.add(XBannerBean(indexLoopInfo.name, indexLoopInfo.img))
        }
        //初始化轮播图
        f_home_xBanner.setBannerData(list)
        f_home_xBanner.loadImage { banner, model, view, position ->
            Glide.with(this@Home).load(list[position].url).into(view as ImageView)
        }
//        f_home_xBanner.setOnItemClickListener { xBanner: XBanner, any: Any, view: View, i: Int ->
//            ToastUtil.setToast(activity, "")
//        }
    }

    /**
     * 设置景点数据
     */
    private fun setSceneData() {
        adapter = FHomeRecyclerViewAdapter(fHomeSceneBean.mustPlaySceneList, context)
        f_home_recyclerView.layoutManager = GridLayoutManager(context, 3)
        f_home_recyclerView.adapter = adapter
        f_home_recyclerView.isNestedScrollingEnabled=false
        adapter!!.addItemClickListener { view, t, position ->
            startActivity(
                Intent(
                    activity,
                    SceneDetails::class.java
                ).putExtra("sceneId", t!!.sceneId.toString())
            )
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.f_home_position -> {
                //选择城市
                startActivityForResult(
                    Intent(activity, Area::class.java).putExtra(
                        "PreciseChoice",
                        true
                    ), 1
                )
            }
            R.id.search_lay -> {
                //跳转到搜索界面
                startActivity(Intent(activity, Search::class.java))
            }
            R.id.f_home_moreViewPoint_tv -> {
                //更多景点
                startActivity(
                    Intent(activity, SceneList::class.java).putExtra(
                        "city",
                        f_home_position.text.toString()
                    )
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                1 -> {
                    if (data == null) {
                        return
                    }
                    cityId = data.getStringExtra("cityId")
                    cityName = data.getStringExtra("cityName")
                    provincesId = data.getStringExtra("provincesId")
                    provincesName = data.getStringExtra("provincesName")
                    f_home_position.text =
                        if (cityName == "不限") provincesName else cityName
                    timer?.cancel()
                    canRefresh = true
                    getDataList()
                }
            }
        }
    }

    override fun getContentView(): View? {
        return f_home_ll
    }


    /**
     * 开启定时器
     */
    private fun startTimer() {
        timer = object : CountDownTimer(20 * 1000, 1000) {
            override fun onTick(l: Long) {
                canRefresh = false
            }

            override fun onFinish() {
                canRefresh = true
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()
        if (canRefresh) {
            //20秒后，显示当前页面刷新
            getDataList()
            f_home_xBanner.startAutoPlay()
        }
    }

    override fun onPause() {
        super.onPause()
        f_home_xBanner.stopAutoPlay()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        if (null == timer) {
            timer!!.cancel()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun locationEvent(event: LocationEvent) {
        when (event.code) {
            3 -> {
                f_home_position.text = if (event.str.endsWith("市")) {
                    event.str.substring(0, (event.str.length - 1))
                } else {
                    event.str
                }
                SpUtil.Save(context, "city", f_home_position.text.toString())
                timer?.cancel()
                canRefresh = true
                getDataList()
            }
        }
    }
}