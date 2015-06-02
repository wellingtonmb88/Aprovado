package com.wellingtonmb88.aprovado.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.adapter.ViewPagerAdapter;
import com.wellingtonmb88.aprovado.slidingtab.SlidingTabLayout;
import com.wellingtonmb88.aprovado.utils.Constants;


public class MainActivity extends AppCompatActivity {
   
    private int mNumbofmTabs = 2;
    private ViewPager mPager;
    private ViewPagerAdapter mAdapter;
    private SlidingTabLayout mTabs;
    private Toolbar mToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        loadUI();
        loadDataUI();
        setListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                int selectedTab = data.getIntExtra(Constants.TabSharedPreferences.SELECTED_TAB,0);
                mPager.setCurrentItem(selectedTab);
            }
        }
    }

    private void loadUI(){
        LinearLayout toolbar = (LinearLayout) findViewById(R.id.toolbar);
        mToolbarLayout = (Toolbar) toolbar.findViewById(R.id.toolbar_layout);
        mTabs = (SlidingTabLayout) toolbar.findViewById(R.id.tabs);
        mPager = (ViewPager) findViewById(R.id.pager);
    }

    private void loadDataUI(){
        CharSequence mTitles[]={getString(R.string.tablebar_header_calculator), getString(R.string.tablebar_header_classes)};
        mAdapter =  new ViewPagerAdapter(getApplicationContext(), getSupportFragmentManager(), mTitles, mNumbofmTabs);
        mPager.setAdapter(mAdapter);
        mTabs.setDistributeEvenly(true);
        mTabs.setViewPager(mPager);
        mToolbarLayout.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolbarLayout);
        if(getSupportActionBar() != null){
            getSupportActionBar().setLogo(R.mipmap.actionbar_approved_logo);
        }
    }

    private void setListener(){
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(android.R.color.white);
            }
        });
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
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            intent.putExtra(Constants.TabSharedPreferences.SELECTED_TAB, mPager.getCurrentItem());
            startActivityForResult(intent, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
