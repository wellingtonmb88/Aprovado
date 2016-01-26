package com.wellingtonmb88.aprovado.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.wellingtonmb88.aprovado.AppApplication;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.activity.CourseActivity;
import com.wellingtonmb88.aprovado.adapter.CourseRecyclerViewAdapter;
import com.wellingtonmb88.aprovado.custom.FloatActionButtonHideShow;
import com.wellingtonmb88.aprovado.dagger.components.DaggerFragmentInjectorComponent;
import com.wellingtonmb88.aprovado.database.DatabaseHelper;
import com.wellingtonmb88.aprovado.entity.Course;
import com.wellingtonmb88.aprovado.listener.RecyclerItemClickListener;
import com.wellingtonmb88.aprovado.listener.RecyclerViewSwipeDismissCallBacks;
import com.wellingtonmb88.aprovado.listener.SnackBarClickListener;
import com.wellingtonmb88.aprovado.listener.SwipeDismissRecyclerViewTouchListener;
import com.wellingtonmb88.aprovado.presenter.CourseListFragmentPresenterImpl;
import com.wellingtonmb88.aprovado.presenter.interfaces.CourseListFragmentPresenter;
import com.wellingtonmb88.aprovado.presenter.interfaces.CourseListFragmentView;
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
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeDismissRecyclerViewTouchListener mTouchListener;
    private FloatActionButtonHideShow mFloatActionButtonHideShow;
    private List<Course> mList;
    private List<Course> mDeletedCourseList;
    private List<Integer> mDeletedPositionList;
    private View.OnClickListener mSnackBarClickListener;

    private Handler mWorkHandler;

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

        loadDataUI();
        setListener();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCourseListFragmentPresenter.onSetCourseList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mDatabaseHelper = null;
        mSnackBarClickListener = null;
        mTouchListener = null;
        mCourseListFragmentPresenter.onDestroy();

        if (mWorkHandler != null) {
            mWorkHandler.removeCallbacks(null);
            mWorkHandler = null;
        }
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

        mWorkHandler = new Handler();
        mList = new ArrayList<>();
        mDeletedCourseList = new ArrayList<>();
        mDeletedPositionList = new ArrayList<>();
        createTouchListener();

        mFloatActionButtonHideShow = new FloatActionButtonHideShow(mAddCourseFAB);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CourseRecyclerViewAdapter(getActivity().getApplicationContext(), mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnTouchListener(mTouchListener);
        mRecyclerView.addOnScrollListener(mTouchListener.makeScrollListener());

        mSnackBarClickListener = new SnackBarClickListener(mCourseListFragmentPresenter, mRecyclerView,
                mList, mDeletedCourseList, mDeletedPositionList);

        // Add the sticky headers decoration
        final StickyRecyclerHeadersDecoration headersDecor =
                new StickyRecyclerHeadersDecoration((StickyRecyclerHeadersAdapter) mAdapter);
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
        startActivity(intent);
    }

    private void setListener() {

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mRecyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        mCourseListFragmentPresenter.onOpenCourseDetails(position);
                    }
                }));

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

    private void createTouchListener() {

        RecyclerViewSwipeDismissCallBacks recyclerViewSwipeDismissCallBacks =
                new RecyclerViewSwipeDismissCallBacks(mCourseListFragmentPresenter, mList,
                        mDeletedCourseList, mDeletedPositionList);
        recyclerViewSwipeDismissCallBacks.setHandler(mWorkHandler);
        mTouchListener = new SwipeDismissRecyclerViewTouchListener(mRecyclerView, recyclerViewSwipeDismissCallBacks);
    }

    @Override
    public void openCourseDetails(int position) {
        Intent intent = new Intent(getActivity().getApplicationContext(), CourseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.CourseExtra.BUNDLE_EXTRA, mList.get(position).getId());
        intent.putExtra(Constants.CourseExtra.INTENT_EXTRA, bundle);
        startActivityForResult(intent, 1);
    }

    @Override
    public void undoCourseDeleted() {
        mDeletedCourseList.clear();
        mDeletedPositionList.clear();
        mWorkHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void notifyItemInserted(int position) {
        mAdapter.notifyItemInserted(position);
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
    public void showSnackBar(String deletedCourseName) {

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) mRecyclerView.getRootView()
                .findViewById(R.id.coordinatorlayout_course_list);

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "", Snackbar.LENGTH_LONG);

        if (mDeletedCourseList.size() > 1) {
            snackbar.setText(mDeletedCourseList.size() + " " + getString(R.string.courselist_snackbar_itens));
        } else {
            snackbar.setText(deletedCourseName + " " + getString(R.string.courselist_snackbar_title));
        }

        mAdapter.notifyDataSetChanged();
        snackbar.setAction(getString(R.string.fragment_courses_list_undo), mSnackBarClickListener);
        snackbar.show();
    }

    @Override
    public void onRefresh() {
        mCourseListFragmentPresenter.onSetCourseList();
    }
}
