package com.example.bysj2020.activity

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bysj2020.Interface.BookingClickBack
import com.example.bysj2020.R
import com.example.bysj2020.adapter.SceneDetailsTicketAdapter
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.bean.SceneDetailsBean
import com.example.bysj2020.bean.SceneTickInfo
import com.example.bysj2020.bean.XBannerBean
import com.example.bysj2020.dialog.PopupBookingNotice
import com.example.bysj2020.https.HttpResult
import com.example.bysj2020.https.RxHttp
import com.example.bysj2020.statelayout.LoadHelper
import com.example.bysj2020.utils.LoadImageUtil
import kotlinx.android.synthetic.main.activity_scene_details.*

/**
 * Author : wxz
 * Time   : 2020/04/02
 * Desc   : 景点详情页
 */
class SceneDetails : BaseActivity() {

    private var sceneDetailsBean: SceneDetailsBean? = null
    private var sceneId = ""
    private var xBanners = mutableListOf<XBannerBean>()
    private var popupBookingNotice: PopupBookingNotice? = null
    private var bookingNotice = StringBuilder()
    private lateinit var ticketAdapter: SceneDetailsTicketAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_scene_details
    }

    override fun initViews() {
        sceneId = intent.getStringExtra("sceneId").toString()
        setBack()
        setTitle("景点详情")
        initStateLayout(object : LoadHelper.EmptyClickListener {
            override fun reload() {
                getData()
            }
        })
        getData()
    }

    override fun setViewClick() {
        scene_details_booking_notice.setOnClickListener(this)
    }

    override fun isRegisterEventBus(): Boolean {
        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.scene_details_booking_notice -> {
                //预订须知
                if (popupBookingNotice == null) {
                    popupBookingNotice =
                        PopupBookingNotice.Builder(this, window.decorView, bookingNotice.toString())
                }
                popupBookingNotice!!.show()
            }
        }
    }

    override fun getContentView(): View {
        return scene_details_rootView
    }

    /**
     * 初始化数据
     */
    private fun setData() {
        //初始化轮播图
        xBanners.removeAll(xBanners)
        for (i in sceneDetailsBean?.imgRespList!!.indices) {
            xBanners.add(XBannerBean("", sceneDetailsBean?.imgRespList!![i].imgPath))
        }
        scene_details_XBanner.setBannerData(xBanners)
        scene_details_XBanner.loadImage { banner, model, view, position ->
            LoadImageUtil(this).loadImage(
                view as ImageView,
                xBanners[position].url.toString()
            )
        }
        scene_details_name.text = sceneDetailsBean?.name
        scene_details_score_tv.text = sceneDetailsBean?.star.toString()
        scene_details_score_rb.rating = sceneDetailsBean?.star!!.toFloat()
        scene_details_score_ranking_tv.text = "第${sceneDetailsBean?.ranking}名"

        //标签
        val tags = sceneDetailsBean?.tag?.split(",")
        for (i in tags!!.indices) {
            val view = LayoutInflater.from(this).inflate(R.layout.item_float, null)
            view.findViewById<AppCompatTextView>(R.id.item_float_content).text = tags[i]
            scene_details_label_lay.addView(view)
        }

        scene_details_open_time.text = sceneDetailsBean?.openTime
        scene_details_address.text = sceneDetailsBean?.address
        scene_details_traffic_bus.text = sceneDetailsBean?.trafficBus
        scene_details_overView_content.text = sceneDetailsBean?.overview
        scene_details_rule_content.text = sceneDetailsBean?.rule
        scene_details_recommend_content.text = sceneDetailsBean?.recommend

        //是否显示预订列表
        if (sceneDetailsBean?.sceneTickInfoList != null && sceneDetailsBean?.sceneTickInfoList!!.isNotEmpty()) {
            //预订须知
            if (sceneDetailsBean?.bookNotice != null && sceneDetailsBean?.bookNotice!!.isNotEmpty()) {
                for (i in sceneDetailsBean?.bookNotice!!.indices) {
                    bookingNotice.append("${sceneDetailsBean?.bookNotice!![i].name}\n${sceneDetailsBean?.bookNotice!![i].value}\n")
                }
            }

            ticketAdapter = SceneDetailsTicketAdapter(sceneDetailsBean?.sceneTickInfoList!!, this)
            scene_details_booking_recycler.layoutManager = LinearLayoutManager(this)
            scene_details_booking_recycler.adapter = ticketAdapter
            ticketAdapter.setClickBack(object : BookingClickBack {
                override fun clickBack(data: Any) {
                    showToast((data as SceneTickInfo).name)
                }
            })
        } else {
            scene_details_booking_lay.visibility = View.GONE
        }
    }

    /**
     * 获取数据
     */
    private fun getData() {
        val rxHttp = RxHttp(this)
        addLifecycle(rxHttp)
        val map = mutableMapOf<String, Any>()
        map["sceneId"] = sceneId
        rxHttp.getWithJson("sceneDetail", map, object : HttpResult<SceneDetailsBean> {
            override fun OnSuccess(t: SceneDetailsBean?, msg: String?) {
                if (t == null) {
                    showEmpty()
                } else {
                    sceneDetailsBean = t
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
}
