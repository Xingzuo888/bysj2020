package com.example.bysj2020.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.example.bysj2020.R
import com.example.bysj2020.bean.HotelRecord
import com.example.bysj2020.common.BaseAdapter
import com.example.bysj2020.common.BaseRecyclerHolder
import com.example.bysj2020.utils.TextViewUtil
import com.qmuiteam.qmui.widget.QMUIFloatLayout

/**
 *    Author : wxz
 *    Time   : 2020/04/01
 *    Desc   : 酒店列表适配器
 */
class HotelListAdapter(mList: List<HotelRecord>, val context: Context) :
    BaseAdapter<HotelRecord>(mList, context) {
    override fun covert(
        holder: BaseRecyclerHolder,
        mList: List<HotelRecord>,
        position: Int
    ) {
        holder.setImageResource(R.id.item_search_list_img, mList[position].img)
        holder.setText(R.id.item_search_list_title, mList[position].name)
        holder.setText(R.id.item_search_list_score1, "${mList[position].star}分")
//        holder.setText(R.id.item_search_list_score2, "${mList[position].reviewNum}条点评")
        holder.getView<AppCompatTextView>(R.id.item_search_list_score2).visibility = View.GONE
        holder.setText(R.id.item_search_list_address, mList[position].address)
        val money = holder.getView<AppCompatTextView>(R.id.item_search_list_money)
        money.text = TextViewUtil("${mList[position].price}元起").setTextSize(
            mList[position].price.toString(),
            26
        ).create()
        val linearLayout = holder.getView<QMUIFloatLayout>(R.id.item_search_list_label_lay)
        linearLayout.removeAllViews()
        if (mList[position].tag.isNotBlank()) {
            val tagList = mList[position].tag.split(",").toList()
            for (i in tagList.indices) {
                val view =
                    LayoutInflater.from(context).inflate(R.layout.item_search_list_label, null)
                val textView =
                    view.findViewById<AppCompatTextView>(R.id.item_search_list_label_content)
                textView.text = tagList[i]
                linearLayout.addView(view)
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.item_list
    }

    override fun getContentLength(): Int {
        return 10
    }
}