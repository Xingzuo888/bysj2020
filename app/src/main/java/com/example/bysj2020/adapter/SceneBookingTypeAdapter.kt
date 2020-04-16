package com.example.bysj2020.adapter

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.example.bysj2020.R
import com.example.bysj2020.bean.SceneTickInfos
import com.example.bysj2020.common.BaseAdapter
import com.example.bysj2020.common.BaseRecyclerHolder
import com.example.bysj2020.utils.ToastUtil

/**
 *    Author : wxz
 *    Time   : 2020/04/12
 *    Desc   : 景点门票类型适配器
 */
class SceneBookingTypeAdapter(
    mList: List<SceneTickInfos>,
    val context: Context,
    val selectId: Int
) :
    BaseAdapter<SceneTickInfos>(mList, context) {
    private lateinit var click: SceneBookingTypeNumClick
    override fun covert(
        holder: BaseRecyclerHolder,
        mList: MutableList<SceneTickInfos>,
        position: Int
    ) {
        holder.setText(R.id.item_booking_type_name, mList[position].name)
        holder.setText(R.id.item_booking_type_money, "￥${mList[position].price}")
        val numTextView = holder.getView<AppCompatTextView>(R.id.item_booking_type_num)
        numTextView.text = if (mList[position].id == selectId) "1" else "0"
        holder.getView<AppCompatTextView>(R.id.item_booking_type_reduce)
            .setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    if (numTextView.text.toString().toInt() <= 0) {
                        return
                    } else {
                        numTextView.text = "${numTextView.text.toString().toInt() - 1}"
                        click.reduceNum(
                            position
                        )
                    }
                }
            })
        holder.getView<AppCompatTextView>(R.id.item_booking_type_add)
            .setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    if (numTextView.text.toString().toInt() >= 10) {
                        ToastUtil.setToast(context, "该票型最多能选择10张")
                        return
                    } else {
                        numTextView.text = "${numTextView.text.toString().toInt() + 1}"
                        click.addNum(
                            position
                        )
                    }
                }
            })
    }

    override fun getLayoutId(): Int {
        return R.layout.item_booking_type
    }

    override fun getContentLength(): Int {
        return 10
    }

    fun setClick(click: SceneBookingTypeNumClick) {
        this.click = click
    }

    interface SceneBookingTypeNumClick {
        fun reduceNum(position: Int)
        fun addNum(position: Int)
    }
}