package com.example.bysj2020.dialog

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.contrarywind.view.WheelView
import com.example.bysj2020.R

/**
 *    Author : wxz
 *    Time   : 2020/03/04
 *    Desc   : 条件选择器
 */
class WheelDialog {

    private var title: String? = null
    private var optionsPickerBuilder: OptionsPickerBuilder? = null
    private var pickerView: OptionsPickerView<String>? = null
    private var wheelCallBack: WheelCallBack? = null
    private var rootView: View? = null
    private var context: Context? = null
    private var options1Items: List<String>? = null

    constructor(title: String, context: Context) {
        this.title = title
        this.context = context
        initOptionsPickerBuilder()
    }

    constructor(title: String, options1Items: List<String>, context: Context) {
        this.title = title
        this.options1Items = options1Items
        this.context = context
        initOptionsPickerBuilder()
    }

    /**
     * 初始化OptionsPickerBuilder
     */
    private fun initOptionsPickerBuilder() {
        optionsPickerBuilder = OptionsPickerBuilder(context,
            OnOptionsSelectListener { options1, options2, options3, v ->
                wheelCallBack?.onSelect(
                    title!!,
                    options1,
                    options2,
                    options3
                )
                optionsPickerBuilder?.setSelectOptions(options1)
            })
            .setLayoutRes(R.layout.dialog_wheel) { v ->
                this@WheelDialog.rootView = v
                v?.findViewById<Button>(R.id.btn_cancel)
                    ?.setOnClickListener { pickerView?.dismiss() }
                v?.findViewById<Button>(R.id.btn_ok)?.setOnClickListener {
                    pickerView?.returnData()
                    pickerView?.dismiss()
                }
                v?.findViewById<TextView>(R.id.wheel_title)?.text=title
                v?.findViewById<WheelView>(R.id.options1)?.setTextSize(20f)
                v?.findViewById<WheelView>(R.id.options2)?.setTextSize(20f)
                v?.findViewById<WheelView>(R.id.options3)?.setTextSize(20f)
            }
        pickerView = optionsPickerBuilder?.build<String>()
    }

    /**
     * 显示弹框
     */
    public fun show() {
        pickerView?.setPicker(options1Items)
        pickerView?.show()
    }

    /**
     * 添加数据
     */
    public fun setOptionsItems(options1Items: List<String>) {
        this.options1Items = options1Items
    }

    /**
     * 设置回调事件
     */
    public fun setWheelCallBack(callBack: WheelCallBack) {
        this.wheelCallBack = callBack
    }

    interface WheelCallBack {
        fun onSelect(key: String, options1: Int, options2: Int, options3: Int)
    }
}