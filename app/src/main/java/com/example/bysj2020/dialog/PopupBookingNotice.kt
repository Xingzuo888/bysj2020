package com.example.bysj2020.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.appcompat.widget.AppCompatTextView
import com.example.bysj2020.R

/**
 *    Author : wxz
 *    Time   : 2020/04/05
 *    Desc   : 预订须知弹框
 */
class PopupBookingNotice {
    private var context: Context? = null
    private var parent:View?=null
    private var content:String = ""
    private lateinit var popupWindow:PopupWindow

    constructor(context: Context, parent: View,content: String){
        this.context=context
        this.parent=parent
        this.content=content
        init()
    }

    companion object {
        fun Builder(context: Context, parent: View, content: String): PopupBookingNotice {
            return PopupBookingNotice(context, parent, content)
        }
    }

    private fun init() {
        val child = LayoutInflater.from(context).inflate(R.layout.popup_booking_notice, null)
        val contentText = child.findViewById<AppCompatTextView>(R.id.popup_booking_content)
        contentText.text=content
        popupWindow=PopupWindow(child,WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
        popupWindow.isOutsideTouchable=true
        popupWindow.isFocusable=true
        popupWindow.animationStyle=R.style.AnimBottom
    }

    public fun show() {
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0)
    }
    public fun dismiss() {
        popupWindow.dismiss()
    }
}