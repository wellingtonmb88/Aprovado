package com.wellingtonmb88.aprovado.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wellingtonmb88.aprovado.fragment.CalculatorFragment;
import com.wellingtonmb88.aprovado.fragment.CourseListFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private CharSequence mTitles[];
    private int mNumbOfTabs;

    public ViewPagerAdapter(FragmentManager fm, CharSequence titles[], int numbOfTabs) {
        super(fm);
        this.mTitles = titles;
        this.mNumbOfTabs = numbOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new CourseListFragment();
        } else {
            return new CalculatorFragment();
        }
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