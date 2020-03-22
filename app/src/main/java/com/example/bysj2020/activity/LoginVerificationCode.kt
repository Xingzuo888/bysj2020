package com.example.bysj2020.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.bysj2020.R
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.bean.LoginBean
import com.example.bysj2020.event.LoginEvent
import com.example.bysj2020.global.Config
import com.example.bysj2020.https.HttpResult
import com.example.bysj2020.https.RxHttp
import com.example.bysj2020.utils.LoginUtils
import com.example.bysj2020.utils.SpUtil
import com.example.bysj2020.utils.StringUtil
import com.example.bysj2020.utils.VerifyUtils
import com.google.gson.JsonObject
import com.gyf.immersionbar.ImmersionBar
import com.jakewharton.rxbinding2.widget.RxTextView
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_login_verification_code.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import java.util.regex.Pattern

/**
 * Author : wxz
 * Time   : 2020/02/20
 * Desc   :验证码登录
 */
class LoginVerificationCode : BaseActivity() {

    private var isBackArrow: Boolean = true //登录来源（true 不是从引导页来的，有返回箭头  false 是从引导页来，首次登录跳转home）

    private var isTimerStart: Boolean = false
    private var timer: CountDownTimer? = null
    private var tipDialog: QMUITipDialog? = null
    private var clickTime: Long = 0 //记录第一次点击的时间

    override fun getLayoutId(): Int {
        return R.layout.activity_login_verification_code
    }

    override fun isRegisterEventBus(): Boolean {
        return false
    }

