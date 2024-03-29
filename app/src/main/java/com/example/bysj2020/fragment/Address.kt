package com.example.bysj2020.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bysj2020.Interface.ItemClick
import com.example.bysj2020.R
import com.example.bysj2020.activity.*
import com.example.bysj2020.adapter.FAddressHotelAdapter
import com.example.bysj2020.adapter.FAddressSceneAdapter
import com.example.bysj2020.base.BaseFragment
import com.example.bysj2020.bean.FAddressBean
import com.example.bysj2020.bean.HotelImgTopInfo
import com.example.bysj2020.bean.SceneImgTopInfo
import com.example.bysj2020.event.LocationEvent
import com.example.bysj2020.https.HttpResult
import com.example.bysj2020.https.RxHttp
import com.example.bysj2020.statelayout.LoadHelper
import com.example.bysj2020.utils.SpUtil
import kotlinx.android.synthetic.main.fragment_address.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *    Author : wxz
 *    Time   : 2020/02/25
 *    Desc   : 目的地fragment
 */
class Address : BaseFragment() {

    private var scenes: MutableList<SceneImgTopInfo> = ArrayList()
    private var hotels: MutableList<HotelImgTopInfo> = ArrayList()
    private var sceneAdapter: FAddressSceneAdapter? = null
    private var hotelAdapter: FAddressHotelAdapter? = null

    private var cityId: String = "0"
    private var cityName: String = ""
    private var provincesId: String = ""
    private var provincesName: String = ""

    companion object {
        fun newInstance(): Address {
            return Address()
        }
    }

    override fun loadData() {
        showLoading()
        getDataList()
    }

    override fun setViewClick() {
        f_address_positionName_tv.setOnClickListener(this)
        f_address_search.setOnClickListener(this)
        f_address_moreScene_tv.setOnClickListener(this)
        f_address_moreHotel_tv.setOnClickListener(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_address
    }

    override fun initViews() {
        EventBus.getDefault().register(this)
        initStateLayout(object : LoadHelper.EmptyClickListener {
            override fun reload() {
                getDataList()
            }

        })
        val city = SpUtil.Obtain(context, "city", "").toString()
        if (city.isNotBlank()) {
            f_address_positionName_tv.text = city
        }
        //初始化刷新
        f_address_smartRefreshLayout.setOnRefreshListener {
            getDataList()
        }
        f_address_smartRefreshLayout.setDisableContentWhenRefresh(true)
        f_address_smartRefreshLayout.setDisableContentWhenLoading(true)
        f_address_smartRefreshLayout.setEnableLoadMore(false)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.f_address_positionName_tv -> {
                //选择城市
                startActivityForResult(
                    Intent(activity, Area::class.java).putExtra(
                        "PreciseChoice",
                        true
                    ), 1
                )
            }
            R.id.f_address_search -> {
                //搜索
                startActivity(Intent(activity, Search::class.java))
            }
            R.id.f_address_moreScene_tv -> {
                //更多景点
                startActivity(
                    Intent(activity, SceneList::class.java).putExtra(
                        "city",
                        f_address_positionName_tv.text.toString()
                    )
                )
            }
            R.id.f_address_moreHotel_tv -> {
                //更多酒店
                startActivity(
                    Intent(activity, HotelList::class.java).putExtra(
                        "city",
                        f_address_positionName_tv.text.toString()
                    )
                )
            }
        }
    }

    override fun getContentView(): View? {
        return f_address_ll
    }

    /**
     * 初始化景点信息
     */
    private fun initSceneInfo() {
        if (sceneAdapter == null) {
            sceneAdapter = FAddressSceneAdapter(scenes!!, context!!)
            sceneAdapter?.addItemClickListener(object : ItemClick<SceneImgTopInfo> {
                override fun onItemClick(view: View?, t: SceneImgTopInfo?, position: Int) {
                    startActivity(
                        Intent(
                            activity,
                            SceneDetails::class.java
                        ).putExtra("sceneId", t!!.sceneId.toString())
                    )
                }
            })

            f_address_scene_recycler.adapter = sceneAdapter
            f_address_scene_recycler.layoutManager = GridLayoutManager(context, 3)
            f_address_scene_recycler.isNestedScrollingEnabled=false
        }
        sceneAdapter?.notifyDataSetChanged()
    }

    /**
     * 初始化酒店信息
     */
    private fun initHotelInfo() {
        if (hotelAdapter == null) {
            hotelAdapter = FAddressHotelAdapter(hotels!!, context!!)
            hotelAdapter?.addItemClickListener { view, t, position ->
                startActivity(
                    Intent(
                        activity,
                        HotelDetails::class.java
                    ).putExtra("hotelId", t!!.hotelId.toString())
                )
            }
            f_address_hotel_recycler.layoutManager = GridLayoutManager(context, 3)
            f_address_hotel_recycler.adapter = hotelAdapter
            f_address_hotel_recycler.isNestedScrollingEnabled=false
        }
        hotelAdapter?.notifyDataSetChanged()
    }

    /**
     * 获取数据
     */
    fun getDataList() {
        val rxHttp = RxHttp(activity)
        addLifecycle(rxHttp)
        val map = mutableMapOf<String, Any>()
        map["city"] = f_address_positionName_tv.text.toString()
        map["sceneNum"]=12 // 景点的数量
        map["hotelNum"]=12 //酒店的数量
        rxHttp.getWithJson("mdd", map, object : HttpResult<FAddressBean> {
            override fun OnSuccess(t: FAddressBean?, msg: String?) {
                if (f_address_smartRefreshLayout.isRefreshing) {
                    f_address_smartRefreshLayout.finishRefresh()
                }
                if (t == null) {
                    showEmpty()
                } else {
                    if (t.sceneImgTopInfoList.isEmpty() && t.hotelImgTopInfos.isEmpty()) {
                        showEmpty()
                    } else {
                        if (t.sceneImgTopInfoList.isEmpty()) {
                            f_address_scene_lay.visibility = View.GONE
                        } else {
                            scenes.removeAll(scenes)
                            scenes.addAll(t.sceneImgTopInfoList)
                            initSceneInfo()
                            f_address_scene_lay.visibility = View.VISIBLE
                        }
                        if (t.hotelImgTopInfos.isEmpty()) {
                            f_address_hotel_lay.visibility = View.GONE
                        } else {
                            hotels.removeAll(hotels)
                            hotels.addAll(t.hotelImgTopInfos)
                            initHotelInfo()
                            f_address_hotel_lay.visibility = View.VISIBLE
                        }
                        showContent()
                    }
                }
            }

            override fun OnFail(code: Int, msg: String?) {
                if (f_address_smartRefreshLayout.isRefreshing) {
                    f_address_smartRefreshLayout.finishRefresh()
                }
                showError()
                showToast(msg!!)
            }
        })
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
                    f_address_positionName_tv.text =
                        if (cityName == "不限") provincesName else cityName
                    getDataList()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun locationEvent(event: LocationEvent) {
        when (event.code) {
            3 -> {
                f_address_positionName_tv.text = if (event.str.endsWith("市")) {
                    event.str.substring(0, (event.str.length - 1))
                } else {
                    event.str
                }
                getDataList()
            }
        }
    }
}