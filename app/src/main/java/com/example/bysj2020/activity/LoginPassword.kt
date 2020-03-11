package com.example.bysj2020.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.bysj2020.R
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.utils.SpUtil
import com.example.bysj2020.utils.StringUtil
import com.jakewharton.rxbinding2.widget.RxTextView
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_login_password.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    override fun getLayoutId(): Int {
        return R.layout.activity_login_password
    }

    override fun initViews() {
        isBackArrow = intent.getBooleanExtra("isBackArrow", true)
        right_text.visibility = View.VISIBLE
        right_text.text = "注册"
        if (isBackArrow) {
            setBack()
        }
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
    }

    /**
     * 是否可登录监听
     */
    private fun switchLogin() {
        val accountOb: Observable<out CharSequence> =
            RxTextView.textChanges(login_password_ed_phone)
        val passwordOb: Observable<out CharSequence> =
            RxTextView.textChanges(login_password_ed_password)
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
            } else {
                login_password_code_img_close.visibility = View.GONE
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
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    private fun login() {
        tipDialog?.show()
        GlobalScope.launch {
            delay(3000)
            tipDialog?.dismiss()
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
        if (StringUtil.isBlank(password)) {
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
