package com.example.bysj2020.activity

import android.view.View
import com.example.bysj2020.R
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.utils.ActivityManagerUtil
import kotlinx.android.synthetic.main.activity_pay_success.*

class PaySuccess : BaseActivity() {
    private var name:String=""
    override fun getLayoutId(): Int {
        return R.layout.activity_pay_success
    }

    override fun initViews() {
        name=intent.getStringExtra("name").toString()
        setBack()
        if (name == "SceneBooking" && name == "HotelBooking") {
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
            R.id.pay_success_complete->{
                if (name == "SceneBooking" && name == "HotelBooking") {
                    ActivityManagerUtil.destroyActivityIndex(2)
                } else {
                    finish()
                }
            }
            R.id.pay_success_lookOrder->{

            }
        }
    }

}
