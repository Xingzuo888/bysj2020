package com.example.bysj2020.adapter

import android.content.Context
import com.example.bysj2020.R
import com.example.bysj2020.bean.OrderListRecord
import com.example.bysj2020.common.BaseAdapter
import com.example.bysj2020.common.BaseRecyclerHolder

/**
 *    Author : wxz
 *    Time   : 2020/04/13
 *    Desc   : 订单列表适配器
 */
class OrderListAdapter(mList: List<OrderListRecord>, context: Context) :
    BaseAdapter<OrderListRecord>(mList, context) {
    override fun covert(
        holder: BaseRecyclerHolder,
        mList: MutableList<OrderListRecord>,
        position: Int
    ) {
        holder.setImageResource(R.id.item_orderList_img, mList[position].img)
        holder.setText(R.id.item_orderList_name, mList[position].name)
        holder.setText(R.id.item_orderList_type, mList[position].typeName)
        holder.setText(R.id.item_orderList_money, "￥${mList[position].totalAmount}")
        holder.setText(R.id.item_orderList_state, mList[position].status)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_order_list
    }

    override fun getContentLength(): Int {
        return 10
    }
}