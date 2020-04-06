package com.example.bysj2020.activity

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bysj2020.Interface.BookingClickBack
import com.example.bysj2020.R
import com.example.bysj2020.adapter.HotelDetailsRoomAdapter
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.bean.HotelDetailsBean
import com.example.bysj2020.bean.HotelPolicyBean
import com.example.bysj2020.bean.HotelRoomInfo
import com.example.bysj2020.bean.XBannerBean
import com.example.bysj2020.https.HttpResult
import com.example.bysj2020.https.RxHttp
import com.example.bysj2020.statelayout.LoadHelper
import com.example.bysj2020.utils.LoadImageUtil
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.stx.xhb.androidx.XBanner
import kotlinx.android.synthetic.main.activity_hotel_details.*

/**
 * Author : wxz
 * Time   : 2020/04/02
 * Desc   : 酒店详情页
 */
class HotelDetails : BaseActivity() {

    private var hotelDetailsBean: HotelDetailsBean? = null
    private var hotelId: String = ""
    private var xBanners = mutableListOf<XBannerBean>()
    private lateinit var roomAdapter: HotelDetailsRoomAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_hotel_details
    }

    override fun initViews() {
        hotelId = intent.getStringExtra("hotelId").toString()
        setBack()
        setTitle("酒店详情")
        initStateLayout(object : LoadHelper.EmptyClickListener {
            override fun reload() {
                getData()
            }
        })
        getData()
    }

    override fun setViewClick() {
    }

    override fun isRegisterEventBus(): Boolean {
        return false
    }

    override fun onClick(v: View?) {
    }

    override fun getContentView(): View {
        return hotel_details_scrollView
    }

    /**
     * 设置数据
     */
    private fun setData() {
        //初始化轮播图
        xBanners.removeAll(xBanners)
        for (i in hotelDetailsBean?.img!!.indices) {
            xBanners.add(XBannerBean("", hotelDetailsBean?.img!![i].imgPath))
        }
        hotel_details_XBanner.setBannerData(xBanners)
        hotel_details_XBanner.loadImage { banner, model, view, position ->
            LoadImageUtil(this@HotelDetails).loadImage(
                view as ImageView,
                xBanners[position].url.toString()
            )
        }

        hotel_details_name.text = hotelDetailsBean?.name
        hotel_details_score_tv.text = hotelDetailsBean?.star.toString()
        hotel_details_score_searchNum_tv.text = "搜索次数：${hotelDetailsBean?.searchNum}"
        hotel_details_address.text = hotelDetailsBean?.address
        hotel_details_traffic_bus.visibility = View.GONE
        hotel_details_open_time.text = "${hotelDetailsBean?.debutYear}年开业"
        hotel_details_update_time.text = "${hotelDetailsBean?.decorateDate}年装修"
        hotel_details_type_star.text = hotelDetailsBean?.starName
        hotel_details_type_room.text = "${hotelDetailsBean?.roomCount}房间"
        hotel_details_tel.text = "酒店电话：${hotelDetailsBean?.tel}"

        //相关服务
        for (i in hotelDetailsBean?.hotelServiceInfoList!!.indices) {
            val view = LayoutInflater.from(this).inflate(R.layout.item_float, null)
            view.findViewById<AppCompatTextView>(R.id.item_float_content).text =
                hotelDetailsBean?.hotelServiceInfoList!![i].name
            hotel_details_service.addView(view)
        }

        //标签
        val tags = hotelDetailsBean?.tag?.split(",")
        for (i in tags!!.indices) {
            val view = LayoutInflater.from(this).inflate(R.layout.item_float, null)
            view.findViewById<AppCompatTextView>(R.id.item_float_content).text = tags[i]
            hotel_details_label_lay.addView(view)
        }

        hotel_details_overView_content.text = hotelDetailsBean?.overview
        //设施项目
        for (i in hotelDetailsBean?.facilityInfoList!!.indices) {
            val view = LayoutInflater.from(this).inflate(R.layout.item_float, null)
            view.findViewById<AppCompatTextView>(R.id.item_float_content).text =
                hotelDetailsBean?.facilityInfoList!![i].name
            hotel_details_facilities_content.addView(view)
        }

        //是否显示房间列表
        //房间类型
        if (hotelDetailsBean?.hotelRoomInfoList != null && hotelDetailsBean?.hotelRoomInfoList!!.isNotEmpty()) {
            roomAdapter = HotelDetailsRoomAdapter(hotelDetailsBean?.hotelRoomInfoList!!, this)
            hotel_details_booking_recycler.layoutManager = LinearLayoutManager(this)
            hotel_details_booking_recycler.adapter = roomAdapter
            roomAdapter.setClickBack(object : BookingClickBack {
                override fun clickBack(data: Any) {
                    showToast((data as HotelRoomInfo).roomType)
                }
            })
        } else {
            hotel_details_booking_lay.visibility = View.GONE
        }

        //相关政策
        val hotelPolicyBean =
            Gson().fromJson<HotelPolicyBean>(hotelDetailsBean?.policy, HotelPolicyBean::class.java)
        hotel_details_policy_content.text =
            "${hotelPolicyBean.cancel}\n${hotelPolicyBean.requirements}\n${hotelPolicyBean.checkInTime}\n${hotelPolicyBean.checkOutTime}\n${hotelPolicyBean.arrivalDeparture}\n${hotelPolicyBean.depositPrepaid}\n${hotelPolicyBean.pet}\n${hotelPolicyBean.children}\n"

        hotel_details_related_lay.visibility = View.GONE
    }

    /**
     * 获取数据
     */
    private fun getData() {
        val rxHttp = RxHttp(this)
        addLifecycle(rxHttp)
        val map = mutableMapOf<String, Any>()
        map["hotelId"] = hotelId
        rxHttp.getWithJson("hotelDetail", map, object : HttpResult<HotelDetailsBean> {
            override fun OnSuccess(t: HotelDetailsBean?, msg: String?) {
                if (t == null) {
                    showEmpty()
                } else {
                    hotelDetailsBean = t
                    setData()
                    showContent()
                }
            }

            override fun OnFail(code: Int, msg: String?) {
                showToast(msg!!)
                showError()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        hotel_details_XBanner.startAutoPlay()
    }

    override fun onPause() {
        super.onPause()
        hotel_details_XBanner.stopAutoPlay()
    }
}
