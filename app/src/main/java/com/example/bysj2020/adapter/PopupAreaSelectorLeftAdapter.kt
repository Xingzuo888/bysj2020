package com.example.bysj2020.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.bysj2020.R
import com.example.bysj2020.bean.AreaProvincesBean
import com.example.bysj2020.common.BaseAdapter
import com.example.bysj2020.common.BaseRecyclerHolder

/**
 *    Author : wxz
 *    Time   : 2020/03/29
 *    Desc   :
 */
class PopupAreaSelectorLeftAdapter(mList: List<AreaProvincesBean>, context: Context) :
    BaseAdapter<AreaProvincesBean>(mList, context) {
    var provincesId: String = "-1" //默认选择全国
    override fun covert(
        holder: BaseRecyclerHolder,
        mList: List<AreaProvincesBean>,
        position: Int
    ) {
        val areaProvincesBean = mList[position]
        val rootView = holder.getView<ConstraintLayout>(R.id.item_area_selector_left_lay)
        val txt = holder.getView<TextView>(R.id.area_selector_content)
        txt.text = areaProvincesBean.name
        txt.setTextColor(Color.parseColor(if (areaProvincesBean.code == provincesId) "#222222" else "#888888"))
        rootView.setBackgroundColor(Color.parseColor(if (areaProvincesBean.code == provincesId) "#FFFFFF" else "#F4F4F4"))
        val line = holder.getView<View>(R.id.area_selector_line)
        line.visibility =
            if (areaProvincesBean.code == provincesId) View.VISIBLE else View.GONE
    }

    override fun getLayoutId(): Int {
        return R.layout.item_area_selector
    }

    override fun getContentLength(): Int {
        return 10
    }
}