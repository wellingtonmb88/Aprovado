package com.wellingtonmb88.aprovado.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.wellingtonmb88.aprovado.AppApplication;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.adapter.ViewPagerAdapter;
import com.wellingtonmb88.aprovado.dagger.components.DaggerActivityInjectorComponent;
import com.wellingtonmb88.aprovado.presenter.MainPresenterImpl;
import com.wellingtonmb88.aprovado.presenter.interfaces.MainView;
import com.wellingtonmb88.aprovado.utils.CommonUtils;
import com.wellingtonmb88.aprovado.utils.Constants;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements MainView, NavigationView.OnNavigationItemSelectedListener {

    private static final int NUM_TABS = 2;

    @Bind(R.id.pager)
    ViewPager mPager;
    @Bind(R.id.tabs)
    TabLayout mTabs;
    @Bind(R.id.toolbar_layout)
    Toolbar mToolbarLayout;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;

    @Inject
    MainPresenterImpl mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        DaggerActivityInjectorComponent.builder()
                .baseComponent(AppApplication.getBaseComponent())
                .build()
                .inject(this);

        mMainPresenter.registerView(this);


        CharSequence titles[] = {getString(R.string.tablebar_header_classes), getString(R.string.tablebar_header_calculator)};
        setViewPageAdapter(titles, NUM_TABS);

        mToolbarLayout.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(mToolbarLayout);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setLogo(R.mipmap.actionbar_approved_logo);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbarLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        mDrawerLayout.setDrawerListener(new SimpleDrawerListener(this));

        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        mMainPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                int selectedTab = data.getIntExtra(Constants.TabSharedPreferences.SELECTED_TAB, 0);
                mPager.setCurrentItem(selectedTab);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        mMainPresenter.onOptionsItemSelected(item.getItemId());

        return super.onOptionsItemSelected(item);
    }

    private void setViewPageAdapter(CharSequence[] titles, int numTabs) {
        ViewPagerAdapter mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, numTabs);
        mPager.setAdapter(mAdapter);
        mTabs.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new SimpleOnPageChangeListener(this));
    }

    @Override
    public void openAboutScreen() {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        intent.putExtra(Constants.TabSharedPreferences.SELECTED_TAB, mPager.getCurrentItem());
        startActivityForResult(intent, 1);
    }

    @Override
    public void openFeedbackScreen() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String uriString =
                "market://details?id=com.wellingtonmb88.aprovado";
        intent.setData(Uri.parse(uriString));
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        CommonUtils.hideSoftKeyBoard(this);
        mMainPresenter.onNavigationItemSelected(item.getItemId());
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private static class SimpleOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        private WeakReference<Activity> mActivity;

        public SimpleOnPageChangeListener(Activity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            Activity activity = mActivity.get();
            if (activity != null) {
                CommonUtils.hideSoftKeyBoard(activity);
            }
        }
    }

    private static class SimpleDrawerListener extends DrawerLayout.SimpleDrawerListener {
        
        private WeakReference<Activity> mActivity;

        public SimpleDrawerListener(Activity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            Activity activity = mActivity.get();
            if (activity != null) {
                CommonUtils.hideSoftKeyBoard(activity);
            }
        }
    }
}
