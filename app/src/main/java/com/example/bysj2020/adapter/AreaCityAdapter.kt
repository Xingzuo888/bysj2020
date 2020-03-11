package com.example.bysj2020.adapter

import android.content.Context
import android.graphics.Color
import androidx.appcompat.widget.AppCompatTextView
import com.example.bysj2020.R
import com.example.bysj2020.bean.AreaCityBean
import com.example.bysj2020.common.BaseAdapter
import com.example.bysj2020.common.BaseRecyclerHolder

/**
 *    Author : wxz
 *    Time   : 2020/03/08
 *    Desc   : 城市适配器
 */
class AreaCityAdapter(mList: List<AreaCityBean>, context: Context) :
    BaseAdapter<AreaCityBean>(mList, context) {
    var cityId = "-1"
    override fun covert(
        holder: BaseRecyclerHolder,
        mList: MutableList<AreaCityBean>,
        position: Int
    ) {
        val areaCityBean = mList[position]
        val txt = holder.getView<AppCompatTextView>(R.id.area_city_content)
        txt.text = areaCityBean.name
        txt.setTextColor(Color.parseColor(if (areaCityBean.code == cityId) "#33cb98" else "#888888"))
        txt.setBackgroundResource(if (areaCityBean.code == cityId) R.drawable.area_item_selected_border else R.drawable.area_item_normal_border)

    }

    override fun getLayoutId(): Int {
        return R.layout.item_area_city
    }

    override fun getContentLength(): Int {
        return 10
    }
}