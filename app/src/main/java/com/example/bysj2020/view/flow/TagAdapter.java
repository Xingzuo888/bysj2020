package com.example.bysj2020.view.flow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bysj2020.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : wxz
 * Time   : 2020/03/26
 * Desc   : 标签适配器
 */
public class TagAdapter<T> extends BaseAdapter implements OnInitSelectedPosition {

    private final Context mContext;
    private final List<T> list;

    public TagAdapter(Context mContext) {
        this.mContext = mContext;
        this.list = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //加载自己的view
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_search_float, null);
        TextView textView = view.findViewById(R.id.search_float_item_content);
        T t = list.get(position);
        if (t instanceof String) {
            textView.setText((String) t);
        }
        return view;
    }

    /**
     * 添加数据
     *
     * @param datas
     */
    public void onlyAddAll(List<T> datas) {
        list.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 清除全部数据并添加数据
     *
     * @param datas
     */
    public void clearAndAddAll(List<T> datas) {
        list.clear();
        onlyAddAll(datas);
    }

    @Override
    public boolean isSelectedPosition(int position) {
        if (position % 2 == 0) {
            return true;
        }
        return false;
    }
}
