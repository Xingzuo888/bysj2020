package com.example.bysj2020.fragment

import android.content.Intent
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.bysj2020.Interface.ItemClick
import com.example.bysj2020.R
import com.example.bysj2020.activity.SceneList
import com.example.bysj2020.activity.Search
import com.example.bysj2020.adapter.FHomeRecyclerViewAdapter
import com.example.bysj2020.base.BaseFragment
import com.example.bysj2020.bean.FHomeBean
import com.example.bysj2020.bean.FHomeSceneBean
import com.example.bysj2020.bean.MustPlayScene
import com.example.bysj2020.bean.XBannerBean
import com.example.bysj2020.event.SwitchFragmentEvent
import com.example.bysj2020.https.HttpResult
import com.example.bysj2020.https.RxHttp
import com.example.bysj2020.statelayout.LoadHelper
import com.example.bysj2020.utils.ToastUtil
import com.stx.xhb.androidx.XBanner
import kotlinx.android.synthetic.main.fragment_home.*
import org.greenrobot.eventbus.EventBus

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

    companion object {
        fun newInstance(): Home {
            return Home()
        }
    }

    override fun loadData() {
        canRefresh = true
        showLoading()
        getDataList()
    }

    override fun setViewClick() {
        f_home_hotCity1_lay.setOnClickListener(this)
        f_home_hotCity2_lay.setOnClickListener(this)
        f_home_hotCity3_lay.setOnClickListener(this)
        f_home_hotCity4_lay.setOnClickListener(this)
        f_home_hotCity5_lay.setOnClickListener(this)
        search_lay.setOnClickListener(this)
        f_home_moreCity_tv.setOnClickListener(this)
        f_home_moreViewPoint_tv.setOnClickListener(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initViews() {
        initStateLayout(object : LoadHelper.EmptyClickListener {
            override fun reload() {
                getDataList()
            }

        })
        //初始化刷新
        f_home_smartRefreshLayout.setOnRefreshListener {
            canRefresh = true
            getDataList()
        }
        f_home_smartRefreshLayout.setDisableContentWhenRefresh(true)
        f_home_smartRefreshLayout.setDisableContentWhenLoading(true)
        f_home_smartRefreshLayout.setEnableLoadMore(false)
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
                    getSceneData(t.hotCityList[0])
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
    private fun getSceneData(cityName: String) {
        f_home_mustPlay_lay.visibility = View.GONE
        val rxHttp = RxHttp(context)
        addLifecycle(rxHttp)
        var map = mutableMapOf<String, String>()
        map["city"] = cityName
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
        f_home_hotCity1_tv.text = fHomeBean.hotCityList[0]
        f_home_hotCity2_tv.text = fHomeBean.hotCityList[1]
        f_home_hotCity3_tv.text = fHomeBean.hotCityList[2]
        f_home_hotCity4_tv.text = fHomeBean.hotCityList[3]
        f_home_hotCity5_tv.text = fHomeBean.hotCityList[4]
        showLine(1)
        list.removeAll(list)
        for (indexLoopInfo in fHomeBean.indexLoopInfoList) {
            list.add(XBannerBean(indexLoopInfo.name, indexLoopInfo.img))
        }
        //初始化轮播图
        f_home_xBanner.setBannerData(list)
        f_home_xBanner.loadImage { banner, model, view, position ->
            Glide.with(this@Home).load(list[position].url).into(view as ImageView)
        }
        f_home_xBanner.setOnItemClickListener { xBanner: XBanner, any: Any, view: View, i: Int ->
            ToastUtil.setToast(activity, "")
        }
    }

    /**
     * 设置景点数据
     */
    private fun setSceneData() {
        adapter = FHomeRecyclerViewAdapter(fHomeSceneBean.mustPlaySceneList, context)
//        f_home_recyclerView.setHasFixedSize(true)
//        f_home_recyclerView.isNestedScrollingEnabled=false
        f_home_recyclerView.layoutManager = GridLayoutManager(context, 3)
        f_home_recyclerView.adapter = adapter
        adapter!!.addItemClickListener(object : ItemClick<MustPlayScene> {
            override fun onItemClick(view: View?, t: MustPlayScene?, position: Int) {
                t?.name?.let { showToast(it) }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.f_home_hotCity1_lay -> {
                showLine(1)
                getSceneData(fHomeBean.hotCityList[0])
            }
            R.id.f_home_hotCity2_lay -> {
                showLine(2)
                getSceneData(fHomeBean.hotCityList[1])
            }
            R.id.f_home_hotCity3_lay -> {
                showLine(3)
                getSceneData(fHomeBean.hotCityList[2])
            }
            R.id.f_home_hotCity4_lay -> {
                showLine(4)
                getSceneData(fHomeBean.hotCityList[3])
            }
            R.id.f_home_hotCity5_lay -> {
                showLine(5)
                getSceneData(fHomeBean.hotCityList[4])
            }
            R.id.search_lay -> {
                //跳转到搜索界面
                startActivity(Intent(activity, Search::class.java))
            }
            R.id.f_home_moreCity_tv -> {
                //更多城市
                EventBus.getDefault().post(SwitchFragmentEvent(0, 1))
            }
            R.id.f_home_moreViewPoint_tv -> {
                //更多景点
                startActivity(Intent(activity, SceneList::class.java))
            }
        }
    }

    override fun getContentView(): View? {
        return f_home_ll
    }

    /**
     * 显示 热门城市选择的下划线
     */
    private fun showLine(showLine: Int) {
        when (showLine) {
            1 -> {
                f_home_hotCity1_tv.setTextColor(ContextCompat.getColor(context!!, R.color.black))
                f_home_hotCity1_v.visibility = View.VISIBLE
                f_home_hotCity2_tv.setTextColor(ContextCompat.getColor(context!!, R.color.gray_6))
                f_home_hotCity2_v.visibility = View.GONE
                f_home_hotCity3_tv.setTextColor(ContextCompat.getColor(context!!, R.color.gray_6))
                f_home_hotCity3_v.visibility = View.GONE
                f_home_hotCity4_tv.setTextColor(ContextCompat.getColor(context!!, R.color.gray_6))
                f_home_hotCity4_v.visibility = View.GONE
                f_home_hotCity5_tv.setTextColor(ContextCompat.getColor(context!!, R.color.gray_6))
                f_home_hotCity5_v.visibility = View.GONE
            }
            2 -> {
                f_home_hotCity1_tv.setTextColor(ContextCompat.getColor(context!!, R.color.gray_6))
                f_home_hotCity1_v.visibility = View.GONE
                f_home_hotCity2_tv.setTextColor(ContextCompat.getColor(context!!, R.color.black))
                f_home_hotCity2_v.visibility = View.VISIBLE
                f_home_hotCity3_tv.setTextColor(ContextCompat.getColor(context!!, R.color.gray_6))
                f_home_hotCity3_v.visibility = View.GONE
                f_home_hotCity4_tv.setTextColor(ContextCompat.getColor(context!!, R.color.gray_6))
                f_home_hotCity4_v.visibility = View.GONE
                f_home_hotCity5_tv.setTextColor(ContextCompat.getColor(context!!, R.color.gray_6))
                f_home_hotCity5_v.visibility = View.GONE
            }
            3 -> {
                f_home_hotCity1_tv.setTextColor(ContextCompat.getColor(context!!, R.color.gray_6))
                f_home_hotCity1_v.visibility = View.GONE
                f_home_hotCity2_tv.setTextColor(ContextCompat.getColor(context!!, R.color.gray_6))
                f_home_hotCity2_v.visibility = View.GONE
                f_home_hotCity3_tv.setTextColor(ContextCompat.getColor(context!!, R.color.black))
                f_home_hotCity3_v.visibility = View.VISIBLE
                f_home_hotCity4_tv.setTextColor(ContextCompat.getColor(context!!, R.color.gray_6))
                f_home_hotCity4_v.visibility = View.GONE
                f_home_hotCity5_tv.setTextColor(ContextCompat.getColor(context!!, R.color.gray_6))
                f_home_hotCity5_v.visibility = View.GONE
            }
            4 -> {
                f_home_hotCity1_tv.setTextColor(ContextCompat.getColor(context!!, R.color.gray_6))
                f_home_hotCity1_v.visibility = View.GONE
                f_home_hotCity2_tv.setTextColor(ContextCompat.getColor(context!!, R.color.gray_6))
                f_home_hotCity2_v.visibility = View.GONE
                f_home_hotCity3_tv.setTextColor(ContextCompat.getColor(context!!, R.color.gray_6))
                f_home_hotCity3_v.visibility = View.GONE
                f_home_hotCity4_tv.setTextColor(ContextCompat.getColor(context!!, R.color.black))
                f_home_hotCity4_v.visibility = View.VISIBLE
                f_home_hotCity5_tv.setTextColor(ContextCompat.getColor(context!!, R.color.gray_6))
                f_home_hotCity5_v.visibility = View.GONE
            }
            5 -> {
                f_home_hotCity1_tv.setTextColor(ContextCompat.getColor(context!!, R.color.gray_6))
                f_home_hotCity1_v.visibility = View.GONE
                f_home_hotCity2_tv.setTextColor(ContextCompat.getColor(context!!, R.color.gray_6))
                f_home_hotCity2_v.visibility = View.GONE
                f_home_hotCity3_tv.setTextColor(ContextCompat.getColor(context!!, R.color.gray_6))
                f_home_hotCity3_v.visibility = View.GONE
                f_home_hotCity4_tv.setTextColor(ContextCompat.getColor(context!!, R.color.gray_6))
                f_home_hotCity4_v.visibility = View.GONE
                f_home_hotCity5_tv.setTextColor(ContextCompat.getColor(context!!, R.color.black))
                f_home_hotCity5_v.visibility = View.VISIBLE
            }
        }
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

    override fun onDestroy() {
        super.onDestroy()
        f_home_xBanner.stopAutoPlay()
        if (null == timer) {
            timer!!.cancel()
        }
    }
}