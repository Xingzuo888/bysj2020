package com.example.bysj2020.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.WindowManager
import com.example.bysj2020.R
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.global.Config
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
import kotlinx.android.synthetic.main.activity_modify_password.*
import java.util.regex.Pattern

/**
 * Author : wxz
 * Time   : 2020/02/21
 * Desc   : 修改密码
 */
class ModifyPassword : BaseActivity() {

    private var titleID = 0 //0 找回密码 1 修改密码
    private var titles = arrayListOf("找回密码", "修改密码")
    private var msgUrls = arrayListOf("sendSmsForgetPassword", "sendSmsModifyPassword")
    private var modifyPasswords = arrayListOf("forgetPassword", "modifyPassword")
    private var isTimerStart: Boolean = false
    private var timer: CountDownTimer? = null
    private var tipDialog: QMUITipDialog? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_modify_password
    }

    override fun isRegisterEventBus(): Boolean {
        return false
    }

    override fun initViews() {
        titleID = intent.getIntExtra("titleId", 0)
        setTitle(titles[titleID])
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
        val accountOb: Observable<out CharSequence> = RxTextView.textChanges(mp_ed_phone)
        val codeOb: Observable<out CharSequence> = RxTextView.textChanges(mp_ed_code)
        val passwordOb: Observable<out CharSequence> = RxTextView.textChanges(mp_ed_password)
        Observable.combineLatest(accountOb, codeOb, passwordOb,
            Function3<CharSequence, CharSequence, CharSequence, Boolean> { account, code, password ->
                verifyMth(account.toString(), code.toString(), password.toString())
            })
            .subscribe {
                if (it) {
                    mp_btn.setOnTouchListener(this)
                    mp_btn.setBackgroundResource(R.drawable.button_green)
                    mp_btn.setOnClickListener(this)
                } else {
                    mp_btn.setOnTouchListener(null)
                    mp_btn.setBackgroundResource(R.drawable.button_gray)
                    mp_btn.setOnClickListener(null)
                }
            }.addTo(compositeDisposable)
        //删除的图标
        accountOb.subscribe {
            if (it.isNotEmpty()) {
                mp_phone_img_close.visibility = View.VISIBLE
            } else {
                mp_phone_img_close.visibility = View.GONE
            }
        }.addTo(compositeDisposable)
        passwordOb.subscribe {
            if (it.isNotEmpty()) {
                mp_password_img_close.visibility = View.VISIBLE
                mp_password_tb.visibility = View.VISIBLE
            } else {
                mp_password_img_close.visibility = View.GONE
                mp_password_tb.visibility = View.GONE
            }
            if (it.length in 8..20) {
                mp_password_prompt.visibility = View.GONE
            }
        }.addTo(compositeDisposable)
        //验证码
        accountOb.subscribe {
            if (isTimerStart) {
                return@subscribe
            }
            if (VerifyUtils.verifyPhone(it.toString())) {
                mp_tv_code.setTextColor(resources.getColor(R.color.green))
                mp_tv_code.setOnClickListener(this)
            } else {
                mp_tv_code.setTextColor(resources.getColor(R.color.gray_b))
                mp_tv_code.setOnClickListener(null)
            }
        }.addTo(compositeDisposable)
    }

    override fun setViewClick() {
        mp_phone_img_close.setOnClickListener(this)
        mp_password_img_close.setOnClickListener(this)
        mp_tv_code.setOnClickListener(this)
        mp_password_tb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                //显示密码
                mp_ed_password.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                //隐藏密码
                mp_ed_password.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
            mp_ed_password.setSelection(mp_ed_password.text.toString().length)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mp_phone_img_close -> {
                //清除输入框的手机号
                mp_ed_phone.setText("")
            }
            R.id.mp_password_img_close -> {
                //清除输入框的密码
                mp_ed_password.setText("")
            }
            R.id.mp_tv_code -> {
                //获取验证码
                hideKeyboard()
                if (VerifyUtils.verifyPhone(mp_ed_phone.text.toString())) {
                    getMsgCode()
                } else {
                    showToast(Config.PHONE_FORMAT_NOT_CORRECT)
                }
            }
            R.id.mp_btn -> {
                //修改密码
                hideKeyboard()
                if (VerifyUtils.verifyPhone(mp_ed_phone.text.toString())) {
                    if (mp_ed_password.text.toString().length in 8..20) {
                        modifyPassword()
                    } else {
                        mp_password_prompt.visibility = View.VISIBLE
                        showToast(Config.PASSWORD_FORMAT_INCORRECT)
                    }
                } else {
                    showToast(Config.PHONE_FORMAT_NOT_CORRECT)
                }
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
        body.addProperty("phone", mp_ed_phone.text.toString())
        rxHttp.postWithJson(msgUrls[titleID], body, object : HttpResult<String> {
            override fun OnSuccess(t: String?, msg: String?) {
                tipDialog!!.dismiss()
                showToast(Config.VERIFY_CODE)
                startTimer()
            }

            override fun OnFail(code: Int, msg: String?) {
                tipDialog!!.dismiss()
                showToast(msg!!)
            }
        })
    }

    private fun modifyPassword() {
        tipDialog?.show()
        val rxHttp = RxHttp(this)
        addLifecycle(rxHttp)
        var body = JsonObject()
        body.addProperty("phone", mp_ed_phone.text.toString())
        body.addProperty("code", mp_ed_code.text.toString())
        val password = MD5.getMD5String(mp_ed_password.text.toString())
        body.addProperty("password", password)
        rxHttp.postWithJson(modifyPasswords[titleID], body, object : HttpResult<String> {
            override fun OnSuccess(t: String?, msg: String?) {
                tipDialog!!.dismiss()
                showToast("密码修改成功")
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
                mp_tv_code.setTextColor(resources.getColor(R.color.green))
                mp_tv_code.text = "重新获取验证码"
            }

            override fun onTick(millisUntilFinished: Long) {
                isTimerStart = true
                mp_tv_code.setTextColor(resources.getColor(R.color.gray_b))
                mp_tv_code.text = "${millisUntilFinished / 1000}s"
            }
        }.start()
    }

    override fun onPause() {
        hideKeyboard()
        super.onPause()
    }
}
