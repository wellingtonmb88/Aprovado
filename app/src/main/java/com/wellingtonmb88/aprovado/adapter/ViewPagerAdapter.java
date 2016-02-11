package com.wellingtonmb88.aprovado.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final CharSequence mTitles[];
    private final int mNumbOfTabs;
    private final List<Fragment> mFragmentList;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, CharSequence titles[], int numbOfTabs) {
        super(fm);
        this.mTitles = titles;
        this.mNumbOfTabs = numbOfTabs;
        this.mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public int getCount() {
        return mNumbOfTabs;
    }
}