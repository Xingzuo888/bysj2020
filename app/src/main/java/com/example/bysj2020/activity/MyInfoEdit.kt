package com.example.bysj2020.activity

import android.view.View
import com.example.bysj2020.R
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.enums.EditInfoEnum
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import kotlinx.android.synthetic.main.activity_my_info_edit.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 *    Author : wxz
 *    Time   : 2020/03/23
 *    Desc   : 编辑简介
 */

class MyInfoEdit : BaseActivity() {

    private var loadingDialog: QMUITipDialog? = null
    private var contents: String = ""
    private var type: Int = 0 //描述类型

    override fun getLayoutId(): Int {
        return R.layout.activity_my_info_edit
    }

    override fun initViews() {
        setBack()
        setRightText("完成")
        type = intent.getIntExtra("type", 0)
        contents = intent.getStringExtra("content")
        if (contents.isNotBlank()) edit_info.setText(contents)
        loadingDialog =
            QMUITipDialog.Builder(this).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在提交").create()
        setInfoTitle()
    }

    /**
     * 设置初始化数据
     */
    private fun setInfoTitle() {
        when (type) {
            EditInfoEnum.PERSONAL_PROFILE.type -> {
                setTitle("个人简介")
            }
            EditInfoEnum.HOBBY.type -> {
                setTitle("编辑爱好")
            }
        }
        if (type != EditInfoEnum.PERSONAL_PROFILE.type) {//不为个人简介时,标题设置为
            edit_info.hint = "请填写您的${toolbar_title.text.toString().replace("编辑", "")}"
        }
    }

    override fun setViewClick() {
        right_text.setOnClickListener(this)
    }

    override fun isRegisterEventBus(): Boolean {
        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.right_text -> {
                if (edit_info.text.toString().isNullOrEmpty()) {
                    showToast("编辑内容不为空")
                    return
                }
                hideKeyboard(edit_info)
                setResult(RESULT_OK, intent.putExtra("editContent", edit_info.text.toString()))
                finish()
            }
        }
    }

}
