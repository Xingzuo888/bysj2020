package com.example.bysj2020.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bysj2020.R
import com.example.bysj2020.adapter.HotelBookingTypeAdapter
import com.example.bysj2020.alipay.PayByALiPay
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.bean.HotelRoomInfos
import com.example.bysj2020.bean.PayInfoBean
import com.example.bysj2020.common.Format
import com.example.bysj2020.dialog.TimePickerDialog
import com.example.bysj2020.https.HttpResult
import com.example.bysj2020.https.RxHttp
import com.example.bysj2020.utils.DateUtil
import com.example.bysj2020.utils.VerifyUtils
import com.example.bysj2020.view.LinearItemDecoration
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_hotel_booking.*
import java.util.*

/**
 * Author : wxz
 * Time   : 2020/04/07
 * Desc   : 酒店预订
 */
class HotelBooking : BaseActivity() {
    private var json = ""
    private var money: Int = 0
    private var number: Int = 0 //用于判断选择的房间数
    private var timePickerDialog: TimePickerDialog? = null
    private lateinit var hotelRoomInfo: List<HotelRoomInfos>
    private lateinit var adapter: HotelBookingTypeAdapter
    override fun getLayoutId(): Int {
        return R.layout.activity_hotel_booking
    }

    override fun initViews() {
        json = intent.getStringExtra("json").toString()
        hotelRoomInfo = Gson().fromJson<List<HotelRoomInfos>>(json, object :
            TypeToken<List<HotelRoomInfos>>() {}.type)
        setBack()
        setTitle("酒店预订")
        hotel_booking_date.text =
            DateUtil.longToString(System.currentTimeMillis(), Format.YEAR_MONTH_DAY_CROSS)
        hotel_booking_money.text = "￥${money}"
        //初始化适配器
        adapter = HotelBookingTypeAdapter(hotelRoomInfo, this)
        val linearItemDecoration = LinearItemDecoration.Builder(this).setSpan(1f)
            .setColorResource(R.color.gray_e6)
            .setmShowLastLine(true)
            .build()

        hotel_booking_roomType.layoutManager = LinearLayoutManager(this)
        hotel_booking_roomType.addItemDecoration(linearItemDecoration)
        hotel_booking_roomType.adapter = adapter
    }

    override fun setViewClick() {
        hotel_booking_date.setOnClickListener(this)
        hotel_booking_submit.setOnClickListener(this)

        adapter.setClick(object : HotelBookingTypeAdapter.HotelBookingTypeNumClick {
            override fun reduceNum(position: Int) {
                money -= hotelRoomInfo[position].price
                number -= 1
                hotelRoomInfo[position].num -= 1
                hotel_booking_money.text = "￥${money}"
            }

            override fun addNum(position: Int) {
                money += hotelRoomInfo[position].price
                number += 1
                hotelRoomInfo[position].num += 1
                hotel_booking_money.text = "￥${money}"
            }
        })
    }

    override fun isRegisterEventBus(): Boolean {
        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.hotel_booking_date -> {
                //选择日期
                hideKeyboard()
                showDayDialog()
            }
            R.id.hotel_booking_submit -> {
                //提交订单
                hideKeyboard()
                if (verify()) {
                    submitOrder()
                }
            }
        }
    }

    private fun showDayDialog() {
        if (timePickerDialog == null) {
            timePickerDialog = TimePickerDialog("birthday", this)
        }
        timePickerDialog?.setTimerPickerCallBack(object : TimePickerDialog.TimePickerCallBack {
            override fun onTimeSelect(key: String, date: Date) {
                hotel_booking_date.text =
                    DateUtil.dateToString(date, Format.YEAR_MONTH_DAY_CROSS)
            }
        })
        timePickerDialog?.show()
    }

    /**
     * 提交订单前验证
     */
    private fun verify(): Boolean {
        if (number <= 0) {
            showToast("请选择要预订的房间类型")
            return false
        }
        if (hotel_booking_name.text.toString().isNullOrBlank()) {
            showToast("请输入真实姓名")
            return false
        }
        val day = DateUtil.stringToDate(
            hotel_booking_date.text.toString(),
            Format.YEAR_MONTH_DAY_CROSS
        )
        val current =
            DateUtil.longToDate(System.currentTimeMillis(), Format.YEAR_MONTH_DAY_CROSS)
        if (day != null && day < current) {
            showToast("出发时间不能小于当天时间!")
            return false
        }
        if (!VerifyUtils.verifyPhone(hotel_booking_phone.text.toString())) {
            showToast("请输入有效的手机号")
            return false
        }
        if (!VerifyUtils.verifyIdentity(hotel_booking_identity.text.toString())) {
            showToast("请输入有效身份证")
            return false
        }
        return true
    }

    /**
     * 提交订单
     */
    private fun submitOrder() {
        val rxHttp = RxHttp(this)
        addLifecycle(rxHttp)
        val body = JsonObject()
        val tickets = JsonArray()
        for (i in hotelRoomInfo.indices) {
            if (hotelRoomInfo[i].num > 0) {
                val ticket = JsonObject()
                ticket.addProperty("num", hotelRoomInfo[i].num)
                ticket.addProperty("orderId", hotelRoomInfo[i].id)
                tickets.add(ticket)
            }
        }
        body.add("orderInfoList", tickets)
        body.addProperty("name", hotel_booking_name.text.toString())
        body.addProperty("bookTime", hotel_booking_date.text.toString())
        body.addProperty("phone", hotel_booking_phone.text.toString())
        body.addProperty("idCard", hotel_booking_identity.text.toString())
        rxHttp.postWithJson("createHotelOrder", body, object : HttpResult<PayInfoBean> {
            override fun OnSuccess(t: PayInfoBean?, msg: String?) {
                if (t != null) {
                    if (t.orderInfo.isNotBlank()) {
                        val payByALiPay = PayByALiPay(this@HotelBooking)
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
