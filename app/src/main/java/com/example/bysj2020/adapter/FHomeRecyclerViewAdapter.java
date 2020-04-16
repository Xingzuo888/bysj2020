package com.example.bysj2020.adapter;

import android.content.Context;
import android.widget.RatingBar;

import com.example.bysj2020.R;
import com.example.bysj2020.bean.MustPlayScene;
import com.example.bysj2020.common.BaseAdapter;
import com.example.bysj2020.common.BaseRecyclerHolder;

import java.util.List;

/**
 * Author : wxz
 * Time   : 2020/02/25
 * Desc   : fragment首页RecyclerView的适配器
 */
public class FHomeRecyclerViewAdapter extends BaseAdapter<MustPlayScene> {
    public FHomeRecyclerViewAdapter(List<MustPlayScene> mList, Context context) {
        super(mList, context);
    }

    @Override
    public void covert(BaseRecyclerHolder holder, List<MustPlayScene> mList, int position) {
        holder.setImageResource(R.id.hri_iv, mList.get(position).getSmallImg());
        holder.setText(R.id.hri_tv, mList.get(position).getName());
        RatingBar ratingBar = holder.getView(R.id.hri_rb);
        ratingBar.setRating(mList.get(position).getStar() / 2f);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_home_recycler_view;
    }

    @Override
    protected int getContentLength() {
        return 3;
    }
}
