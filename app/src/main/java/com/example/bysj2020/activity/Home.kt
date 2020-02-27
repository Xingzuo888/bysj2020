package com.example.bysj2020.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bysj2020.R
import com.example.bysj2020.adapter.ViewPagerAdapters
import com.example.bysj2020.fragment.Address
import com.example.bysj2020.fragment.Home
import com.example.bysj2020.fragment.Mine
import com.example.bysj2020.utils.ActivityManagerUtil
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.bottom_bar.*

/**
 * Author : wxz
 * Time   : 2020/02/19
 * Desc   : 首页
 */
class Home : AppCompatActivity(),View.OnClickListener {

    private var homeFragments: MutableList<Fragment>? = null
    private var clickTime: Long = 0 //记录第一次点击的时间

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        ImmersionBar.with(this).statusBarColor(R.color.green).statusBarDarkFont(true).init()
        ActivityManagerUtil.addDestroyActivity(this@Home, javaClass.name)
        initViews()
    }

    override fun onDestroy() {
        ActivityManagerUtil.destroyActivity(javaClass.name)
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun initViews() {
        getHomeFragments()
        val adapters= ViewPagerAdapters(supportFragmentManager,homeFragments)
        home_viewPager!!.adapter=adapters
        //预加载视图个数
        home_viewPager!!.offscreenPageLimit=2
        bottom_radio_home.setOnClickListener(this)
        bottom_radio_address.setOnClickListener(this)
        bottom_radio_mine.setOnClickListener(this)
    }

    /**
     * 添加fragment集合
     */
    private fun getHomeFragments(): List<Fragment> {
        homeFragments=ArrayList()
        (homeFragments as ArrayList<Fragment>).add(Home.newInstance())
        (homeFragments as ArrayList<Fragment>).add(Address.newInstance())
        (homeFragments as ArrayList<Fragment>).add(Mine.newInstance())
        return homeFragments as ArrayList<Fragment>
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bottom_radio_home->{
                ImmersionBar.with(this).statusBarColor(R.color.green).statusBarDarkFont(true)
                    .init()//切换页面，顶部沉浸式修改
                home_viewPager.currentItem = 0
            }
            R.id.bottom_radio_address->{
                ImmersionBar.with(this).statusBarColor(R.color.green).statusBarDarkFont(true)
                    .init()//切换页面，顶部沉浸式修改
                home_viewPager.currentItem = 1
            }
            R.id.bottom_radio_mine->{
                ImmersionBar.with(this).statusBarColor(R.color.green).statusBarDarkFont(true)
                    .init()//切换页面，顶部沉浸式修改
                home_viewPager.currentItem = 2
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
            return true
        }
        return super.onKeyDown(keyCode,event)
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
}
