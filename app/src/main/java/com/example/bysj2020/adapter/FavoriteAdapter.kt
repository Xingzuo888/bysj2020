package com.example.bysj2020.adapter

import android.content.Context
import com.example.bysj2020.R
import com.example.bysj2020.bean.FavoriteRecord
import com.example.bysj2020.common.BaseAdapter
import com.example.bysj2020.common.BaseRecyclerHolder

/**
 *    Author : wxz
 *    Time   : 2020/04/12
 *    Desc   : 收藏适配器
 */
class FavoriteAdapter(mList: List<FavoriteRecord>, context: Context) : BaseAdapter<FavoriteRecord>(mList, context) {
    override fun covert(holder: BaseRecyclerHolder, mList: MutableList<FavoriteRecord>, position: Int) {
        holder.setImageResource(R.id.item_favorite_img, mList[position].avatar)
        holder.setText(R.id.item_favorite_name, mList[position].name)
        holder.setText(R.id.item_favorite_date, "收藏时间：${mList[position].viewTime}")
    }

    override fun getLayoutId(): Int {
        return R.layout.item_favorite
    }

    override fun getContentLength(): Int {
        return 10
    }
}