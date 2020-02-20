package com.example.bysj2020.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.bysj2020.R
import com.example.bysj2020.utils.SpUtil
import com.gyf.immersionbar.ImmersionBar

/**
 * Author : wxz
 * Time   : 2020/02/19
 * Desc   :启动页
 */
class Launch : AppCompatActivity() {

    private var isFirst = true //是否第一次使用此应用
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        ImmersionBar.with(this).statusBarColor(R.color.transparent).statusBarDarkFont(true).init()
        initViews()
    }

    fun initViews() {
        //每次进入APP就调用检查
        isFirst = SpUtil.Obtain(this, "isFirstLaunch", true) as Boolean
        if (isFirst) {
            handler.postDelayed({
                startActivity(Intent(this@Launch, Welcome::class.java))
                finish()
            }, 1000)

        } else {
            handler.postDelayed({
                startActivity(Intent(this@Launch, Home::class.java))
                finish()
            }, 1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
