package com.example.bysj2020.activity

import android.app.Activity
import android.content.Intent
import android.view.View
import com.example.bysj2020.R
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.bean.UserInfoBean
import com.example.bysj2020.common.Format
import com.example.bysj2020.dialog.TimePickerDialog
import com.example.bysj2020.dialog.WheelDialog
import com.example.bysj2020.event.InfoEvent
import com.example.bysj2020.https.HttpResult
import com.example.bysj2020.https.RxHttp
import com.example.bysj2020.statelayout.LoadHelper
import com.example.bysj2020.utils.DateUtil
import com.example.bysj2020.utils.LoadImageUtil
import com.example.bysj2020.utils.MyInfoUtils
import com.example.bysj2020.utils.SpUtil
import com.google.gson.JsonObject
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import kotlinx.android.synthetic.main.activity_personal_information.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 *    Author : wxz
 *    Time   : 2020/03/01
 *    Desc   : 个人信息
 */
class PersonalInformation : BaseActivity() {

    private var loading: QMUITipDialog? = null
    private var wheelDialog: WheelDialog? = null
    private var timePickerDialog: TimePickerDialog? = null
    private var userInfoBean: UserInfoBean? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_personal_information
    }

    override fun isRegisterEventBus(): Boolean {
        return false
    }

    override fun initViews() {
        setTitle("个人信息")
        setBack()
        setRightText("保存")
        initStateLayout(object : LoadHelper.EmptyClickListener {
            override fun reload() {
                getDataList()
            }

        })
        loading = QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord("保存中...")
            .create()
        getDataList()
    }

    override fun setViewClick() {
        right_text.setOnClickListener(this)
        personal_info_sex_lay.setOnClickListener(this)
        personal_info_birthday_lay.setOnClickListener(this)
        personal_info_hobby_lay.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.right_text -> {
                //保存
                //进行验证
                if (personal_info_name_et.text.toString().length > 12) {
                    showToast("名称不能超过12个字符")
                    return
                }
                val birthday = DateUtil.stringToDate(
                    personal_info_birthday_tv.text.toString(),
                    Format.YEAR_MONTH_DAY_CROSS
                )
                val current =
                    DateUtil.longToDate(System.currentTimeMillis(), Format.YEAR_MONTH_DAY_CROSS)
                if (birthday != null && birthday > current) {
                    showToast("生日时间不能大于当天时间!")
                    return
                }
                saveInfo()
            }
            R.id.personal_info_sex_lay -> {
                //选择性别
                showSexDialog()
            }
            R.id.personal_info_birthday_lay -> {
                //选择出生日期
                showBirthdayDialog()
            }
            R.id.personal_info_hobby_lay -> {
                //编辑爱好
                startActivityForResult(
                    Intent(this, MyInfoEdit::class.java)
                        .putExtra("type", 1)
                        .putExtra(
                            "content",
                            if (userInfoBean!!.hobbit.isNotBlank()) userInfoBean!!.hobbit else ""
                        ), 0
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                userInfoBean!!.hobbit = data.getStringExtra("editContent")
                personal_info_hobby_tv.text = "已完善"
                personal_info_hobby_tv.setTextColor(resources.getColor(R.color.gray_8))
            }
        }
    }

    /**
     * 显示性别选择器弹框
     */
    private fun showSexDialog() {
        if (wheelDialog == null) {
            wheelDialog = WheelDialog("性别", this)
        }
        wheelDialog?.setWheelCallBack(object : WheelDialog.WheelCallBack {
            override fun onSelect(key: String, options1: Int, options2: Int, options3: Int) {
                val sexEnum = MyInfoUtils.sexList[options1]
                personal_info_sex_tv.text = sexEnum.typeName
                userInfoBean!!.sex = sexEnum.type
            }
        })
        wheelDialog?.setOptionsItems(MyInfoUtils.myInfoSex)
        wheelDialog?.show()
    }

    /**
     * 显示日历选择器弹框
     */
    private fun showBirthdayDialog() {
        if (timePickerDialog == null) {
            timePickerDialog = TimePickerDialog("birthday", this)
        }
        timePickerDialog?.setTimerPickerCallBack(object : TimePickerDialog.TimePickerCallBack {
            override fun onTimeSelect(key: String, date: Date) {
                personal_info_birthday_tv.text =
                    DateUtil.dateToString(date, Format.YEAR_MONTH_DAY_CROSS)
                userInfoBean?.birthDay = DateUtil.dateToString(date, Format.YEAR_MONTH_DAY_CROSS)
            }
        })
        timePickerDialog?.show()
    }

    override fun getContentView(): View {
        return personal_info_scrollView
    }

    /**
     * 获取数据
     */
    private fun getDataList() {
        val rxHttp = RxHttp(this)
        addLifecycle(rxHttp)
        var map = mapOf<String, String>()
        rxHttp.getWithJson("basicUserInfo", map, object : HttpResult<UserInfoBean> {
            override fun OnSuccess(t: UserInfoBean?, msg: String?) {
                userInfoBean = t
                setData()
                showContent()
            }

            override fun OnFail(code: Int, msg: String?) {
                showError("", msg!!)
                showToast(msg!!)
            }
        })
    }

    /**
     * 设置数据
     */
    private fun setData() {
        LoadImageUtil(this).loadImage(
            personal_info_head,
            userInfoBean?.avatar,
            R.mipmap.default_head
        )
        personal_info_name_et.setText(userInfoBean?.nickname)
        personal_info_name_et.hint = "原名称为（${userInfoBean?.nickname}）"
        personal_info_sex_tv.text = MyInfoUtils.myInfoSex[userInfoBean!!.sex]
        personal_info_birthday_tv.text = userInfoBean?.birthDay
        if (userInfoBean?.hobbit.isNullOrBlank()) {
            personal_info_hobby_tv.text = "未完善"
            personal_info_hobby_tv.setTextColor(resources.getColor(R.color.prompt_red))
        } else {
            personal_info_hobby_tv.text = "已完善"
            personal_info_hobby_tv.setTextColor(resources.getColor(R.color.gray_8))
        }
        personal_info_phone_tv.text = userInfoBean?.phone
    }

    /**
     * 保存信息
     */
    private fun saveInfo() {
        loading?.show()
        val rxHttp = RxHttp(this)
        addLifecycle(rxHttp)
        val body = JsonObject()
        body.addProperty("avatar", userInfoBean?.avatar)
        body.addProperty("nickname", personal_info_name_et.text.toString())
        body.addProperty("sex", userInfoBean?.sex)
        body.addProperty("birthDay", personal_info_birthday_tv.text.toString())
        body.addProperty("hobbit", userInfoBean?.hobbit)
        rxHttp.postWithJson("modifyUserInfo", body, object : HttpResult<String> {
            override fun OnSuccess(t: String?, msg: String?) {
                loading?.dismiss()
                showToast("保存成功")
                SpUtil.Save(this@PersonalInformation, "avatar", userInfoBean?.avatar)
                if (personal_info_name_et.text.toString().isNotBlank()) {
                    SpUtil.Save(
                        this@PersonalInformation,
                        "nickname",
                        personal_info_name_et.text.toString()
                    )
                    EventBus.getDefault().post(InfoEvent(0))
                } else {
                    personal_info_name_et.setText(userInfoBean?.nickname)
                }
                SpUtil.Save(this@PersonalInformation, "sex", personal_info_sex_tv.text.toString())
            }

            override fun OnFail(code: Int, msg: String?) {
                loading?.dismiss()
                showToast(msg!!)
            }
        })
    }
}

