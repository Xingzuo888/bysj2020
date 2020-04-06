package com.example.bysj2020.adapter

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import com.example.bysj2020.Interface.BookingClickBack
import com.example.bysj2020.R
import com.example.bysj2020.bean.HotelRoomInfo
import com.example.bysj2020.common.BaseAdapter
import com.example.bysj2020.common.BaseRecyclerHolder

/**
 *    Author : wxz
 *    Time   : 2020/04/05
 *    Desc   : 房间类型适配器
 */
class HotelDetailsRoomAdapter(mList: List<HotelRoomInfo>, context: Context) :
    BaseAdapter<HotelRoomInfo>(mList, context) {
    private lateinit var clickBack: BookingClickBack
    override fun covert(
        holder: BaseRecyclerHolder,
        mList: MutableList<HotelRoomInfo>,
        position: Int
    ) {
        holder.setImageResource(R.id.item_room_img, mList[position].img)
        holder.setText(R.id.item_room_type, mList[position].roomType)
        holder.setText(R.id.item_room_size, mList[position].roomSize)
        holder.setText(R.id.item_room_tag, mList[position].roomTag)
        holder.setText(R.id.item_room_money, "${mList[position].price}元")
        holder.getView<AppCompatTextView>(R.id.item_room_booking)
            .setOnClickListener { clickBack.clickBack(mList[position]) }
    }

    override fun getLayoutId(): Int {
        return R.layout.item_hotel_details_booking
    }

    override fun getContentLength(): Int {
        return 10
    }

    fun setClickBack(clickBack: BookingClickBack) {
        this.clickBack = clickBack
    }
}