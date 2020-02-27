package com.example.bysj2020.adapter;

import android.content.Context;
import android.widget.RatingBar;

import com.example.bysj2020.R;
import com.example.bysj2020.bean.FHomeRecyclerBean;
import com.example.bysj2020.common.BaseAdapter;
import com.example.bysj2020.common.BaseRecyclerHolder;

import java.util.List;

/**
 * Author : wxz
 * Time   : 2020/02/25
 * Desc   : fragment首页RecyclerView的适配器
 */
public class FHomeRecyclerViewAdapter extends BaseAdapter<FHomeRecyclerBean> {
    public FHomeRecyclerViewAdapter(List<FHomeRecyclerBean> mList, Context context) {
        super(mList, context);
    }

    @Override
    public void covert(BaseRecyclerHolder holder, List<FHomeRecyclerBean> mList, int position) {
        holder.setImageResource(R.id.hri_iv, mList.get(position).getImgUrl());
        holder.setText(R.id.hri_tv, mList.get(position).getName());
        RatingBar ratingBar = holder.getView(R.id.hri_rb);
        ratingBar.setRating(mList.get(position).getStar());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_recycler_view_item;
    }

    @Override
    protected int getContentLength() {
        return 3;
    }
}
