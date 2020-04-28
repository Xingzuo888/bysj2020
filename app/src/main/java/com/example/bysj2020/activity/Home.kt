package com.example.bysj2020.activity

import android.Manifest
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.example.bysj2020.R
import com.example.bysj2020.adapter.ViewPagerAdapters
import com.example.bysj2020.amaplocation.AMapLocationClientListener
import com.example.bysj2020.event.SwitchFragmentEvent
import com.example.bysj2020.fragment.Address
import com.example.bysj2020.fragment.Home
import com.example.bysj2020.fragment.Mine
import com.example.bysj2020.utils.ActivityManagerUtil
import com.example.bysj2020.utils.ToastUtil
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.bottom_bar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import permissions.dispatcher.*

/**
 * Author : wxz
 * Time   : 2020/02/19
 * Desc   : 首页
 */
@RuntimePermissions
class Home : AppCompatActivity(), View.OnClickListener {

    public lateinit var mLocationClient: AMapLocationClient //声明AMapLocationClient类对象
    public lateinit var mLocationListener:AMapLocationListener //声明定位回调监听器
    public lateinit var mLocationOption:AMapLocationClientOption //声明AMapLocationClientOption对象

    private var homeFragments: MutableList<Fragment>? = null
    private var clickTime: Long = 0 //记录第一次点击的时间


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        ImmersionBar.with(this).statusBarColor(R.color.green).statusBarDarkFont(true).init()
        ActivityManagerUtil.addDestroyActivity(this@Home, javaClass.name)
        showAllPermission()
        initLocation()
        initViews()
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        ActivityManagerUtil.destroyActivity(javaClass.name)
        EventBus.getDefault().unregister(this)
        //销毁定位客户端，同时销毁本地定位服务。
        mLocationClient.onDestroy()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    //初始化定位
    private fun initLocation() {
        //初始化定位
        mLocationClient= AMapLocationClient(applicationContext)
        //初始化定位监听
        mLocationListener= AMapLocationClientListener(this,mLocationClient)
        //初始化配置
        mLocationOption= AMapLocationClientOption()
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener)
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Battery_Saving
        //设置单次定位
        mLocationOption.isOnceLocation = false
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.isNeedAddress=true
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.httpTimeOut = 20000
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption)
        //启动定位
        mLocationClient.startLocation()
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
                home_viewPager.currentItem = 0
            }
            R.id.bottom_radio_address -> {
                home_viewPager.currentItem = 1
            }
            R.id.bottom_radio_mine -> {
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
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE
    )
    fun showAllPermission() {

    }

    /**
     * 注释执行需要一个或多个权限的操作的方法
     */
    @OnShowRationale(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE
    )
    fun ShowRationaleForAllPermission(request: PermissionRequest) {
        request.proceed()
    }

    /**
     * 注释如果用户未授予权限则调用的方法
     */
    @OnPermissionDenied(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE
    )
    fun onPermissionDenied() {
        ToastUtil.setToast(this, "未授权，部分功能将无法使用")
    }

    /**
     * 注释一个方法，如果用户选择让设备“不再询问”权限，则调用该方法
     */
    @OnNeverAskAgain(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE
    )
    fun onNeverAskAgain() {
        ToastUtil.setToast(this, "未授权，部分功能将无法使用,如果要使用，请到设置中打开")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun switchFragmentEvent(event: SwitchFragmentEvent) {
        when (event.code) {
            0 -> {
                home_viewPager.currentItem = event.num
                when (event.num) {
                    0 -> bottom_group.check(R.id.bottom_radio_home)
                    1 -> bottom_group.check(R.id.bottom_radio_address)
                    2 -> bottom_group.check(R.id.bottom_radio_mine)
                }
            }
        }
    }
}
