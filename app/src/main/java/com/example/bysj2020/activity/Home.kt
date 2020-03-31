package com.example.bysj2020.activity

import android.Manifest
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.example.bysj2020.BaiduLBS.MyLocationListener
import com.example.bysj2020.R
import com.example.bysj2020.adapter.ViewPagerAdapters
import com.example.bysj2020.fragment.Address
import com.example.bysj2020.fragment.Home
import com.example.bysj2020.fragment.Mine
import com.example.bysj2020.utils.ActivityManagerUtil
import com.example.bysj2020.utils.ToastUtil
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.bottom_bar.*
import permissions.dispatcher.*

/**
 * Author : wxz
 * Time   : 2020/02/19
 * Desc   : 首页
 */
@RuntimePermissions
class Home : AppCompatActivity(), View.OnClickListener {

    private var homeFragments: MutableList<Fragment>? = null
    private var clickTime: Long = 0 //记录第一次点击的时间


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        ImmersionBar.with(this).statusBarColor(R.color.green).statusBarDarkFont(true).init()
        ActivityManagerUtil.addDestroyActivity(this@Home, javaClass.name)
        showAllPermission()
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
        val adapters = ViewPagerAdapters(supportFragmentManager, homeFragments)
        home_viewPager!!.adapter = adapters
        //预加载视图个数
        home_viewPager!!.offscreenPageLimit = 2
        bottom_radio_home.setOnClickListener(this)
        bottom_radio_address.setOnClickListener(this)
        bottom_radio_mine.setOnClickListener(this)
        showAllPermissionWithPermissionCheck()
    }

    /**
     * 添加fragment集合
     */
    private fun getHomeFragments(): List<Fragment> {
        homeFragments = ArrayList()
        (homeFragments as ArrayList<Fragment>).add(Home.newInstance())
        (homeFragments as ArrayList<Fragment>).add(Address.newInstance())
        (homeFragments as ArrayList<Fragment>).add(Mine.newInstance())
        return homeFragments as ArrayList<Fragment>
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bottom_radio_home -> {
                ImmersionBar.with(this).statusBarColor(R.color.green).statusBarDarkFont(true)
                    .init()//切换页面，顶部沉浸式修改
                home_viewPager.currentItem = 0
            }
            R.id.bottom_radio_address -> {
                ImmersionBar.with(this).statusBarColor(R.color.green).statusBarDarkFont(true)
                    .init()//切换页面，顶部沉浸式修改
                home_viewPager.currentItem = 1
            }
            R.id.bottom_radio_mine -> {
                ImmersionBar.with(this).statusBarColor(R.color.green).statusBarDarkFont(true)
                    .init()//切换页面，顶部沉浸式修改
                home_viewPager.currentItem = 2
            }
        }
    }

    /**
     * 注册处理权限
     */
    @NeedsPermission(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    fun showAllPermission() {

    }

    /**
     * 注释执行需要一个或多个权限的操作的方法
     */
    @OnShowRationale(Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun ShowRationaleForAllPermission(request: PermissionRequest) {
        request.proceed()
    }

    /**
     * 注释如果用户未授予权限则调用的方法
     */
    @OnPermissionDenied(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun onPermissionDenied() {
        ToastUtil.setToast(this,"未授权，部分功能将无法使用")
    }
    /**
     * 注释一个方法，如果用户选择让设备“不再询问”权限，则调用该方法
     */
    @OnNeverAskAgain(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun onNeverAskAgain() {
        ToastUtil.setToast(this,"未授权，部分功能将无法使用,如果要使用，请到设置中打开")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode,grantResults)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
            return true
        }
        return super.onKeyDown(keyCode, event)
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
