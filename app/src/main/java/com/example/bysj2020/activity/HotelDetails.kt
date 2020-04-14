package com.example.bysj2020.activity

import android.content.Intent
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
import com.example.bysj2020.bean.XBannerBean
import com.example.bysj2020.event.UserInfoEvent
import com.example.bysj2020.https.HttpResult
import com.example.bysj2020.https.RxHttp
import com.example.bysj2020.statelayout.LoadHelper
import com.example.bysj2020.utils.LoadImageUtil
import com.example.bysj2020.utils.SpUtil
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import kotlinx.android.synthetic.main.activity_hotel_details.*
import org.greenrobot.eventbus.EventBus

/**
 * Author : wxz
 * Time   : 2020/04/02
 * Desc   : 酒店详情页
 */
class HotelDetails : BaseActivity() {

    private var loading: QMUITipDialog? = null
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
        loading = QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord("正在处理收藏...")
            .create()
        getData()
    }

    override fun setViewClick() {
        //收藏
        hotel_details_favorite.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isLogin()) {
                hotel_details_favorite.isChecked = false
                return@setOnCheckedChangeListener
            }
            if (isChecked) {
                setFavorite(1)
            } else {
                setFavorite(2)
            }
        }
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
        if (hotelDetailsBean?.hotelServiceInfoList != null && hotelDetailsBean?.hotelServiceInfoList!!.isNotEmpty()) {
            for (i in hotelDetailsBean?.hotelServiceInfoList!!.indices) {
                val view = LayoutInflater.from(this).inflate(R.layout.item_float, null)
                view.findViewById<AppCompatTextView>(R.id.item_float_content).text =
                    hotelDetailsBean?.hotelServiceInfoList!![i].name
                hotel_details_service.addView(view)
            }
        } else {
            hotel_details_service_lay.visibility = View.GONE
        }

        //标签
        if (hotelDetailsBean?.tag != null && hotelDetailsBean?.tag!!.isNotBlank()) {
            val tags = hotelDetailsBean?.tag?.split(",")
            for (i in tags!!.indices) {
                val view = LayoutInflater.from(this).inflate(R.layout.item_tag, null)
                view.findViewById<AppCompatTextView>(R.id.item_tag_content).text = tags[i]
                hotel_details_label_lay.addView(view)
            }
        } else {
            hotel_details_label_lay.visibility = View.GONE
        }

        hotel_details_overView_content.text = hotelDetailsBean?.overview
        //设施项目
        if (hotelDetailsBean?.facilityInfoList != null && hotelDetailsBean?.facilityInfoList!!.isNotEmpty()) {
            for (i in hotelDetailsBean?.facilityInfoList!!.indices) {
                val view = LayoutInflater.from(this).inflate(R.layout.item_float, null)
                view.findViewById<AppCompatTextView>(R.id.item_float_content).text =
                    hotelDetailsBean?.facilityInfoList!![i].name
                hotel_details_facilities_content.addView(view)
            }
        } else {
            hotel_details_facilities_lay.visibility = View.GONE
        }

        //是否显示房间列表
        //房间类型
        if (hotelDetailsBean?.hotelRoomInfoList != null && hotelDetailsBean?.hotelRoomInfoList!!.isNotEmpty()) {
            roomAdapter = HotelDetailsRoomAdapter(hotelDetailsBean?.hotelRoomInfoList!!, this)
            hotel_details_booking_recycler.layoutManager = LinearLayoutManager(this)
            hotel_details_booking_recycler.adapter = roomAdapter
            roomAdapter.setClickBack(object : BookingClickBack {
                override fun clickBack(data: Any) {
                    if (isLogin()) {
                        return
                    }
                    val json = Gson().toJson(hotelDetailsBean?.hotelRoomInfoList)
                    startActivity(
                        Intent(this@HotelDetails, HotelBooking::class.java).putExtra(
                            "json",
                            json
                        )
                    )
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
        hotel_details_favorite.isChecked = hotelDetailsBean?.isSave!!
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

    /**
     * 收藏
     */
    private fun setFavorite(type: Int) {
        loading?.show()
        val rxHttp = RxHttp(this)
        addLifecycle(rxHttp)
        val body = JsonObject()
        body.addProperty("hotelId", hotelId)
        body.addProperty("type", type) //1-收藏 2- 取消
        rxHttp.postWithJson("saveHotel", body, object : HttpResult<String> {
            override fun OnSuccess(t: String?, msg: String?) {
                loading?.dismiss()
                showToast(if (type == 1) "收藏成功" else "已取消收藏")
                EventBus.getDefault().post(UserInfoEvent(2))
            }

            override fun OnFail(code: Int, msg: String?) {
                loading?.dismiss()
                showToast(msg!!)
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

    /**
     * 判断是否登录
     */
    private fun isLogin(): Boolean {
        val loginToken = SpUtil.Obtain(this@HotelDetails, "loginToken", "").toString()
        if (loginToken.isBlank()) {
            startActivity(
                Intent(
                    this@HotelDetails,
                    LoginVerificationCode::class.java
                ).putExtra("isBackArrow", true)
            )
            return true
        }
        return false
    }
}
