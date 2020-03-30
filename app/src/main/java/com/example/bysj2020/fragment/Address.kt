package com.example.bysj2020.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bysj2020.Interface.ItemClick
import com.example.bysj2020.R
import com.example.bysj2020.activity.Area
import com.example.bysj2020.activity.Search
import com.example.bysj2020.adapter.FAddressHotelAdapter
import com.example.bysj2020.adapter.FAddressSceneAdapter
import com.example.bysj2020.base.BaseFragment
import com.example.bysj2020.bean.FAddressBean
import com.example.bysj2020.bean.HotelImgTopInfo
import com.example.bysj2020.bean.SceneImgTopInfo
import com.example.bysj2020.https.HttpResult
import com.example.bysj2020.https.RxHttp
import com.example.bysj2020.statelayout.LoadHelper
import kotlinx.android.synthetic.main.fragment_address.*

/**
 *    Author : wxz
 *    Time   : 2020/02/25
 *    Desc   : 目的地fragment
 */
class Address : BaseFragment() {

    private lateinit var fAddressBean: FAddressBean
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
        initStateLayout(object : LoadHelper.EmptyClickListener {
            override fun reload() {
                getDataList()
            }

        })
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
                        false
                    ), 1
                )
            }
            R.id.f_address_search -> {
                //搜索
                startActivity(Intent(activity, Search::class.java))
            }
            R.id.f_address_moreScene_tv -> {
                //更多景点
            }
            R.id.f_address_moreHotel_tv -> {
                //更多酒店
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
        if (fAddressBean.sceneImgTopInfoList != null && fAddressBean.sceneImgTopInfoList.isNotEmpty()) {
            f_address_scene_lay.visibility = View.VISIBLE
            if (sceneAdapter == null) {
                sceneAdapter = FAddressSceneAdapter(fAddressBean.sceneImgTopInfoList, context!!)
                sceneAdapter?.addItemClickListener(object : ItemClick<SceneImgTopInfo> {
                    override fun onItemClick(view: View?, t: SceneImgTopInfo?, position: Int) {
                        showToast(t!!.name)
                    }
                })
                f_address_scene_recycler.adapter = sceneAdapter
                f_address_scene_recycler.layoutManager = GridLayoutManager(context, 3)
            }
            sceneAdapter?.notifyDataSetChanged()
        } else {
            f_address_scene_lay.visibility = View.GONE
        }
    }

    /**
     * 初始化酒店信息
     */
    private fun initHotelInfo() {
        if (fAddressBean.hotelImgTopInfos != null && fAddressBean.hotelImgTopInfos.isNotEmpty()) {
            f_address_hotel_lay.visibility = View.VISIBLE
            if (hotelAdapter == null) {
                hotelAdapter = FAddressHotelAdapter(fAddressBean.hotelImgTopInfos, context!!)
                hotelAdapter?.addItemClickListener(object : ItemClick<HotelImgTopInfo> {
                    override fun onItemClick(view: View?, t: HotelImgTopInfo?, position: Int) {
                        showToast(t!!.name)
                    }
                })
                f_address_hotel_recycler.adapter = hotelAdapter
                f_address_hotel_recycler.layoutManager = GridLayoutManager(context, 3)
            }
            hotelAdapter?.notifyDataSetChanged()
        } else {
            f_address_hotel_lay.visibility = View.GONE
        }
    }

    /**
     * 获取数据
     */
    fun getDataList() {
        val rxHttp = RxHttp(activity)
        addLifecycle(rxHttp)
        val map = mutableMapOf<String, String>()
        map["city"] = f_address_positionName_tv.text.toString()
        rxHttp.getWithJson("mdd", map, object : HttpResult<FAddressBean> {
            override fun OnSuccess(t: FAddressBean?, msg: String?) {
                if (t == null) {
                    showEmpty()
                } else {
                    fAddressBean = t
                    initSceneInfo()
                    initHotelInfo()
                    showContent()
                }
                if (f_address_smartRefreshLayout.isRefreshing) {
                    f_address_smartRefreshLayout.finishRefresh()
                }
            }

            override fun OnFail(code: Int, msg: String?) {
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
                }
            }
        }
    }
}