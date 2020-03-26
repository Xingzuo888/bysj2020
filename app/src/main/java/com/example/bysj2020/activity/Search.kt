package com.example.bysj2020.activity

import android.graphics.PorterDuff
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import com.example.bysj2020.R
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.bean.SearchRecommendBean
import com.example.bysj2020.dialog.CustomDialog
import com.example.bysj2020.greendao.SearchHistoryBean
import com.example.bysj2020.greendao.SearchHistoryDBManager
import com.example.bysj2020.https.HttpResult
import com.example.bysj2020.https.RxHttp
import com.example.bysj2020.statelayout.LoadHelper
import com.example.bysj2020.utils.ToastUtil
import com.example.bysj2020.view.flow.FlowTagLayout
import com.example.bysj2020.view.flow.OnTagClickListener
import com.example.bysj2020.view.flow.TagAdapter
import kotlinx.android.synthetic.main.activity_search.*

/**
 *    Author : wxz
 *    Time   : 2020/02/29
 *    Desc   : 搜索页
 */

class Search : BaseActivity() {

    private var searchRecommendBean: SearchRecommendBean? = null
    private lateinit var searchHistoryDBManager: SearchHistoryDBManager //数据库操作
    private lateinit var searchHistoryAdapter: TagAdapter<String>
    private lateinit var searchRecommendAdapter: TagAdapter<String>
    private var historyList = mutableListOf<String>()

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    override fun isRegisterEventBus(): Boolean {
        return false
    }

    override fun initViews() {
        initStateLayout(object : LoadHelper.EmptyClickListener {
            override fun reload() {
                getDataList()
            }

        })
        //设置标题栏
        val upArrow = ContextCompat.getDrawable(this, R.drawable.ic_keyboard_arrow_left_black)
        upArrow?.setColorFilter(
            ContextCompat.getColor(this, R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
        setSupportActionBar(search_toolbar)
        supportActionBar?.setHomeAsUpIndicator(upArrow)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        searchHistoryDBManager = SearchHistoryDBManager.getInstance(this)
        search_history_lay.visibility = View.GONE
        search_recommend_lay.visibility = View.GONE

        //初始化标签布局
        searchHistoryAdapter = TagAdapter(this)
        search_history_content.adapter = searchHistoryAdapter
        search_history_content.setOnTagClickListener(object : OnTagClickListener {
            override fun onItemClick(parent: FlowTagLayout?, view: View?, position: Int) {
                showToast(parent!!.adapter.getItem(position) as String)
            }
        })
        searchRecommendAdapter = TagAdapter(this)
        search_recommend_content.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_NONE)
        search_recommend_content.adapter = searchRecommendAdapter
        search_recommend_content.setOnTagClickListener(object : OnTagClickListener {
            override fun onItemClick(parent: FlowTagLayout?, view: View?, position: Int) {
                showToast(parent!!.adapter.getItem(position) as String)
            }
        })

        getDataList()
    }

    private fun getSearchHistoryData() {
        val query = searchHistoryDBManager.query()
        historyList.removeAll(historyList)
        for (i in (query.size - 1) downTo 0) {
            historyList.add(query[i].name)
        }
        if (historyList != null && historyList.size > 0) {
            search_history_lay.visibility = View.VISIBLE
            searchHistoryAdapter.clearAndAddAll(historyList)
        } else {
            search_history_lay.visibility = View.GONE
        }
    }

    private fun initSearchRecommendData() {
        if (searchRecommendBean != null) {
            search_recommend_lay.visibility = View.VISIBLE
            searchRecommendAdapter.onlyAddAll(searchRecommendBean!!.recommendTagList)
        }
    }

    override fun setViewClick() {
        search_toolbar_top_tv.setOnClickListener(this)
        search_history_clear.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.search_toolbar_top_tv -> {
                //搜索
                if (search_searchView.text.toString().isNotBlank()) {
                    if (!historyList.contains(search_searchView.text.toString())) {
                        searchHistoryDBManager.insert(
                            SearchHistoryBean(
                                historyList.size.toLong(),
                                search_searchView.text.toString()
                            )
                        )
                        ToastUtil.setToast(this, "搜索")
                        getSearchHistoryData()
                    } else {
                        showToast("数据库已有")
                    }
                } else {
                    showToast("请输入要搜索的内容")
                }
            }
            R.id.search_history_clear -> {
                //清空历史搜索
                showClearDialog()

            }
        }
    }

    private fun showClearDialog() {
        val dialog = CustomDialog(this)
        dialog.setTitle("清除搜索记录")
        dialog.setContentMsg("删除后不能恢复，是否删除？")
        dialog.setCancleable(false)
        dialog.setNegativeButton("取消", View.OnClickListener { dialog.dismiss() })
        dialog.setPositiveButton("确定", View.OnClickListener {
            searchHistoryDBManager.deleteAll()
            getSearchHistoryData()
            dialog.dismiss()
        })
        dialog.show()
    }

    override fun getContentView(): View {
        return search_scrollView
    }

    private fun getDataList() {
        val rxHttp = RxHttp(this)
        addLifecycle(rxHttp)
        rxHttp.getWithJson(
            "searchRecommend",
            mapOf<String, String>(),
            object : HttpResult<SearchRecommendBean> {
                override fun OnSuccess(t: SearchRecommendBean?, msg: String?) {
                    if (t != null) {
                        searchRecommendBean = t
                    }
                    getSearchHistoryData()
                    initSearchRecommendData()
                    showContent()
                }

                override fun OnFail(code: Int, msg: String?) {
                    showError()
                    showToast(msg!!)
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                hideKeyboard()
                finish()
            }
        }
        return true
    }
}
