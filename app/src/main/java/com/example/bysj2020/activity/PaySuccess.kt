package com.example.bysj2020.activity

import android.content.Intent
import android.view.View
import com.example.bysj2020.R
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.event.UserInfoEvent
import com.example.bysj2020.utils.ActivityManagerUtil
import kotlinx.android.synthetic.main.activity_pay_success.*
import org.greenrobot.eventbus.EventBus

/**
 * Author : wxz
 * Time   : 2020/04/14
 * Desc   : 支付成功界面
 */
class PaySuccess : BaseActivity() {
    private var name: String = ""
    private var orderId: String = ""
    override fun getLayoutId(): Int {
        return R.layout.activity_pay_success
    }

    override fun initViews() {
        name = intent.getStringExtra("name").toString()
        orderId = intent.getStringExtra("orderId").toString()

        setBack()
        if (name == "SceneBooking" || name == "HotelBooking") {
            pay_success_complete.visibility = View.VISIBLE
            pay_success_lookOrder.visibility = View.VISIBLE
        } else {
            pay_success_complete.visibility = View.VISIBLE
            pay_success_lookOrder.visibility = View.GONE
        }
    }

    override fun setViewClick() {
        pay_success_complete.setOnClickListener(this)
        pay_success_lookOrder.setOnClickListener(this)
    }

    override fun isRegisterEventBus(): Boolean {
        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.pay_success_complete -> {
                if (name == "SceneOrderDetails") {
                    EventBus.getDefault().post(UserInfoEvent(3))
                } else if (name == "HotelOrderDetails") {
                    EventBus.getDefault().post(UserInfoEvent(4))
                }
                ActivityManagerUtil.destroyActivityIndex(2)
            }
            R.id.pay_success_lookOrder -> {
                if (name == "SceneBooking") {
                    startActivity(
                        Intent(this, SceneOrderDetails::class.java).putExtra(
                            "orderId",
                            orderId
                        )
                    )
                    ActivityManagerUtil.destroyActivityIndex(3)
                } else if (name == "HotelBooking") {
                    startActivity(
                        Intent(this, HotelOrderDetails::class.java).putExtra(
                            "orderId",
                            orderId
                        )
                    )
                    ActivityManagerUtil.destroyActivityIndex(3)
                }
            }
        }
    }

}
