package com.example.bysj2020.adapter

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatTextView
import com.example.bysj2020.Interface.BookingClickBack
import com.example.bysj2020.R
import com.example.bysj2020.bean.SceneTickInfo
import com.example.bysj2020.common.BaseAdapter
import com.example.bysj2020.common.BaseRecyclerHolder
import com.qmuiteam.qmui.widget.QMUIFloatLayout

/**
 *    Author : wxz
 *    Time   : 2020/04/06
 *    Desc   :
 */
class SceneDetailsTicketAdapter(mList:List<SceneTickInfo>,val context: Context):BaseAdapter<SceneTickInfo>(mList,context) {
    private lateinit var clickBack: BookingClickBack
    override fun covert(
        holder: BaseRecyclerHolder,
        mList: MutableList<SceneTickInfo>,
        position: Int
    ) {
        holder.setText(R.id.item_ticket_name,mList[position].name)
        holder.setText(R.id.item_ticket_money,"${mList[position].price}元")
        val tagLay = holder.getView<QMUIFloatLayout>(R.id.item_ticket_tag)
        if (mList[position].tag.isNotBlank()) {
            val tags = mList[position].tag.split(",")
            for (i in tags.indices) {
                val view = LayoutInflater.from(context).inflate(R.layout.item_scene_tag, null)
                view.findViewById<AppCompatTextView>(R.id.item_scene_tags_content).text=tags[i]
                tagLay.addView(view)
            }
        }
        holder.getView<AppCompatTextView>(R.id.item_ticket_booking).setOnClickListener{
            clickBack.clickBack(mList[position])
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.item_scene_details_booking
    }

    override fun getContentLength(): Int {
        return 10
    }

    fun setClickBack(clickBack: BookingClickBack) {
        this.clickBack=clickBack
    }
}