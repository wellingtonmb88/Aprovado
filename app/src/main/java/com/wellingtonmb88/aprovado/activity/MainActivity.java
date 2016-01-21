package com.wellingtonmb88.aprovado.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.wellingtonmb88.aprovado.AppApplication;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.adapter.ViewPagerAdapter;
import com.wellingtonmb88.aprovado.dagger.components.DaggerActivityInjectorComponent;
import com.wellingtonmb88.aprovado.database.realm.RealmManager;
import com.wellingtonmb88.aprovado.database.realm.model.Course;
import com.wellingtonmb88.aprovado.slidingtab.SlidingTabLayout;
import com.wellingtonmb88.aprovado.utils.Constants;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {


    private static final int NUM_TABS = 2;

    @Bind(R.id.pager)
    ViewPager mPager;
    @Bind(R.id.tabs)
    SlidingTabLayout mTabs;
    @Bind(R.id.toolbar_layout)
    Toolbar mToolbarLayout;

    @Inject
    RealmManager<Course> mRealManager;

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

        Course course1 = new Course();
        course1.setName("NAME_1");
        course1.setId(UUID.randomUUID().toString());
        Course course2 = new Course();
        course2.setName("NAME_12");
        course2.setId(UUID.randomUUID().toString());
        Course course3 = new Course();
        course3.setName("NAME_13");
        course3.setId(UUID.randomUUID().toString());

        mRealManager.insert(course1);
        mRealManager.insert(course2);
        mRealManager.insert(course3);


        loadDataUI();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Course> all = mRealManager.getAll(Course.class);
        String id = null;
        for (Course object : all) {
            Log.d(" Course : ", (object).getName());
            id = object.getId();
        }

        Course entityAsync = mRealManager.getEntityAsync(Course.class, id);
        Log.d(" Course entityAsync : ", entityAsync.getName());
    }

    @Override
    protected void onDestroy() {
        mRealManager.deleteAll(Course.class);
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

    private void loadDataUI() {
        CharSequence mTitles[] = {getString(R.string.tablebar_header_calculator), getString(R.string.tablebar_header_classes)};
        ViewPagerAdapter mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mTitles, NUM_TABS);
        mPager.setAdapter(mAdapter);
        mTabs.setDistributeEvenly(true);
        mTabs.setViewPager(mPager);
        mToolbarLayout.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolbarLayout);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setLogo(R.mipmap.actionbar_approved_logo);
        }
    }

    private void setListener() {
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(MainActivity.this, android.R.color.white);
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
