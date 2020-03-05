package com.example.bysj2020.dialog

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.TimePickerView
import com.contrarywind.view.WheelView
import com.example.bysj2020.R
import java.util.*

/**
 *    Author : wxz
 *    Time   : 2020/03/04
 *    Desc   : 时间选择器
 */
class TimePickerDialog {

    private var title: String = "TimePicker"
    private var timePickerBuilder: TimePickerBuilder? = null
    private var pickerView: TimePickerView? = null
    private var rootView: View? = null
    private var timePickerCallBack: TimePickerCallBack? = null
    private var context: Context? = null
    private var selectedDate: Calendar? = null
    private var startDate: Calendar? = null
    private var endDate: Calendar? = null

    constructor(
        title: String,
        startDate: Calendar,
        endDate: Calendar,
        selecteDate: Calendar,
        context: Context
    ) {
        this.context = context
        this.title = title
        this.startDate = startDate
        this.endDate = endDate
        this.selectedDate = selectedDate
        initTimePickerBuilder()
    }

    constructor(title: String, context: Context) {
        this.title = title
        this.context = context
        this.selectedDate = Calendar.getInstance()//系统当前时间
        this.startDate = Calendar.getInstance()//系统当前时间
        this.startDate?.set(1500, 0, 1)
        this.endDate = Calendar.getInstance()
        this.endDate?.set(2060, 11, 31)
        initTimePickerBuilder()
    }

    /**
     * 初始化TimePickerBuilder
     */
    private fun initTimePickerBuilder() {
        timePickerBuilder = TimePickerBuilder(context,
            OnTimeSelectListener { date, v -> timePickerCallBack?.onTimeSelect(title, date!!) })
            .setRangDate(startDate, endDate)
            .setDate(selectedDate)
            .setLayoutRes(
                R.layout.dialog_time_picker
            ) { v ->
                this@TimePickerDialog.rootView = v
                v?.findViewById<Button>(R.id.btn_cancel)
                    ?.setOnClickListener { pickerView?.dismiss() }
                v?.findViewById<Button>(R.id.btn_ok)?.setOnClickListener {
                    pickerView?.returnData()
                    pickerView?.dismiss()
                }
                v?.findViewById<TextView>(R.id.wheel_title)?.text=title
                v?.findViewById<WheelView>(R.id.year)?.setTextSize(20f)
                v?.findViewById<WheelView>(R.id.month)?.setTextSize(20f)
                v?.findViewById<WheelView>(R.id.day)?.setTextSize(20f)
                v?.findViewById<WheelView>(R.id.hour)?.setTextSize(20f)
                v?.findViewById<WheelView>(R.id.min)?.setTextSize(20f)
                v?.findViewById<WheelView>(R.id.second)?.setTextSize(20f)
            }
    }

    /**
     * 显示弹框
     */
    public fun show() {
        pickerView = timePickerBuilder?.build()
        pickerView?.show()
    }

    /**
     * 设置只选择年份
     * 分别控制“年”“月”“日”“时”“分”“秒”的显示或隐藏
     */
    public fun setChooseYear() {
        timePickerBuilder?.setType(booleanArrayOf(true, false, false, false, false, false))
    }

    /**
     * 设置只选择年月
     * 分别控制“年”“月”“日”“时”“分”“秒”的显示或隐藏
     */
    public fun setChooseYearAndMonth() {
        timePickerBuilder?.setType(booleanArrayOf(true, true, false, false, false, false))
    }

    /**
     * 设置只选择年月日
     * 分别控制“年”“月”“日”“时”“分”“秒”的显示或隐藏
     */
    public fun setChooseDate() {
        timePickerBuilder?.setType(booleanArrayOf(true, true, true, false, false, false))
    }

    /**
     * 设置只选择时
     * 分别控制“年”“月”“日”“时”“分”“秒”的显示或隐藏
     */
    public fun setChooseHours() {
        timePickerBuilder?.setType(booleanArrayOf(false, false, false, true, false, false))
    }

    /**
     * 设置只选择时分
     * 分别控制“年”“月”“日”“时”“分”“秒”的显示或隐藏
     */
    public fun setChooseHoursAndMin() {
        timePickerBuilder?.setType(booleanArrayOf(false, false, false, true, true, false))
    }

    /**
     * 设置只选择时分秒
     * 分别控制“年”“月”“日”“时”“分”“秒”的显示或隐藏
     */
    public fun setChooseTime() {
        timePickerBuilder?.setType(booleanArrayOf(false, false, false, true, true, true))
    }

    /**
     * 设置选择全部
     * 分别控制“年”“月”“日”“时”“分”“秒”的显示或隐藏
     */
    public fun setChooseAll() {
        timePickerBuilder?.setType(booleanArrayOf(true, true, true, true, true, true))
    }

    /**
     * 设置回调事件
     */
    public fun setTimerPickerCallBack(callBack: TimePickerCallBack) {
        this.timePickerCallBack = callBack
    }

    interface TimePickerCallBack {
        fun onTimeSelect(key: String, date: Date)
    }
}