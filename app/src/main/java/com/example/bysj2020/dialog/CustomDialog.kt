package com.example.bysj2020.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.bysj2020.R

/**
 *    Author : wxz
 *    Time   : 2020/03/03
 *    Desc   : 基本弹框
 */
class CustomDialog {

    private var context: Context? = null

    private lateinit var positive_btn: TextView
    private lateinit var negative_btn: TextView
    private lateinit var title_tv: TextView
    private lateinit var content_tv: TextView
    private lateinit var btn_group: LinearLayout
    private lateinit var contentAndBtn_line: View
    private var cb: CheckBox? = null
    private var positiveListener: View.OnClickListener? = null
    private var negativeListener: View.OnClickListener? = null
    private var checkedChangeListener: CompoundButton.OnCheckedChangeListener? = null
    private var dialog: Dialog? = null
    private var view: View? = null
    private var positiveTextColor: String? = null

    private var positiveText: String? = null
    private var negativeText: String? = null
    private var title = ""
    private var content = ""
    private var positiveTextId = 0
    private var negativeTextId = 0
    private var titleId: Int = 0
    private var contentId: Int = 0
    private var isShowTitle = true
    private var isShowCb = 0
    private var isTwoBtn = true
    private var line: View? = null

    /**
     * 创建dialog并初始化默认的view
     *
     * @param context
     */
    constructor(context: Context) {
        this.context = context
        dialog = Dialog(context, R.style.MyDialog)
        dialog!!.setCanceledOnTouchOutside(false)
        view = getDefaultView()
        initDefaultView()
    }

    /**
     *
     * @param context
     * @param isShowTitle
     */
    constructor(context: Context, isShowTitle: Boolean) {
        this.context = context
        this.isShowTitle = isShowTitle
        dialog = Dialog(context, R.style.MyDialog)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setCancelable(false)
        view = getDefaultView()
        initDefaultView()
    }

    /**
     *
     * @param context
     * @param isShowTitle
     */
    constructor(context: Context, isShowTitle: Boolean, isTwoBtn: Boolean) {
        this.context = context
        this.isShowTitle = isShowTitle
        this.isTwoBtn = isTwoBtn
        dialog = Dialog(context, R.style.MyDialog)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setCancelable(false)
        view = getDefaultView()
        initDefaultView()
    }

    constructor(context: Context, isShowTitle: Boolean, isShowCb: Int) {
        this.context = context
        this.isShowTitle = isShowTitle
        this.isShowCb = isShowCb
        dialog = Dialog(context, R.style.MyDialog)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setCancelable(false)
        view = getDefaultView()
        initDefaultView()
    }


    /**
     * 初始化默认样式
     *
     * @param
     */
    private fun initDefaultView() {
        title_tv = view!!.findViewById(R.id.title_tv)
        content_tv = view!!.findViewById(R.id.content_tv)
        positive_btn = view!!.findViewById(R.id.positive_btn)
        negative_btn = view!!.findViewById(R.id.negative_btn)
        cb = view!!.findViewById(R.id.alert_cb)
        line = view!!.findViewById(R.id.line)
        btn_group = view!!.findViewById(R.id.btn_group)
        contentAndBtn_line = view!!.findViewById(R.id.contentAndBtn_line)
        if (isShowCb == 1) {
            cb!!.visibility = View.VISIBLE
        }
        if (!isShowTitle) {
            title_tv.visibility = View.GONE
            negative_btn.visibility = View.GONE
            content_tv.setTextColor(context!!.resources.getColor(R.color.black))
        }
        if (!isTwoBtn) {
            line!!.visibility = View.GONE
            negative_btn!!.visibility = View.GONE
        }
    }

