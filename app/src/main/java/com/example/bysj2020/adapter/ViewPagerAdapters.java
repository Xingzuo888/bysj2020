package com.example.bysj2020.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Author : wxz
 * Time   : 2020/02/19
 * Desc   :
 */
public class ViewPagerAdapters<T> extends FragmentStatePagerAdapter {
    private List<T> fragments;

    public ViewPagerAdapters(@NonNull FragmentManager fm) {
        super(fm);
    }

    public ViewPagerAdapters(@NonNull FragmentManager fm, List<T> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return (Fragment) fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
