package com.example.bysj2020.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import com.example.bysj2020.R
import com.example.bysj2020.bean.SceneRecord
import com.example.bysj2020.common.BaseAdapter
import com.example.bysj2020.common.BaseRecyclerHolder
import com.example.bysj2020.utils.TextViewUtil

/**
 *    Author : wxz
 *    Time   : 2020/04/01
 *    Desc   : 景点列表适配器
 */
class SceneListAdapter(mList: List<SceneRecord>, val context: Context) :
    BaseAdapter<SceneRecord>(mList, context) {
    override fun covert(
        holder: BaseRecyclerHolder,
        mList: List<SceneRecord>,
        position: Int
    ) {
        holder.setImageResource(R.id.item_search_list_img, mList[position].smallImg)
        holder.setText(R.id.item_search_list_title, mList[position].name)
        holder.setText(R.id.item_search_list_score1, "${mList[position].star}分")
        val reViewNum = holder.getView<AppCompatTextView>(R.id.item_search_list_score2)
        if (mList[position].reviewNum != 0) {
            reViewNum.text = "${mList[position].reviewNum}条点评"
            reViewNum.visibility = View.VISIBLE
        } else {
            reViewNum.visibility = View.GONE
        }
        holder.setText(R.id.item_search_list_address, mList[position].address)
        val money = holder.getView<AppCompatTextView>(R.id.item_search_list_money)
        money.text = TextViewUtil("${mList[position].price}元起").setTextSize(
            mList[position].price.toString(),
            26
        ).create()

        if (mList[position].tag.isNotBlank()) {
            val tagList = mList[position].tag.split(",").toList()
            val linearLayout = holder.getView<LinearLayout>(R.id.item_search_list_label_lay)
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