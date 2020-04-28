package com.example.bysj2020.view.flow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.bysj2020.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : wxz
 * Time   : 2020/04/27
 * Desc   : 文本适配器
 */
public class TextAdapter<T> extends BaseAdapter implements OnInitSelectedPosition {

    private final Context mContext;
    private final List<T> list;

    public TextAdapter(Context mContext) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_text, null);
        View line=view.findViewById(R.id.item_home_line);
        AppCompatTextView content = view.findViewById(R.id.item_home_content);
        T t = list.get(position);
        if (t instanceof String) {
            if (position == 0) {
                line.setVisibility(View.GONE);
            }
            content.setText((String) t);
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
