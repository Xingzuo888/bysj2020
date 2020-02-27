package com.example.bysj2020.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.bysj2020.R
import com.example.bysj2020.adapter.ViewPagerAdapters
import com.example.bysj2020.fragment.Guide
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_welcome.*

/**
 *    Author : wxz
 *    Time   : 2020/02/18
 *    Desc   : 欢迎页
 */
class Welcome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        ImmersionBar.with(this).statusBarColor(R.color.transparent).statusBarDarkFont(true).init()
        initViews()
    }

    private fun initViews() {
        val mFragments=ArrayList<Fragment>()
        for (i in 0..1) {
            mFragments.add(Guide.newInstance(i))
        }
        mFragments.add(Guide.newInstance(2))
        val adapters=
            ViewPagerAdapters(supportFragmentManager, mFragments)
        welcome_vp!!.adapter=adapters
        welcome_indicator.setmViewPager(welcome_vp)
        welcome_vp!!.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 2) {
                    welcome_indicator.visibility = View.GONE
                } else {
                    welcome_indicator.visibility=View.VISIBLE
                }
            }

        })
    }
}
