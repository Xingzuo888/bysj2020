package com.example.bysj2020.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import com.example.bysj2020.R
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.utils.StringUtil
import com.example.bysj2020.utils.VerifyUtils
import com.gyf.immersionbar.ImmersionBar
import com.jakewharton.rxbinding2.widget.RxTextView
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import io.reactivex.Observable
import io.reactivex.functions.Function3
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_forget_login_password.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern

/**
 * Author : wxz
 * Time   : 2020/02/21
 * Desc   : 修改密码
 */
class ForgetLoginPassword : BaseActivity() {

    private var isTimerStart: Boolean = false
    private var timer: CountDownTimer? = null
    private var tipDialog: QMUITipDialog? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_forget_login_password
    }

    override fun initViews() {
        setTitle("找回密码")
        setBack()
        setToolbarColor(R.color.green)
        ImmersionBar.with(this).statusBarColor(R.color.green).statusBarDarkFont(true).init()
        switchModifyPassword()
        tipDialog = QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord("提交中...")
            .create()
    }

    private fun switchModifyPassword() {
        val accountOb: Observable<out CharSequence> = RxTextView.textChanges(forget_lp_ed_phone)
        val codeOb: Observable<out CharSequence> = RxTextView.textChanges(forget_lp_ed_code)
        val passwordOb: Observable<out CharSequence> = RxTextView.textChanges(forget_lp_ed_password)
        Observable.combineLatest(accountOb, codeOb, passwordOb,
            Function3<CharSequence, CharSequence, CharSequence, Boolean> { account, code, password ->
                verifyMth(account.toString(), code.toString(), password.toString())
            })
            .subscribe {
                if (it) {
                    forget_lp_btn.setOnTouchListener(this)
                    forget_lp_btn.setBackgroundResource(R.drawable.button_green)
                    forget_lp_btn.setOnClickListener(this)
                } else {
                    forget_lp_btn.setOnTouchListener(null)
                    forget_lp_btn.setBackgroundResource(R.drawable.button_gray)
                    forget_lp_btn.setOnClickListener(null)
                }
            }.addTo(compositeDisposable)
        //删除的图标
        accountOb.subscribe {
            if (it.isNotEmpty()) {
                forget_lp_phone_img_close.visibility = View.VISIBLE
            } else {
                forget_lp_phone_img_close.visibility = View.GONE
            }
        }.addTo(compositeDisposable)
        codeOb.subscribe {
            if (it.isNotEmpty()) {
                forget_lp_password_img_close.visibility = View.VISIBLE
            } else {
                forget_lp_password_img_close.visibility = View.GONE
            }
        }.addTo(compositeDisposable)
        //验证码
        accountOb.subscribe {
            if (isTimerStart) {
                return@subscribe
            }
            if (VerifyUtils.verifyPhone(it.toString())) {
                forget_lp_tv_code.setTextColor(resources.getColor(R.color.green))
                forget_lp_tv_code.setOnClickListener(this)
            } else {
                forget_lp_tv_code.setTextColor(resources.getColor(R.color.gray_b))
                forget_lp_tv_code.setOnClickListener(null)
            }
        }.addTo(compositeDisposable)
    }

    override fun setViewClick() {
        forget_lp_phone_img_close.setOnClickListener(this)
        forget_lp_password_img_close.setOnClickListener(this)
        forget_lp_tv_code.setOnClickListener(this)
        forget_lp_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.forget_lp_phone_img_close -> {
                //清除输入框的手机号
                forget_lp_ed_phone.setText("")
            }
            R.id.forget_lp_password_img_close -> {
                //清除输入框的密码
                forget_lp_ed_password.setText("")
            }
            R.id.forget_lp_tv_code -> {
                //获取验证码
                getMsgCode()
            }
            R.id.forget_lp_btn -> {
                //修改密码
                modifyPassword()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    private fun getMsgCode() {
        if (VerifyUtils.verifyPhone(forget_lp_ed_phone.text.toString())) {
            startTimer()
        }
    }

    private fun modifyPassword() {
        tipDialog?.show()
        GlobalScope.launch {
            delay(3000)
            tipDialog?.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (null != timer) {
            timer?.cancel()
        }
    }

    /**
     * 验证手机号码和密码是否正确
     * @param account
     * @param code
     * @param password
     * @return
     */
    private fun verifyMth(account: String, code: String, password: String): Boolean {
        //电话校验
        val rex = "^1\\d{10}\$"
        val pattern: Pattern = Pattern.compile(rex)
        if (!pattern.matcher(account).matches()) {
            return false
        }
        //验证码校验
        if (code.length != 6 || StringUtil.isBlank(code)) {
            return false
        }
        //密码校验
        if (StringUtil.isBlank(password)) {
            return false
        }
        return true
    }

    /**
     * 开启定时器
     */
    private fun startTimer() {
        timer = object : CountDownTimer(60 * 1000, 1000) {
            override fun onFinish() {
                isTimerStart = false
                forget_lp_tv_code.setTextColor(resources.getColor(R.color.green))
                forget_lp_tv_code.text = "重新获取验证码"
            }

            override fun onTick(millisUntilFinished: Long) {
                isTimerStart = true
                forget_lp_tv_code.setTextColor(resources.getColor(R.color.gray_b))
                forget_lp_tv_code.text = "${millisUntilFinished / 1000}s"
            }
        }.start()
    }

    override fun onPause() {
        hideKeyboard()
        super.onPause()
    }
}
