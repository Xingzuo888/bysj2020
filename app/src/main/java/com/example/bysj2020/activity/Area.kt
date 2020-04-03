package com.example.bysj2020.activity

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bysj2020.R
import com.example.bysj2020.adapter.AreaCityAdapter
import com.example.bysj2020.adapter.AreaProvincesAdapter
import com.example.bysj2020.base.BaseActivity
import com.example.bysj2020.bean.AreaCityBean
import com.example.bysj2020.bean.AreaProvincesBean
import com.example.bysj2020.statelayout.LoadHelper
import com.example.bysj2020.utils.AreaUtils.getAreaDataList
import com.example.bysj2020.utils.AreaUtils.getCityOfAll
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_area.*
import java.util.*

/**
 * Author : wxz
 * Time   : 2020/03/08
 * Desc   : 选择地区
 */
class Area : BaseActivity() {

    private var provincesList: List<AreaProvincesBean>? = null
    private var cityList: List<AreaCityBean>? = null
    private lateinit var provincesAdapter: AreaProvincesAdapter
    private lateinit var cityAdapter: AreaCityAdapter
    private var preciseChoice: Boolean = false //是否是精确选择（不包含全国和省份不限的选择）
    private var cityId: String = "0"
    private var cityName: String = ""
    private var provincesId: String = ""
    private var provincesName: String = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_area
    }

    override fun isRegisterEventBus(): Boolean {
        return false
    }

    override fun getContentView(): View {
        return area_layout
    }

    override fun initViews() {
        setBack()
        setTitle("地区选择")
        preciseChoice = intent.getBooleanExtra("PreciseChoice", false)
        initStateLayout(object : LoadHelper.EmptyClickListener {
            override fun reload() {
                getAreaData()
            }
        })
        getAreaData()
    }

    /**
     * 初始化省份的适配器
     */
    private fun initProvincesAdapter() {
        provincesAdapter = AreaProvincesAdapter(provincesList!!, this)
        area_province_lay.adapter = provincesAdapter
        area_province_lay.layoutManager = LinearLayoutManager(this)
        provincesAdapter.addItemClickListener { view, t, position ->
            //省份的点击事件
            provincesAdapter.provincesId = t.code!!
            provincesId = t.code!!
            provincesName = t.name!!
            provincesAdapter.notifyDataSetChanged()
            //适配城市数据
            cityList = if (!preciseChoice) {
                getCityOfAll(t.citys).toMutableList()
            } else {
                t.citys
            }
            initCityAdapter()
        }
    }


    /**
     * 初始化城市适配器
     */
    private fun initCityAdapter() {
        cityAdapter = AreaCityAdapter(cityList!!, this)
        area_city_lay.adapter = cityAdapter
        area_city_lay.layoutManager = GridLayoutManager(this, 3)
        cityAdapter.addItemClickListener { view, t, position ->
            //城市的点击事件
            cityAdapter.cityId = t.code!!
            cityAdapter.notifyDataSetChanged()
            cityId = t.code!!
            cityName = t.name!!
            setResult(
                Activity.RESULT_OK, intent.putExtra("cityName", cityName)
                    .putExtra("cityId", cityId)
                    .putExtra("provincesId", provincesId)
                    .putExtra("provincesName", provincesName)
            )
            finish()
        }
    }

    override fun setViewClick() {
    }

    override fun onClick(v: View?) {
    }

    /**
     * 获取城市数据
     */
    private fun getAreaData() {
        getAreaDataList(this, object : Observer<List<AreaProvincesBean>> {
            override fun onComplete() {
                showContent()
            }

            override fun onSubscribe(d: Disposable) {
                showLoading()
            }

            override fun onNext(data: List<AreaProvincesBean>) {
                val dataList: MutableList<AreaProvincesBean> = data.toMutableList()
                if (!preciseChoice) {
                    dataList.add(0, AreaProvincesBean("0", "全国", "quanguo", ArrayList()))
                }
                provincesList = dataList
                initProvincesAdapter()
            }

            override fun onError(e: Throwable) {
                showError()
            }
        })
    }
}
