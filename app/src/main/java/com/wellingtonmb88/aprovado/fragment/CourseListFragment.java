package com.wellingtonmb88.aprovado.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.wellingtonmb88.aprovado.AppApplication;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.activity.CourseActivity;
import com.wellingtonmb88.aprovado.adapter.CourseRecyclerViewAdapter;
import com.wellingtonmb88.aprovado.custom.FloatActionButtonHideShow;
import com.wellingtonmb88.aprovado.dagger.components.DaggerFragmentInjectorComponent;
import com.wellingtonmb88.aprovado.database.DatabaseHelper;
import com.wellingtonmb88.aprovado.entity.Course;
import com.wellingtonmb88.aprovado.listener.SimpleItemTouchHelperCallback;
import com.wellingtonmb88.aprovado.presenter.CourseListFragmentPresenterImpl;
import com.wellingtonmb88.aprovado.presenter.view.CourseListFragmentView;
import com.wellingtonmb88.aprovado.utils.Constants;
import com.wellingtonmb88.aprovado.utils.CourseSemesterComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CourseListFragment extends Fragment implements CourseListFragmentView, SwipeRefreshLayout.OnRefreshListener {

    public final static int REQUEST_CODE_FRAGMENT = 3;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.floatingActionButton_add)
    FloatingActionButton mAddCourseFAB;
    @Inject
    DatabaseHelper<Course> mDatabaseHelper;
    @Inject
    CourseListFragmentPresenterImpl mCourseListFragmentPresenter;
    private CourseRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatActionButtonHideShow mFloatActionButtonHideShow;
    private List<Course> mList;

    public CourseListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_courses_list, container, false);

        ButterKnife.bind(this, v);

        DaggerFragmentInjectorComponent.builder().baseComponent(AppApplication.getBaseComponent())
                .build()
                .inject(this);

        mCourseListFragmentPresenter.registerView(this);
        mCourseListFragmentPresenter.registerDatabaseHelper(mDatabaseHelper);

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mSwipeRefreshLayout.setColorSchemeResources(R.color.ColorPrimary);
        loadDataUI();
        setListener();

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        mCourseListFragmentPresenter.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mDatabaseHelper = null;
        mCourseListFragmentPresenter.onDestroy();
    }

    private void loadDataUI() {

        mSwipeRefreshLayout.setOnRefreshListener(this);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         mSwipeRefreshLayout.setRefreshing(true);

                                         mCourseListFragmentPresenter.onSetCourseList();
                                     }
                                 }
        );

        mList = new ArrayList<>();
        mCourseListFragmentPresenter.registerList(mList);

        mFloatActionButtonHideShow = new FloatActionButtonHideShow(mAddCourseFAB);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CourseRecyclerViewAdapter(mCourseListFragmentPresenter, getActivity().getApplicationContext(), mList);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);

        // Add the sticky headers decoration
        final StickyRecyclerHeadersDecoration headersDecor =
                new StickyRecyclerHeadersDecoration(mAdapter);
        mRecyclerView.addItemDecoration(headersDecor);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });

    }

    @OnClick(R.id.floatingActionButton_add)
    public void addCourseButton() {
        mCourseListFragmentPresenter.onAddCourse();
    }

    @Override
    public void addCourse() {
        final Intent intent = new Intent(getActivity().getApplicationContext(), CourseActivity.class);
        startActivityForResult(intent, REQUEST_CODE_FRAGMENT);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FRAGMENT) {
            if (resultCode == Activity.RESULT_OK) {
                mCourseListFragmentPresenter.onSetCourseList();
            }
        }
    }

    private void setListener() {

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    mFloatActionButtonHideShow.hide();
                } else if (dy < 1) {
                    mFloatActionButtonHideShow.show();
                }
            }
        });
    }

    @Override
    public void openCourseDetails(int position) {
        Intent intent = new Intent(getActivity().getApplicationContext(), CourseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.CourseExtra.BUNDLE_EXTRA, mList.get(position).getId());
        intent.putExtra(Constants.CourseExtra.INTENT_EXTRA, bundle);
        startActivityForResult(intent, REQUEST_CODE_FRAGMENT);
    }

    @Override
    public void notifyItemInserted(int deletedPosition) {
        if (deletedPosition == 0) {
            mRecyclerView.scrollToPosition(deletedPosition);
        }
        mAdapter.notifyItemInserted(deletedPosition);
    }

    @Override
    public void notifyCourseDeleted(Course course) {
        mDatabaseHelper.delete(Course.class, course.getId());
    }

    @Override
    public void setCourseList(List<Course> courseList) {
        mList.clear();
        mList.addAll(courseList);
        Collections.sort(mList, new CourseSemesterComparator());
        mAdapter.notifyDataSetChanged();
        // stopping swipe refresh
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showSnackBar(String snackBarText) {

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) mRecyclerView.getRootView()
                .findViewById(R.id.coordinatorlayout_course_list);

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, snackBarText, Snackbar.LENGTH_LONG);

        mAdapter.notifyDataSetChanged();
        snackbar.setAction(getString(R.string.fragment_courses_list_undo), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCourseListFragmentPresenter.onSnackBarClicked();
            }
        });
        snackbar.show();
    }

    @Override
    public void onRefresh() {
        mCourseListFragmentPresenter.onSetCourseList();
    }
}
