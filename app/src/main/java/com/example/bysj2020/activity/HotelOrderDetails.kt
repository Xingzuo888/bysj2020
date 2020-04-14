package com.example.bysj2020.activity

import android.os.CountDownTimer
import android.view.View
import com.example.bysj2020.R
import com.example.bysj2020.alipay.PayByALiPay
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.bean.OrderDetailsBean
import com.example.bysj2020.bean.PayInfoBean
import com.example.bysj2020.event.UserInfoEvent
import com.example.bysj2020.https.HttpResult
import com.example.bysj2020.https.RxHttp
import com.example.bysj2020.statelayout.LoadHelper
import com.example.bysj2020.utils.DateUtil
import com.example.bysj2020.utils.LoadImageUtil
import com.google.gson.JsonObject
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import kotlinx.android.synthetic.main.activity_hotel_order_details.*
import org.greenrobot.eventbus.EventBus

/**
 * Author : wxz
 * Time   : 2020/04/11
 * Desc   : 酒店订单详情
 */
class HotelOrderDetails : BaseActivity() {
    private lateinit var loading: QMUITipDialog
    private var orderId = ""
    private lateinit var timer: CountDownTimer
    private lateinit var orderDetailsBean: OrderDetailsBean
    override fun getLayoutId(): Int {
        return R.layout.activity_hotel_order_details
    }

    override fun initViews() {
        orderId = intent.getStringExtra("orderId").toString()
        setBack()
        setTitle("酒店订单详情")
        initStateLayout(object : LoadHelper.EmptyClickListener {
            override fun reload() {
                getData()
            }
        })
        getData()
        loading = QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord("正在取消订单...")
            .create()
    }

    override fun setViewClick() {
        hotelOrderDetail_cancel.setOnClickListener(this)
        hotelOrderDetail_pay.setOnClickListener(this)
    }

    override fun isRegisterEventBus(): Boolean {
        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.hotelOrderDetail_cancel -> {
                //取消订单
                if (orderDetailsBean.status == "PAYING" || orderDetailsBean.status == "SUCCESS") {
                    cancelOrder()
                }
            }
            R.id.hotelOrderDetail_pay -> {
                //去支付
                if (orderDetailsBean.status == "PAYING") {
                    rePayOrder()
                }
            }
        }
    }

    override fun getContentView(): View {
        return hotelOrderDetail_scrollView
    }

    /**
     * 初始化数据
     */
    private fun setData() {
        LoadImageUtil(this).loadImage(
            hotelOrderDetail_img,
            orderDetailsBean.img,
            R.mipmap.default_picture
        )
        hotelOrderDetail_orderId.text = orderDetailsBean.bizNo
        hotelOrderDetail_hotelName.text = orderDetailsBean.name
        hotelOrderDetail_name.text = orderDetailsBean.realName
        hotelOrderDetail_date.text = orderDetailsBean.bookTime
        hotelOrderDetail_roomType.text = orderDetailsBean.typeName
        hotelOrderDetail_phone.text = orderDetailsBean.realPhone
        hotelOrderDetail_identity.text = orderDetailsBean.realIdCard

        when (orderDetailsBean.status) {
            "PAYING" -> {
                //支付中
                hotelOrderDetail_state.text = "支付中"
                hotelOrderDetail_bottom_lay.visibility = View.VISIBLE
                hotelOrderDetail_cancel.visibility = View.VISIBLE
                hotelOrderDetail_pay.visibility = View.VISIBLE
                lastTime(orderDetailsBean.validTime)
            }
            "SUCCESS" -> {
                //支付成功
                hotelOrderDetail_state.text = "支付成功"
                hotelOrderDetail_bottom_lay.visibility = View.VISIBLE
                hotelOrderDetail_cancel.visibility = View.VISIBLE
                hotelOrderDetail_pay.visibility = View.GONE
            }
            "FAIL" -> {
                //支付失败
                hotelOrderDetail_state.text = "支付失败"
                hotelOrderDetail_bottom_lay.visibility = View.GONE
            }
            "REFUND" -> {
                //已取消
                hotelOrderDetail_state.text = "已取消"
                hotelOrderDetail_bottom_lay.visibility = View.GONE
            }
            "TIME_OUT" -> {
                //已过期
                hotelOrderDetail_state.text = "已过期"
                hotelOrderDetail_bottom_lay.visibility = View.GONE
            }
            "NOT_EXISTS" -> {
                //不存在
                hotelOrderDetail_state.text = "不存在"
                hotelOrderDetail_bottom_lay.visibility = View.GONE
            }
        }
    }

    /**
     * 倒计时
     */
    private fun lastTime(time: Long) {
        timer = (object : CountDownTimer(time, 1000) {
            override fun onFinish() {
                hotelOrderDetail_lastTime.visibility = View.GONE
                getData()
                EventBus.getDefault().post(UserInfoEvent(4))
            }

            override fun onTick(millisUntilFinished: Long) {
                hotelOrderDetail_lastTime.text =
                    "还剩时间：${DateUtil.longToString(millisUntilFinished)}"
                hotelOrderDetail_lastTime.visibility = View.VISIBLE
            }

        })
    }

    /**
     * 获取数据
     */
    private fun getData() {
        val rxHttp = RxHttp(this)
        addLifecycle(rxHttp)
        val map = mutableMapOf<String, Any>()
        map["orderId"] = orderId
        map["type"] = 2 //1-景点 2-酒店
        rxHttp.getWithJson("orderDetail", map, object : HttpResult<OrderDetailsBean> {
            override fun OnSuccess(t: OrderDetailsBean?, msg: String?) {
                if (t == null) {
                    showEmpty()
                } else {
                    orderDetailsBean = t
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
     * 取消订单
     */
    private fun cancelOrder() {
        loading.show()
        val rxHttp = RxHttp(this)
        addLifecycle(rxHttp)
        val body = JsonObject()
        body.addProperty("orderId", orderId)
        rxHttp.postWithJson("cancelOrder", body, object : HttpResult<String> {
            override fun OnSuccess(t: String?, msg: String?) {
                loading.dismiss()
                showToast("订单取消成功")
                getData()
                EventBus.getDefault().post(UserInfoEvent(4))
            }

            override fun OnFail(code: Int, msg: String?) {
                loading.dismiss()
                showToast(msg!!)
            }
        })
    }

    /**
     * 去支付
     */
    private fun rePayOrder() {
        val rxHttp = RxHttp(this)
        addLifecycle(rxHttp)
        val body = JsonObject()
        body.addProperty("orderId", orderId)
        rxHttp.postWithJson("continueOrder", body, object : HttpResult<PayInfoBean> {
            override fun OnSuccess(t: PayInfoBean?, msg: String?) {
                if (t != null) {
                    if (t.orderInfo.isNotBlank()) {
                        val payByALiPay = PayByALiPay(this@HotelOrderDetails)
                        payByALiPay.pay(t.orderInfo)
                    }
                }
            }

            override fun OnFail(code: Int, msg: String?) {
                showToast(msg!!)
            }
        })
    }
}
