package com.example.bysj2020.adapter

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.bysj2020.R
import com.example.bysj2020.bean.AreaCityBean
import com.example.bysj2020.common.BaseAdapter
import com.example.bysj2020.common.BaseRecyclerHolder

/**
 *    Author : wxz
 *    Time   : 2020/03/29
 *    Desc   :
 */
class PopupAreaSelectorRightAdapter(mList: List<AreaCityBean>, context: Context) :
    BaseAdapter<AreaCityBean>(mList, context) {
    var cityId: String = "-1" //默认选择不限
    override fun covert(
        holder: BaseRecyclerHolder,
        mList: List<AreaCityBean>,
        position: Int
    ) {
        val areaCityBean = mList[position]
        val txt = holder.getView<TextView>(R.id.area_selector_content)
        txt.text = areaCityBean.name
        txt.setTextColor(Color.parseColor(if (areaCityBean.code == cityId) "#222222" else "#888888"))
    }

    override fun getLayoutId(): Int {
        return R.layout.item_area_selector
    }

    override fun getContentLength(): Int {
        return 10
    }
}