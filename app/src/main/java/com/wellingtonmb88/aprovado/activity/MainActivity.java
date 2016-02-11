package com.wellingtonmb88.aprovado.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wellingtonmb88.aprovado.AppApplication;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.adapter.ViewPagerAdapter;
import com.wellingtonmb88.aprovado.dagger.components.DaggerActivityInjectorComponent;
import com.wellingtonmb88.aprovado.database.DatabaseHelper;
import com.wellingtonmb88.aprovado.entity.Course;
import com.wellingtonmb88.aprovado.entity.User;
import com.wellingtonmb88.aprovado.fragment.CalculatorFragment;
import com.wellingtonmb88.aprovado.fragment.CourseListFragment;
import com.wellingtonmb88.aprovado.fragment.SignInDialogFragment;
import com.wellingtonmb88.aprovado.preferences.DriveApiPreferences;
import com.wellingtonmb88.aprovado.preferences.UserPreferences;
import com.wellingtonmb88.aprovado.presenter.MainPresenterImpl;
import com.wellingtonmb88.aprovado.presenter.view.MainView;
import com.wellingtonmb88.aprovado.utils.CommonUtils;
import com.wellingtonmb88.aprovado.utils.Constants;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends BaseActivity implements MainView, NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_CODE_MAIN_ACTIVITY = 2;
    public static final int REQUEST_CODE_DRIVE_API = 3;
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
    @Inject
    DatabaseHelper<Course> mDatabaseHelper;
    private MenuItem mSyncDataNavMenuItem;
    private MenuItem mDriveSignInNavMenuItem;
    private CourseListFragment mCourseListFragment;
    private ProgressDialog progress;

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

        progress = new ProgressDialog(this);

        mMainPresenter.registerView(this);
        mMainPresenter.registerDatabaseHelper(mDatabaseHelper);
        mMainPresenter.onCreate();

        mCourseListFragment = CourseListFragment.getNewInstance();

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(mCourseListFragment);
        fragmentList.add(CalculatorFragment.getNewInstance());

        CharSequence titles[] = {getString(R.string.tablebar_header_classes), getString(R.string.tablebar_header_calculator)};
        setViewPageAdapter(fragmentList, titles, NUM_TABS);

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

        mSyncDataNavMenuItem = mNavigationView.getMenu().findItem(R.id.nav_sync_data);
        mDriveSignInNavMenuItem = mNavigationView.getMenu().findItem(R.id.nav_drive_sign_in);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMainPresenter.onResume();
        User user = UserPreferences.getUserFromPreferences();
        updateDrawerLayout(user);
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
        if (requestCode == REQUEST_CODE_MAIN_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                int selectedTab = data.getIntExtra(Constants.TabSharedPreferences.SELECTED_TAB, 0);
                mPager.setCurrentItem(selectedTab);
            }
        } else if (requestCode == REQUEST_CODE_DRIVE_API) {
            if (resultCode == RESULT_OK) {
                mSyncDataNavMenuItem.setVisible(true);
                mDriveSignInNavMenuItem.setVisible(false);
                mMainPresenter.onConnectToDrive();
            } else {
                mSyncDataNavMenuItem.setVisible(false);
                mDriveSignInNavMenuItem.setVisible(true);
                UserPreferences.setFirstFlow(false);
                DriveApiPreferences.setDriveApiConnected(false);
                DriveApiPreferences.setDriveApiConnectionDenied(true);
                mMainPresenter.onDisconnectFromDrive();
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

    private void setViewPageAdapter(List<Fragment> fragmentList, CharSequence[] titles, int numTabs) {
        ViewPagerAdapter mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList, titles, numTabs);
        mPager.setAdapter(mAdapter);
        mTabs.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new SimpleOnPageChangeListener(this));
    }

    @Override
    public void openAboutScreen() {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        intent.putExtra(Constants.TabSharedPreferences.SELECTED_TAB, mPager.getCurrentItem());
        startActivityForResult(intent, REQUEST_CODE_MAIN_ACTIVITY);
    }

    @Override
    public void openLoginScreen() {
        SignInDialogFragment newFragment = SignInDialogFragment.newInstance();
        newFragment.show(getSupportFragmentManager(), "dialog");
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
    public void courseListFromDriveApi(List<Course> courseList) {
        if (mCourseListFragment != null) {
            mCourseListFragment.setCourseList(courseList);
        }
    }

    @Override
    public void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.alert_dialog_error_drive_title)
                .setMessage(R.string.alert_dialog_error_drive_message).show();
    }

    @Override
    public void showProgress(String message) {
        if (!progress.isShowing()) {
            progress.setMessage(message);
            progress.show();
        }
    }

    @Override
    public void hideProgress() {
        if (progress != null) {
            progress.dismiss();
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        CommonUtils.hideSoftKeyBoard(this);
        mMainPresenter.onNavigationItemSelected(item.getItemId());
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onDriveApiDisconnectedChangeVisibilityOfNavMenuItem() {
        mSyncDataNavMenuItem.setVisible(false);
        mDriveSignInNavMenuItem.setVisible(false);
    }

    @Override
    public void onDriveApiConnectedChangeVisibilityOfNavMenuItem() {
        mSyncDataNavMenuItem.setVisible(true);
        mDriveSignInNavMenuItem.setVisible(false);
    }

    @Override
    public void updateDrawerLayout(User user) {
        String nameLabel = null;
        String emailLabel = null;
        Uri pictureUri = null;

        if (user != null) {
            if (user.getName() != null) {
                nameLabel = user.getName();
            }

            if (user.getEmail() != null) {
                emailLabel = user.getEmail();
            }

            if (user.getPictureUri() != null) {
                pictureUri = Uri.parse(user.getPictureUri());
            }
        }

        MenuItem loginItem = mNavigationView.getMenu().findItem(R.id.nav_login);

        View headerView = mNavigationView.getHeaderView(0);
        if (headerView != null) {
            TextView name = (TextView) headerView.findViewById(R.id.nav_header_textview_name);
            TextView email = (TextView) headerView.findViewById(R.id.nav_header_textview_email);
            CircleImageView image = (CircleImageView) headerView.findViewById(R.id.nav_header_profile_image);

            if (name != null) {

                if (TextUtils.isEmpty(nameLabel)) {
                    name.setText(R.string.app_name);
                    if (loginItem != null) {
                        loginItem.setTitle(R.string.nav_menu_item_sign_in_label);
                    }
                } else {
                    name.setText(nameLabel);
                    if (loginItem != null) {
                        loginItem.setTitle(R.string.nav_menu_item_sign_out_label);

                        boolean isDriveApiConnected = DriveApiPreferences.isDriveApiConnected();
                        if (!isDriveApiConnected) {
                            mSyncDataNavMenuItem.setVisible(false);
                            mDriveSignInNavMenuItem.setVisible(true);
                        } else {
                            mSyncDataNavMenuItem.setVisible(true);
                            mDriveSignInNavMenuItem.setVisible(false);
                        }

                        boolean isFirstFlow = UserPreferences.isFirstFlow();
                        if (isFirstFlow) {
                            mMainPresenter.onConnectToDrive();
                        }
                    }
                }
            }

            if (email != null) {

                if (TextUtils.isEmpty(emailLabel)) {
                    email.setVisibility(View.GONE);
                } else {
                    email.setVisibility(View.VISIBLE);
                    email.setText(emailLabel);
                }
            }

            if (image != null) {

                if (pictureUri == null) {
                    image.setImageResource(R.mipmap.approved_logo);
                } else {
                    Glide.with(this).fromUri().load(pictureUri).into(image);
                    image.setVisibility(View.VISIBLE);

                }
            }

        }

    }

    private static class SimpleOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        private final WeakReference<Activity> mActivity;

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

        private final WeakReference<Activity> mActivity;

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
