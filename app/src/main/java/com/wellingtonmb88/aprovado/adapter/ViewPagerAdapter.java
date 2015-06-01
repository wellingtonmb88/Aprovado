package com.wellingtonmb88.aprovado.adapter;

/**
 * Created by Wellington on 25/05/2015.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wellingtonmb88.aprovado.fragment.CalculatorFragment;
import com.wellingtonmb88.aprovado.fragment.CourseListFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private CharSequence mTitles[];
    private int mNumbOfTabs;

    public ViewPagerAdapter(Context context, FragmentManager fm, CharSequence titles[], int numbOfTabs) {
        super(fm);
        this.mTitles = titles;
        this.mNumbOfTabs = numbOfTabs;
     }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new CalculatorFragment();
        }else{
            return  new CourseListFragment();
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