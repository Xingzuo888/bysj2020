package com.example.bysj2020.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.WindowManager
import com.example.bysj2020.R
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.https.HttpResult
import com.example.bysj2020.https.RxHttp
import com.example.bysj2020.utils.MD5
import com.example.bysj2020.utils.StringUtil
import com.example.bysj2020.utils.VerifyUtils
import com.google.gson.JsonObject
import com.gyf.immersionbar.ImmersionBar
import com.jakewharton.rxbinding2.widget.RxTextView
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import io.reactivex.Observable
import io.reactivex.functions.Function3
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.regex.Pattern

/**
 * Author : wxz
 * Time   : 2020/02/20
 * Desc   : 注册
 */
class Register : BaseActivity() {

    private var isTimerStart: Boolean = false
    private var timer: CountDownTimer? = null
    private var tipDialog: QMUITipDialog? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_register
    }

    override fun isRegisterEventBus(): Boolean {
        return false
    }

    override fun initViews() {
        right_text.visibility = View.VISIBLE
        right_text.text = "登录"
        setTitle("注册")
        setBack()
        setToolbarColor(R.color.green)
        ImmersionBar.with(this).statusBarColor(R.color.green).statusBarDarkFont(true).init()
        switchLogin()
        tipDialog = QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord("提交中...")
            .create()
    }

    private fun switchLogin() {
        val accountOb: Observable<out CharSequence> = RxTextView.textChanges(register_ed_phone)
        val codeOb: Observable<out CharSequence> = RxTextView.textChanges(register_ed_code)
        val passwordOb: Observable<out CharSequence> = RxTextView.textChanges(register_ed_password)
        Observable.combineLatest(accountOb, codeOb, passwordOb,
            Function3<CharSequence, CharSequence, CharSequence, Boolean> { account, code, password ->
                verifyMth(account.toString(), code.toString(), password.toString())
            })
            .subscribe {
                if (it) {
                    register_btn.setOnTouchListener(this)
                    register_btn.setBackgroundResource(R.drawable.button_green)
                    register_btn.setOnClickListener(this)
                } else {
                    register_btn.setOnTouchListener(null)
                    register_btn.setBackgroundResource(R.drawable.button_gray)
                    register_btn.setOnClickListener(null)
                }
            }.addTo(compositeDisposable)
        //删除的图标
        accountOb.subscribe {
            if (it.isNotEmpty()) {
                register_phone_img_close.visibility = View.VISIBLE
            } else {
                register_phone_img_close.visibility = View.GONE
            }
        }.addTo(compositeDisposable)
        passwordOb.subscribe {
            if (it.isNotEmpty()) {
                register_password_img_close.visibility = View.VISIBLE
                register_password_tb.visibility = View.VISIBLE
            } else {
                register_password_img_close.visibility = View.GONE
                register_password_tb.visibility = View.GONE
            }
        }.addTo(compositeDisposable)
        //验证码
        accountOb.subscribe {
            if (isTimerStart) {
                return@subscribe
            }
            if (VerifyUtils.verifyPhone(it.toString())) {
                register_tv_code.setTextColor(resources.getColor(R.color.green))
                register_tv_code.setOnClickListener(this)
            } else {
                register_tv_code.setTextColor(resources.getColor(R.color.gray_b))
                register_tv_code.setOnClickListener(null)
            }
        }.addTo(compositeDisposable)
    }

    override fun setViewClick() {
        right_text.setOnClickListener(this)
        register_phone_img_close.setOnClickListener(this)
        register_password_img_close.setOnClickListener(this)
        register_tv_code.setOnClickListener(this)
        register_btn.setOnClickListener(this)
        register_password_tb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                //显示密码
                register_ed_password.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                //隐藏密码
                register_ed_password.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
            register_ed_password.setSelection(register_ed_password.text.toString().length)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.right_text -> {
                //登录
                finish()
            }
            R.id.register_phone_img_close -> {
                //清除输入框的手机号
                register_ed_phone.setText("")
            }
            R.id.register_password_img_close -> {
                //清除输入框的密码
                register_ed_password.setText("")
            }
            R.id.register_tv_code -> {
                //获取验证码
                getMsgCode()
            }
            R.id.register_btn -> {
                //注册
                register()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    /**
     * 获取验证码
     */
    private fun getMsgCode() {
        tipDialog!!.show()
        val rxHttp = RxHttp(this)
        addLifecycle(rxHttp)
        var body = JsonObject()
        body.addProperty("phone", register_ed_phone.text.toString())
        rxHttp.postWithJson("sendRegsSms", body, object : HttpResult<String> {
            override fun OnSuccess(t: String?, msg: String?) {
                tipDialog!!.dismiss()
                showToast("验证码已发送")
                startTimer()
            }

            override fun OnFail(code: Int, msg: String?) {
                tipDialog!!.dismiss()
                showToast(msg!!)
            }
        })
    }

    private fun register() {
        tipDialog?.show()
        val rxHttp = RxHttp(this)
        addLifecycle(rxHttp)
        var body = JsonObject()
        body.addProperty("phone", register_ed_phone.text.toString())
        body.addProperty("code", register_ed_code.text.toString())
        val password = MD5.getMD5String(register_ed_password.text.toString())
        body.addProperty("password", password)
        rxHttp.postWithJson("regsMobileCode", body, object : HttpResult<String> {
            override fun OnSuccess(t: String?, msg: String?) {
                tipDialog!!.dismiss()
                showToast("注册成功")
                finish()
            }

            override fun OnFail(code: Int, msg: String?) {
                tipDialog!!.dismiss()
                showToast(msg!!)
            }
        })
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
                register_tv_code.setTextColor(resources.getColor(R.color.green))
                register_tv_code.text = "重新获取验证码"
            }

            override fun onTick(millisUntilFinished: Long) {
                isTimerStart = true
                register_tv_code.setTextColor(resources.getColor(R.color.gray_b))
                register_tv_code.text = "${millisUntilFinished / 1000}s"
            }
        }.start()
    }

    override fun onPause() {
        hideKeyboard()
        super.onPause()
    }
}