    override fun initViews() {
        isBackArrow = intent.getBooleanExtra("isBackArrow", true)
        right_text.visibility = View.VISIBLE
        right_text.text = "注册"
        if (isBackArrow) {
            setBack()
        }
        ImmersionBar.with(this).statusBarColor(R.color.white).init()
        setToolbarColor(R.color.white)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 0f
        }
        switchLogin()
        tipDialog = QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord("提交中...")
            .create()
        var phone = SpUtil.Obtain(this, "loginPhone", "").toString()
        if (!phone.isNullOrEmpty()) login_vc_ed_phone.setText(phone)
    }

    /**
     * 是否可登录监听
     */
    private fun switchLogin() {
        val accountOb: Observable<out CharSequence> = RxTextView.textChanges(login_vc_ed_phone)
        val passwordOb: Observable<out CharSequence> = RxTextView.textChanges(login_vc_ed_code)
        Observable.combineLatest(accountOb, passwordOb,
            BiFunction<CharSequence, CharSequence, Boolean> { account, password ->
                verifyMth(
                    account.toString(),
                    password.toString()
                )
            })
            .subscribe {
                if (it) {
                    login_vc_login_btn.setOnTouchListener(this)
                    login_vc_login_btn.setBackgroundResource(R.drawable.button_green)
                    login_vc_login_btn.setOnClickListener(this)
                } else {
                    login_vc_login_btn.setOnTouchListener(null)
                    login_vc_login_btn.setBackgroundResource(R.drawable.button_gray)
                    login_vc_login_btn.setOnClickListener(null)
                }
            }.addTo(compositeDisposable)
        //删除的图标
        accountOb.subscribe {
            if (it.isNotEmpty()) {
                login_vc_img_close.visibility = View.VISIBLE
            } else {
                login_vc_img_close.visibility = View.GONE
            }
        }.addTo(compositeDisposable)
        //验证码
        accountOb.subscribe {
            if (isTimerStart) {
                return@subscribe
            }
            if (VerifyUtils.verifyPhone(it.toString())) {
                login_vc_tv_code.setTextColor(resources.getColor(R.color.green))
                login_vc_tv_code.setOnClickListener(this)
            } else {
                login_vc_tv_code.setTextColor(resources.getColor(R.color.gray_b))
                login_vc_tv_code.setOnClickListener(null)
            }
        }.addTo(compositeDisposable)
    }

    override fun setViewClick() {
        right_text.setOnClickListener(this)
        login_vc_img_close.setOnClickListener(this)
        login_vc_tv_code.setOnClickListener(this)
        login_vc_loginPassword_tv.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.right_text -> {
                //注册
                startActivity(Intent(this, Register::class.java))
            }
            R.id.login_vc_img_close -> {
                //清除输入框的手机号
                login_vc_ed_phone.setText("")
            }
            R.id.login_vc_tv_code -> {
                //获取验证码
                hideKeyboard()
                if (!isTimerStart) {
                    if (VerifyUtils.verifyPhone(login_vc_ed_phone.text.toString())) {
                        getMsgCode()
                    } else {
                        showToast(Config.PHONE_FORMAT_NOT_CORRECT)
                    }
                }
            }
            R.id.login_vc_login_btn -> {
                //登录
                hideKeyboard()
                if (VerifyUtils.verifyPhone(login_vc_ed_phone.text.toString())) {
                    login()
                } else {
                    showToast(Config.PHONE_FORMAT_NOT_CORRECT)
                }
            }
            R.id.login_vc_loginPassword_tv -> {
                //密码登录
                startActivity(
                    Intent(this, LoginPassword::class.java).putExtra(
                        "isBackArrow",
                        isBackArrow
                    )
                )
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    private fun getMsgCode() {
        tipDialog!!.show()
        val rxHttp = RxHttp(this)
        addLifecycle(rxHttp)
        var body = JsonObject()
        body.addProperty("phone", login_vc_ed_phone.text.toString())
        rxHttp.postWithJson("sendLoginSms", body, object : HttpResult<String> {
            override fun OnSuccess(t: String?, msg: String?) {
                tipDialog?.dismiss()
                showToast(Config.VERIFY_CODE)
                startTimer()
            }

            override fun OnFail(code: Int, msg: String?) {
                tipDialog?.dismiss()
                showToast(msg!!)
            }
        })
    }

    private fun login() {
        tipDialog?.show()
        val rxHttp = RxHttp(this)
        addLifecycle(rxHttp)
        var body = JsonObject()
        body.addProperty("phone", login_vc_ed_phone.text.toString())
        body.addProperty("phoneCode", login_vc_ed_code.text.toString())
        rxHttp.postWithJson("loginPhoneCode", body, object : HttpResult<LoginBean> {
            override fun OnSuccess(t: LoginBean?, msg: String?) {
                tipDialog!!.dismiss()
                //保存登录数据
                LoginUtils.saveLoginBean(this@LoginVerificationCode, t)
                SpUtil.Save(
                    this@LoginVerificationCode,
                    "mobilePhone",
                    login_vc_ed_phone.text.toString()
                )
                SpUtil.Save(
                    this@LoginVerificationCode,
                    "loginPhone",
                    login_vc_ed_phone.text.toString()
                )
                showToast(msg!!)
                //通知登录成功
                EventBus.getDefault().post(LoginEvent(0))
                if (!isBackArrow) {
                    startActivity(Intent(this@LoginVerificationCode, Home::class.java))
                }
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
     * @param password
     * @return
     */
    private fun verifyMth(account: String, password: String): Boolean {
        //电话校验
        val rex = "^1\\d{10}\$"
        val pattern: Pattern = Pattern.compile(rex)
        if (!pattern.matcher(account).matches()) {
            return false
        }
        //验证码校验
        if (password.length != 6 || StringUtil.isBlank(password)) {
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
                login_vc_tv_code.setTextColor(resources.getColor(R.color.green))
                login_vc_tv_code.text = "重新获取验证码"
            }

            override fun onTick(millisUntilFinished: Long) {
                isTimerStart = true
                login_vc_tv_code.setTextColor(resources.getColor(R.color.gray_b))
                login_vc_tv_code.text = "${millisUntilFinished / 1000}s"
            }
        }.start()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isBackArrow) {
                finish()
            } else {
                exit()
            }
            return true
        }
        return false
    }

    /**
     * 退出提示
     */
    private fun exit() {
        if (System.currentTimeMillis() - clickTime > 2000) {
            Toast.makeText(applicationContext, "再按一次退出程序", Toast.LENGTH_SHORT).show()
            clickTime = System.currentTimeMillis()
        } else {
            this.finish()
        }
    }

    override fun onPause() {
        hideKeyboard()
        super.onPause()
    }
}
