package com.example.bysj2020.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.bysj2020.R
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.bean.ImgCodeBean
import com.example.bysj2020.bean.LoginBean
import com.example.bysj2020.event.LoginEvent
import com.example.bysj2020.https.HttpResult
import com.example.bysj2020.https.RxHttp
import com.example.bysj2020.utils.*
import com.google.gson.JsonObject
import com.gyf.immersionbar.ImmersionBar
import com.jakewharton.rxbinding2.widget.RxTextView
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_login_password.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import java.util.regex.Pattern

/**
 * Author : wxz
 * Time   : 2020/02/20
 * Desc   : 密码登录
 */
class LoginPassword : BaseActivity() {

    private var isBackArrow: Boolean = true //登录来源（true 不是从引导页来的，有返回箭头）
    private var tipDialog: QMUITipDialog? = null
    private var clickTime: Long = 0 //记录第一次点击的时间

    private var imgCodeBean: ImgCodeBean? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_login_password
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
        if (!phone.isNullOrEmpty()) login_password_ed_phone.setText(phone)
        if (login_password_imgCode_layout.visibility == View.VISIBLE) {
            getImaCode()
        }
    }

    /**
     * 是否可登录监听
     */
    private fun switchLogin() {
        val accountOb: Observable<out CharSequence> =
            RxTextView.textChanges(login_password_ed_phone)
        val passwordOb: Observable<out CharSequence> =
            RxTextView.textChanges(login_password_ed_password)
        val imgOb: Observable<out CharSequence> = RxTextView.textChanges(login_password_ed_imgCode)
        if (login_password_imgCode_layout.visibility == View.VISIBLE) {
            Observable.combineLatest(
                accountOb,
                passwordOb,
                imgOb,
                Function3<CharSequence, CharSequence, CharSequence, Boolean> { account, password, img ->
                    verifyMth(account.toString(), password.toString(), img.toString())
                })
                .subscribe {
                    if (it) {
                        login_password_login_btn.setOnTouchListener(this)
                        login_password_login_btn.setBackgroundResource(R.drawable.button_green)
                        login_password_login_btn.setOnClickListener(this)
                    } else {
                        login_password_login_btn.setOnTouchListener(null)
                        login_password_login_btn.setBackgroundResource(R.drawable.button_gray)
                        login_password_login_btn.setOnClickListener(null)
                    }
                }.addTo(compositeDisposable)
        } else {
            Observable.combineLatest(
                accountOb,
                passwordOb,
                BiFunction<CharSequence, CharSequence, Boolean> { account, password ->
                    verifyMth(account.toString(), password.toString())
                })
                .subscribe {
                    if (it) {
                        login_password_login_btn.setOnTouchListener(this)
                        login_password_login_btn.setBackgroundResource(R.drawable.button_green)
                        login_password_login_btn.setOnClickListener(this)
                    } else {
                        login_password_login_btn.setOnTouchListener(null)
                        login_password_login_btn.setBackgroundResource(R.drawable.button_gray)
                        login_password_login_btn.setOnClickListener(null)
                    }
                }.addTo(compositeDisposable)
        }

        //删除的图标
        accountOb.subscribe {
            if (it.isNotEmpty()) {
                login_password_phone_img_close.visibility = View.VISIBLE
            } else {
                login_password_phone_img_close.visibility = View.GONE
            }
        }.addTo(compositeDisposable)
        passwordOb.subscribe {
            if (it.isNotEmpty()) {
                login_password_code_img_close.visibility = View.VISIBLE
                login_password_code_tb.visibility = View.VISIBLE
            } else {
                login_password_code_img_close.visibility = View.GONE
                login_password_code_tb.visibility = View.GONE
            }
        }.addTo(compositeDisposable)
    }

    override fun setViewClick() {
        right_text.setOnClickListener(this)
        login_password_phone_img_close.setOnClickListener(this)
        login_password_code_img_close.setOnClickListener(this)
        login_password_login_btn.setOnClickListener(this)
        login_password_login_vc_tv.setOnClickListener(this)
        login_password_forget_password.setOnClickListener(this)
        login_password_code_img_imgCode.setOnClickListener(this)
        login_password_code_tb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                //显示密码
                login_password_ed_password.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                //隐藏密码
                login_password_ed_password.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
            login_password_ed_password.setSelection(login_password_ed_password.text.toString().length)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.right_text -> {
                //注册
                startActivity(Intent(this, Register::class.java))
            }
            R.id.login_password_phone_img_close -> {
                //清除输入框的手机号
                login_password_ed_phone.setText("")
            }
            R.id.login_password_code_img_close -> {
                //清除输入框的密码
                login_password_ed_password.setText("")
            }
            R.id.login_password_login_btn -> {
                //登录
                login()
            }
            R.id.login_password_login_vc_tv -> {
                //验证码登录
                startActivity(
                    Intent(this, LoginVerificationCode::class.java).putExtra(
                        "isBackArrow",
                        isBackArrow
                    )
                )
                finish()
            }
            R.id.login_password_forget_password -> {
                //忘记密码
                startActivity(Intent(this, ModifyPassword::class.java).putExtra("titleId", 0))
            }
            R.id.login_password_code_img_imgCode -> {
                //重新获取图片验证码
                getImaCode()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    /**
     * 获取图片验证码
     */
    private fun getImaCode() {
        tipDialog?.show()
        val rxHttp = RxHttp(this)
        addLifecycle(rxHttp)
        rxHttp.postWithJson("verifyCode", JsonObject(), object : HttpResult<ImgCodeBean> {
            override fun OnSuccess(t: ImgCodeBean?, msg: String?) {
                tipDialog?.dismiss()
                imgCodeBean = t
                LoadImageUtil(this@LoginPassword).loadImage(login_password_code_img_imgCode, t?.img)
            }

            override fun OnFail(code: Int, msg: String?) {
                tipDialog?.dismiss()
                showToast(msg!!)
            }
        })
    }

    /**
     * 登录
     */
    private fun login() {
        tipDialog?.show()
        val rxHttp = RxHttp(this)
        addLifecycle(rxHttp)
        var body = JsonObject()
        body.addProperty("account", login_password_ed_phone.text.toString())
        val password = MD5.getMD5String(login_password_ed_password.text.toString())
        body.addProperty("password", password)
        if (login_password_imgCode_layout.visibility == View.VISIBLE) {
            body.addProperty("uuid", imgCodeBean!!.uuid)
            body.addProperty("uuidCode", login_password_ed_imgCode.text.toString())
        }
        rxHttp.postWithJson("loginAccount", body, object : HttpResult<LoginBean> {
            override fun OnSuccess(t: LoginBean?, msg: String?) {
                tipDialog!!.dismiss()
                //保存登录数据
                LoginUtils.saveLoginBean(this@LoginPassword, t)
                SpUtil.Save(
                    this@LoginPassword,
                    "mobilePhone",
                    login_password_ed_phone.text.toString()
                )
                SpUtil.Save(
                    this@LoginPassword,
                    "loginPhone",
                    login_password_ed_phone.text.toString()
                )
                showToast(msg!!)
                EventBus.getDefault().post(LoginEvent(0))
                if (!isBackArrow) {
                    startActivity(Intent(this@LoginPassword, Home::class.java))
                }
                finish()
            }

            override fun OnFail(code: Int, msg: String?) {
                tipDialog!!.dismiss()
                showToast(msg!!)
            }
        })
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
        //密码校验
        if (StringUtil.isBlank(password)) {
            return false
        }
        return true
    }

    /**
     * 验证手机号码和密码是否正确
     * @param account
     * @param password
     * @param img
     * @return
     */
    private fun verifyMth(account: String, password: String, img: String): Boolean {
        //电话校验
        val rex = "^1\\d{10}\$"
        val pattern: Pattern = Pattern.compile(rex)
        if (!pattern.matcher(account).matches()) {
            return false
        }
        //密码校验
        if (StringUtil.isBlank(password)) {
            return false
        }
        //验证码校验
        if (StringUtil.isBlank(img)) {
            return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
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
