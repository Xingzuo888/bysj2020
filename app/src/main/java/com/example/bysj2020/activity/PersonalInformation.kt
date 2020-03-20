package com.example.bysj2020.activity

import android.view.View
import com.example.bysj2020.R
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.common.Format
import com.example.bysj2020.dialog.TimePickerDialog
import com.example.bysj2020.dialog.WheelDialog
import com.example.bysj2020.statelayout.LoadHelper
import com.example.bysj2020.utils.DateUtil
import kotlinx.android.synthetic.main.activity_personal_information.*
import java.util.*

/**
 *    Author : wxz
 *    Time   : 2020/03/01
 *    Desc   : 个人信息
 */
class PersonalInformation : BaseActivity() {

    private var wheelDialog: WheelDialog? = null
    private var timePickerDialog: TimePickerDialog? = null
    private var list: List<String> = arrayListOf("未知", "男", "女")

    override fun getLayoutId(): Int {
        return R.layout.activity_personal_information
    }

    override fun isRegisterEventBus(): Boolean {
        return false
    }

    override fun initViews() {
        setTitle("个人信息")
        setBack()
        setRightText("保存")
        initStateLayout(object : LoadHelper.EmptyClickListener {
            override fun reload() {
                getDataList()
            }

        })
        getDataList()
    }

    override fun setViewClick() {
        personal_info_sex_lay.setOnClickListener(this)
        personal_info_birthday_lay.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.personal_info_sex_lay -> {
                //选择性别
                showSexDialog()
            }
            R.id.personal_info_birthday_lay -> {
                //选择出生日期
                showBirthdayDialog()
            }
        }
    }

    /**
     * 显示性别选择器弹框
     */
    private fun showSexDialog() {
        if (wheelDialog == null) {
            wheelDialog = WheelDialog("性别", this)
        }
        wheelDialog?.setWheelCallBack(object : WheelDialog.WheelCallBack {
            override fun onSelect(key: String, options1: Int, options2: Int, options3: Int) {
                personal_info_sex_tv.text = list[options1]
            }
        })
        wheelDialog?.setOptionsItems(list)
        wheelDialog?.show()
    }

    /**
     * 显示日历选择器弹框
     */
    private fun showBirthdayDialog() {
        if (timePickerDialog == null) {
            timePickerDialog = TimePickerDialog("birthday", this)
        }
        timePickerDialog?.setTimerPickerCallBack(object : TimePickerDialog.TimePickerCallBack {
            override fun onTimeSelect(key: String, date: Date) {
                personal_info_birthday_tv.text = DateUtil.dateToString(date,Format.YEAR_MONTH_DAY_CROSS)
            }
        })
        timePickerDialog?.show()
    }

    override fun getContentView(): View {
        return personal_info_scrollView
    }

    private fun getDataList() {
        showContent()
    }
}