    private fun getDefaultView(): View {
        return LayoutInflater.from(context).inflate(R.layout.dialog_item, null)

    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun setTitle(titleId: Int) {
        this.titleId = titleId
    }

    fun setContentMsg(content: String) {
        this.content = content
    }

    fun setContentMsg(contentId: Int) {
        this.contentId = contentId
    }

    fun setContentGravity(gravity: Int) {
        content_tv!!.setGravity(gravity)
    }

    fun setPositiveButton(textId: Int, positiveListener: View.OnClickListener) {
        positiveTextId = textId
        this.positiveListener = positiveListener
    }

    fun setPositiveButton(text: String, color: String, positiveListener: View.OnClickListener) {
        positiveText = text
        this.positiveListener = positiveListener
        this.positiveTextColor = color
    }

    fun setNegativeButton(textId: Int, negativeListener: View.OnClickListener) {
        negativeTextId = textId
        this.negativeListener = negativeListener
    }

    fun setPositiveButton(text: String, positiveListener: View.OnClickListener) {
        positiveText = text
        this.positiveListener = positiveListener
    }

    fun setNegativeButton(text: String, negativeListener: View.OnClickListener) {
        negativeText = text
        this.negativeListener = negativeListener
    }

    fun setCheckedChangeListener(
        checkedChangeListener: CompoundButton.OnCheckedChangeListener
    ) {
        this.checkedChangeListener = checkedChangeListener
    }

    fun show() {
        if (isActivityFinished(context)) {
            return
        }

        if (null != dialog) {
            checkPositiveBtn()
            checkNegativeBtn()
            if (isShowCb == 1) {
                cb!!.setOnCheckedChangeListener(checkedChangeListener)
            }
            checkTitle()
            checkContent()
            dialog?.setContentView(view!!)
            dialog?.show()
        }
    }

    fun dismiss() {
        if (isActivityFinished(context)) {
            return
        }
        if (dialog != null) {
            dialog!!.dismiss()
            dialog = null
        }

    }

    fun checkTitle() {
        if (title_tv == null) {
            return
        }
        if (!TextUtils.isEmpty(title)) {
            title_tv!!.text = title
        } else if (titleId > 0) {
            title_tv!!.setText(titleId)
        }
    }

    private fun checkContent() {
        if (content_tv == null) {
            return
        }
        if (!TextUtils.isEmpty(content)) {
            content_tv!!.setText(content)
        } else if (contentId > 0) {
            content_tv!!.setText(contentId)
        }
    }

    private fun checkPositiveBtn() {

        if (positive_btn == null) {
            return
        }
        if (positiveListener != null) {

            if (!TextUtils.isEmpty(positiveText)) {

                positive_btn!!.text = positiveText

            } else if (positiveTextId > 0) {

                positive_btn!!.setText(positiveTextId)
            }
            if (!positiveTextColor.isNullOrEmpty()) {
                positive_btn!!.setTextColor(Color.parseColor(positiveTextColor))
            }
            positive_btn!!.setOnClickListener(positiveListener)

        } else {
            positive_btn!!.visibility = View.GONE
        }
    }

    private fun checkNegativeBtn() {
        if (negative_btn == null) {
            return
        }
        if (negativeListener != null) {
            if (!TextUtils.isEmpty(negativeText)) {
                negative_btn!!.text = negativeText
            } else if (negativeTextId > 0) {
                negative_btn!!.setText(negativeTextId)
            }
            negative_btn!!.setOnClickListener(negativeListener)
        } else {
            negative_btn!!.visibility = View.GONE
        }
    }

    fun getDialog(): Dialog? {
        return dialog
    }

    /**
     * 点击外部是否可以取消
     *
     * @param isCancleable
     */
    fun setCancleable(isCancleable: Boolean) {
        dialog!!.setCanceledOnTouchOutside(isCancleable)
    }

    private fun isActivityFinished(context: Context?): Boolean {
        return (context as Activity).isFinishing
    }

    //虚拟返回键返回是否消失
    fun virKeyIsDismiss(isDis: Boolean) {
        var dismissTime = 0L
        dialog?.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                //按下返回键的同时会多次调用onkeyListener方法,所以这里用
                //时间做判断
                if ((System.currentTimeMillis() - dismissTime) > 500) {
                    if (isDis) dialog.dismiss()

                }

                dismissTime = System.currentTimeMillis()
                return@setOnKeyListener true
            } else {
                return@setOnKeyListener false
            }
        }
    }


}