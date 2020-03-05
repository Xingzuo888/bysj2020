package com.example.bysj2020.activity

import android.graphics.PorterDuff
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.bysj2020.R
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.statelayout.LoadHelper
import com.example.bysj2020.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_search.*

/**
 *    Author : wxz
 *    Time   : 2020/02/29
 *    Desc   : 搜索页
 */

class Search : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    override fun initViews() {
        initStateLayout(object :LoadHelper.EmptyClickListener{
            override fun reload() {
                getDataList()
            }

        })
        //设置标题栏
        val upArrow = ContextCompat.getDrawable(this, R.drawable.ic_keyboard_arrow_left_black)
        upArrow?.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
        setSupportActionBar(search_toolbar)
        supportActionBar?.setHomeAsUpIndicator(upArrow)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initData()
    }

    private fun initData() {
        val list= listOf("dsgg","丢人事","发人数达","ouie4tr","偶尔他如何","出版包速度快","fkreadygri","大概i会都不能感知","sda","三个","s萨汗国")
        for (element in list) {
            val view = layoutInflater.inflate(R.layout.search_float_item, null)
            val textView=view.findViewById<TextView>(R.id.search_float_item_content)
            textView.text= element
            search_history_content.addView(textView)
        }

        getDataList()
    }

    override fun setViewClick() {
        search_toolbar_top_tv.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.search_toolbar_top_tv->{
                //搜索
                ToastUtil.setToast(this,"搜索")
            }
        }
    }

    override fun getContentView(): View {
        return search_scrollView
    }

    private fun getDataList() {
        showContent()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home->{
                hideKeyboard()
                finish()
            }
        }
        return true
    }
}
