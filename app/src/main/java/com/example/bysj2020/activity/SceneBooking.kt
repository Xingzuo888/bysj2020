package com.example.bysj2020.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bysj2020.R
import com.example.bysj2020.adapter.SceneBookingTypeAdapter
import com.example.bysj2020.alipay.PayByALiPay
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.bean.PayInfoBean
import com.example.bysj2020.bean.SceneTickInfos
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
import kotlinx.android.synthetic.main.activity_scene_booking.*
import java.util.*

/**
 * Author : wxz
 * Time   : 2020/04/07
 * Desc   : 景点门票预订
 */
class SceneBooking : BaseActivity() {
    private var json = ""
    private var selectId: Int = 0
    private var money: Int = 0
    private var number: Int = 1 //用于判断选择的票数
    private var timePickerDialog: TimePickerDialog? = null
    private lateinit var sceneTickInfoList: List<SceneTickInfos>
    private lateinit var adapter: SceneBookingTypeAdapter
    override fun getLayoutId(): Int {
        return R.layout.activity_scene_booking
    }

    override fun initViews() {
        selectId = intent.getIntExtra("selectId", 0)
        money = intent.getIntExtra("money", 0)
        json = intent.getStringExtra("json").toString()
        sceneTickInfoList = Gson().fromJson<List<SceneTickInfos>>(
            json,
            object : TypeToken<List<SceneTickInfos>>() {}.type
        )
        setBack()
        setTitle("景点门票")
        //初始化数据
        for (i in sceneTickInfoList.indices) {
            if (sceneTickInfoList[i].id == selectId) {
                sceneTickInfoList[i].num = 1
            }
        }
        scene_booking_date.text =
            DateUtil.longToString(System.currentTimeMillis(), Format.YEAR_MONTH_DAY_CROSS)
        scene_booking_money.text = "￥${money}"
        //初始化适配器
        adapter = SceneBookingTypeAdapter(sceneTickInfoList, this, selectId)
        val linearItemDecoration = LinearItemDecoration.Builder(this).setSpan(1f)
            .setColorResource(R.color.gray_e6)
            .setmShowLastLine(true)
            .build()
        scene_booking_ticketType.layoutManager = LinearLayoutManager(this)
        scene_booking_ticketType.addItemDecoration(linearItemDecoration)
        scene_booking_ticketType.adapter = adapter
    }

    override fun setViewClick() {
        scene_booking_date.setOnClickListener(this)
        scene_booking_submit.setOnClickListener(this)

        adapter.setClick(object : SceneBookingTypeAdapter.SceneBookingTypeNumClick {
            override fun reduceNum(position: Int) {
                money -= sceneTickInfoList[position].price
                number -= 1
                sceneTickInfoList[position].num -= 1
                scene_booking_money.text = "￥${money}"
            }

            override fun addNum(position: Int) {
                money += sceneTickInfoList[position].price
                number += 1
                sceneTickInfoList[position].num += 1
                scene_booking_money.text = "￥${money}"
            }
        })
    }

    override fun isRegisterEventBus(): Boolean {
        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.scene_booking_date -> {
                //选择日期
                hideKeyboard()
                showdayDialog()
            }
            R.id.scene_booking_submit -> {
                //提交订单
                hideKeyboard()
                if (verify()) {
                    submitOrder()
                }
            }
        }
    }

    private fun showdayDialog() {
        if (timePickerDialog == null) {
            timePickerDialog = TimePickerDialog("birthday", this)
        }
        timePickerDialog?.setTimerPickerCallBack(object : TimePickerDialog.TimePickerCallBack {
            override fun onTimeSelect(key: String, date: Date) {
                scene_booking_date.text =
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
            showToast("请选择要预订的票类型")
            return false
        }
        if (scene_booking_name.text.toString().isNullOrBlank()) {
            showToast("请输入真实姓名")
            return false
        }
        val day = DateUtil.stringToDate(
            scene_booking_date.text.toString(),
            Format.YEAR_MONTH_DAY_CROSS
        )
        val current =
            DateUtil.longToDate(System.currentTimeMillis(), Format.YEAR_MONTH_DAY_CROSS)
        if (day != null && day < current) {
            showToast("出发时间不能小于当天时间!")
            return false
        }
        if (!VerifyUtils.verifyPhone(scene_booking_phone.text.toString())) {
            showToast("请输入有效的手机号")
            return false
        }
        if (!VerifyUtils.verifyIdentity(scene_booking_identity.text.toString())) {
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
        val roomTypes = JsonArray()
        for (i in sceneTickInfoList.indices) {
            if (sceneTickInfoList[i].num > 0) {
                val roomType = JsonObject()
                roomType.addProperty("num", sceneTickInfoList[i].num)
                roomType.addProperty("orderId", sceneTickInfoList[i].id)
                roomTypes.add(roomType)
            }
        }
        body.add("orderInfoList", roomTypes)
        body.addProperty("name", scene_booking_name.text.toString())
        body.addProperty("bookTime", scene_booking_date.text.toString())
        body.addProperty("phone", scene_booking_phone.text.toString())
        body.addProperty("idCard", scene_booking_identity.text.toString())
        rxHttp.postWithJson("createSceneOrder", body, object : HttpResult<PayInfoBean> {
            override fun OnSuccess(t: PayInfoBean?, msg: String?) {
                if (t != null) {
                    if (t.orderId.isNotBlank() && t.orderInfo.isNotBlank()) {
                        val payByALiPay = PayByALiPay(this@SceneBooking, t.orderId)
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
