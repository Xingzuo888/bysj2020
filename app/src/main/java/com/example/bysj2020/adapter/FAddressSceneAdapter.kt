package com.example.bysj2020.adapter

import android.content.Context
import com.example.bysj2020.R
import com.example.bysj2020.bean.SceneImgTopInfo
import com.example.bysj2020.common.BaseAdapter
import com.example.bysj2020.common.BaseRecyclerHolder

/**
 *    Author : wxz
 *    Time   : 2020/03/27
 *    Desc   : 目的地景点适配器
 */
class FAddressSceneAdapter(mList: List<SceneImgTopInfo>, context: Context) :
    BaseAdapter<SceneImgTopInfo>(mList, context) {
    override fun covert(
        holder: BaseRecyclerHolder,
        mList: List<SceneImgTopInfo>,
        position: Int
    ) {
        holder.setImageResource(R.id.ari_iv, mList[position].smallImg)
        holder.setText(R.id.ari_nameC, mList[position].name)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_address_recycler_view
    }

    override fun getContentLength(): Int {
        return 10
    }
}