package com.example.bysj2020.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bysj2020.Interface.ItemClick;

import java.util.List;

/**
 * Author : wxz
 * Time   : 2020/02/14
 * Desc   : RecyclerView基本适配器
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseRecyclerHolder> {

    private List<T> mList;
    public Context mContext;
    //点击事件
    private ItemClick<T> itemClick;

    public BaseAdapter(List<T> mList, Context context) {
        this.mList = mList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public BaseRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(getLayoutId(), parent, false);
        return BaseRecyclerHolder.getBaseRecyclerHolder(view, getContentLength(), mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerHolder holder, int position) {
        covert(holder, mList, position);
        holder.itemView.setOnClickListener(view -> {
            if (itemClick != null) {
                itemClick.onItemClick(view, mList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addItemClickListener(ItemClick<T> itemClick) {
        this.itemClick = itemClick;
    }

    public abstract void covert(BaseRecyclerHolder holder, List<T> mList, int position);

    protected abstract int getLayoutId();

    protected abstract int getContentLength();
}
