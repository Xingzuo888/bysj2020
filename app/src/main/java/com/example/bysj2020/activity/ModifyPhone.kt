package com.example.bysj2020.activity

import android.graphics.Color
import android.os.CountDownTimer
import android.view.View
import com.example.bysj2020.R
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.view.InputCodeView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_modify_phone.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern

/**
 * Author : wxz
 * Time   : 2020/03/07
 * Desc   : 修改手机号
 */
class ModifyPhone : BaseActivity() {

    private var loading: QMUITipDialog? = null
    private var page: Int = 0  //0表示输入手机号界面  1表示输入验证码界面
    private val pages = listOf("修改手机号（1/2）", "修改手机号（2/2）")
    private var timer: CountDownTimer? = null
    private var isTimerStart: Boolean = false

    override fun getLayoutId(): Int {
        return R.layout.activity_modify_phone
    }

    override fun getContentView(): View {
        return modify
    }

    override fun initViews() {
        setBack()
        setTitle(pages[page])
        setTitleColors(this, R.color.black_2)
        loading = QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord("加载中...")
            .create()
        if (page == 0) {
            modify_phone.visibility = View.VISIBLE
            modify_code.visibility = View.GONE
        } else {
            modify_phone.visibility = View.GONE
            modify_code.visibility = View.VISIBLE
        }
        switch()
        modify_input_code.setInputCompleteListener(object : InputCodeView.InputCompleteListener {
            override fun inputComplete() {
                modify_code_btn.visibility = View.VISIBLE
            }

            override fun invalidContent() {
                modify_code_btn.visibility = View.GONE
            }

        })
    }

    override fun setViewClick() {
        modify_getCode.setOnClickListener(this)
        modify_code_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.modify_getCode -> {
                //获取验证码
                if (!isTimerStart) {
                    getMsgCode()
                }
            }
            R.id.modify_next_step -> {
                //下一步
                if (verifyMth(modify_phone_input.text.toString())) {
                    //隐藏软盘
                    hideKeyboard(modify_input_code)
                    getMsgCode()
                } else {
                    showToast("手机号格式不正确！")
                }
            }
            R.id.modify_code_btn -> {
                //确定
                changePhone()
            }
        }
    }


    //是否可获取验证码监听
    private fun switch() {
        val phoneOb: Observable<out CharSequence> = RxTextView.textChanges(modify_phone_input)
        phoneOb.subscribe {
            modify_phone_input.textSize = if (it.toString() != "") 20f else 14f
            if (it.length == 11) {
                modify_next_step.setOnTouchListener(this)
                modify_next_step.setBackgroundResource(R.drawable.button_green)
                modify_next_step.setOnClickListener(this)
            } else {
                modify_next_step.setOnTouchListener(null)
                modify_next_step.setBackgroundResource(R.drawable.button_gray)
                modify_next_step.setOnClickListener(null)
            }
        }.addTo(compositeDisposable)

    }

    /**
     * 验证手机号码和密码是否正确
     * @param account
     * @return
     */
    private fun verifyMth(account: String): Boolean {
        //电话校验
        val rex = "^1\\d{10}\$"
        val pattern: Pattern = Pattern.compile(rex)
        return pattern.matcher(account).matches()
    }

    /**
     * 开启定时器
     */
    private fun startTimer() {
        timer = object : CountDownTimer(60 * 1000, 1000) {
            override fun onTick(l: Long) {
                isTimerStart = true
                modify_getCode.setTextColor(Color.parseColor("#BBBBBB"))
                modify_getCode.text = "${l / 1000}s"
            }

            override fun onFinish() {
                isTimerStart = false
                modify_getCode.setTextColor(Color.parseColor("#33cb98"))
                modify_getCode.text = "重新获取"
            }
        }.start()
    }

    /**
     * 获取短信验证码
     */
    private fun getMsgCode() {
        modify_phone.visibility = View.GONE
        modify_code.visibility = View.VISIBLE
        modify_sendTip.text = "已发送验证码至${modify_phone_input.text.toString().substring(
            0,
            3
        )}****${modify_phone_input.text.toString().substring(
            modify_phone_input.text.toString().length - 4,
            modify_phone_input.text.toString().length
        )}"
        startTimer()
    }

    /**
     * 修改手机号
     */
    private fun changePhone() {
        loading?.show()
        GlobalScope.launch {
            delay(3000)
            loading?.dismiss()
        }
    }

    override fun onDestroy() {
        if (null != loading) loading?.dismiss()
        if (null != timer) timer?.cancel()
        super.onDestroy()
    }


}
