package com.example.bysj2020.activity

import android.view.View
import com.example.bysj2020.BuildConfig
import com.example.bysj2020.R
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.dialog.CustomDialog
import com.example.bysj2020.global.Config
import com.example.bysj2020.utils.CacheUtil
import com.example.bysj2020.utils.SpUtil
import com.example.bysj2020.utils.ToastUtil
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import kotlinx.android.synthetic.main.activity_system_setting.*
import java.io.File

/**
 *    Author : wxz
 *    Time   : 2020/03/03
 *    Desc   : 系统设置
 */
class SystemSetting : BaseActivity() {
    private var tipDialog: QMUITipDialog? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_system_setting
    }

    override fun initViews() {
        setBack()
        setTitle("系统设置")
        system_setting_version.text = BuildConfig.VERSION_NAME
        val cacheSize = CacheUtil.getCacheSize(
            Config.CACHE + SpUtil.Obtain(this, "mobilePhone", "default") + File.separator
        )
        system_setting_cache.text = if (cacheSize == "0.0K") "0M" else cacheSize
        tipDialog = QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord("加载中...")
            .create()
    }

    override fun setViewClick() {
        logout.setOnClickListener(this)
        system_setting_about_lay.setOnClickListener(this)
        system_setting_cache_lay.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.system_setting_about_lay -> {
                //关于
                ToastUtil.setToast(this, "当前版本为：${system_setting_version.text}")
            }
            R.id.system_setting_cache_lay -> {
                //清除缓存
                showCacheDialog()
            }
            R.id.logout -> {
                //退出登录
                showExitDialog()
            }
        }
    }

    /**
     * 显示清除缓存对话框
     */
    private fun showCacheDialog() {
        val dialog = CustomDialog(this)
        dialog.setTitle("清除缓存")
        dialog.setContentMsg("您将要清除应用内所有缓存")
        dialog.setCancleable(false)
        dialog.setPositiveButton("确定", View.OnClickListener {
            dialog.dismiss()
            CacheUtil.cleanCache(
                Config.CACHE + SpUtil.Obtain(
                    this,
                    "mobilePhone",
                    "default"
                ) + File.separator
            )
            system_setting_cache.text = "0M"
            showToast("清除成功")
        })
        dialog.setNegativeButton("取消", View.OnClickListener {
            dialog.dismiss()
        })
        dialog.show()
    }

    /**
     * 显示退出登录对话框
     */
    private fun showExitDialog() {
        val dialog = CustomDialog(this)
        dialog.setTitle("退出提示")
        dialog.setContentMsg("是否确认退出？")
        dialog.setCancleable(false)
        dialog.setPositiveButton("取消", View.OnClickListener {
            dialog.dismiss()
        })
        dialog.setNegativeButton("确定", View.OnClickListener {
            dialog.dismiss()
            outLogin()
        })
        dialog.show()
    }

    /**
     * 退出登录
     */
    private fun outLogin() {

    }

    override fun onDestroy() {
        tipDialog?.dismiss()
        super.onDestroy()
    }
}
