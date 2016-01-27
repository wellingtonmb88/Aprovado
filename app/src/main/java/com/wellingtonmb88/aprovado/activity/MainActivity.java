package com.wellingtonmb88.aprovado.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.wellingtonmb88.aprovado.AppApplication;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.adapter.ViewPagerAdapter;
import com.wellingtonmb88.aprovado.dagger.components.DaggerActivityInjectorComponent;
import com.wellingtonmb88.aprovado.presenter.MainPresenterImpl;
import com.wellingtonmb88.aprovado.presenter.interfaces.MainView;
import com.wellingtonmb88.aprovado.utils.Constants;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements MainView {

    private static final int NUM_TABS = 2;

    @Bind(R.id.pager)
    ViewPager mPager;
    @Bind(R.id.tabs)
    TabLayout mTabs;
    @Bind(R.id.toolbar_layout)
    Toolbar mToolbarLayout;

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
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            mMainPresenter.onOptionsItemSelected(id);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setViewPageAdapter(CharSequence[] titles, int numTabs) {
        ViewPagerAdapter mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, numTabs);
        mPager.setAdapter(mAdapter);
        mTabs.setupWithViewPager(mPager);
    }

    @Override
    public void openAboutScreen() {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        intent.putExtra(Constants.TabSharedPreferences.SELECTED_TAB, mPager.getCurrentItem());
        startActivityForResult(intent, 1);
    }
}
