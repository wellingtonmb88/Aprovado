package com.wellingtonmb88.aprovado.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.activity.CourseActivity;
import com.wellingtonmb88.aprovado.adapter.CourseRecyclerViewAdapter;
import com.wellingtonmb88.aprovado.async.SQliteAsyncTask;
import com.wellingtonmb88.aprovado.custom.FloatActionButtonHideShow;
import com.wellingtonmb88.aprovado.entity.Course;
import com.wellingtonmb88.aprovado.listener.SwipeDismissRecyclerViewTouchListener;
import com.wellingtonmb88.aprovado.utils.Constants;
import com.wellingtonmb88.aprovado.utils.CourseSemesterComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CourseListFragment extends Fragment implements SQliteAsyncTask.SQliteCallBack {

    private static final int WAIT_TIMEOUT = 5000;
    private static final int ANIMATION_DURATION = 500;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.floatingActionButton_add)
    FloatingActionButton mAddCourseFAB;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeDismissRecyclerViewTouchListener mTouchListener;
    private FloatActionButtonHideShow mFloatActionButtonHideShow;
    private Runnable mWorkRunnable;
    private Handler mWorkHnalder;
    private List<Course> mList;
    private List<Course> mDeletedCourseList;
    private List<Integer> mDeletedPositionList;

    private View.OnClickListener mSnackBarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!mDeletedCourseList.isEmpty()) {
                Integer deletedPosition = mDeletedPositionList.get(0);
                Course deletedCourse = mDeletedCourseList.get(0);
                if (deletedPosition == 0) {
                    mRecyclerView.scrollToPosition(deletedPosition);
                }
                if (mDeletedCourseList.size() == 1) {
                    if (mList.size() <= deletedPosition) {
                        mList.add(deletedCourse);
                    } else {
                        mList.add(deletedPosition, deletedCourse);
                    }
                    mAdapter.notifyItemInserted(deletedPosition);
                    mDeletedCourseList.remove(deletedCourse);
                    mDeletedPositionList.remove(deletedPosition);
                } else if (mDeletedCourseList.size() > 1) {
                    int index = 0;
                    for (Course course : mDeletedCourseList) {

                        if (mList.size() <= deletedPosition) {
                            mList.add(course);
                        } else {
                            mList.add(mDeletedPositionList.get(index), mDeletedCourseList.get(index));
                        }
                        mAdapter.notifyItemInserted(mDeletedPositionList.get(index));
                        index++;
                    }
                    mDeletedCourseList.clear();
                    mDeletedPositionList.clear();
                    mWorkHnalder.removeCallbacks(mWorkRunnable);
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_courses_list, container, false);

        ButterKnife.bind(this, v);

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        loadDataUI();
        setListener();


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllCourses();
    }

    private void loadDataUI() {

        mList = new ArrayList<>();
        mDeletedCourseList = new ArrayList<>();
        mDeletedPositionList = new ArrayList<>();
        getAllCourses();
        createTouchListener();
        createRunnable();

        mFloatActionButtonHideShow = new FloatActionButtonHideShow(mAddCourseFAB);
        mWorkHnalder = new Handler();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CourseRecyclerViewAdapter(getActivity().getApplicationContext(), mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnTouchListener(mTouchListener);
        mRecyclerView.addOnScrollListener(mTouchListener.makeScrollListener());

        // Add the sticky headers decoration
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration((StickyRecyclerHeadersAdapter) mAdapter);
        mRecyclerView.addItemDecoration(headersDecor);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });

    }

    @OnClick(R.id.floatingActionButton_add)
    public void addCourse() {
        final Intent intent = new Intent(getActivity().getApplicationContext(), CourseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.CourseExtra.BUNDLE_EXTRA, new Course());
        intent.putExtra(Constants.CourseExtra.INTENT_EXTRA, bundle);
        startActivity(intent);
    }

    private void setListener() {

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mRecyclerView,
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), CourseActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(Constants.CourseExtra.BUNDLE_EXTRA, mList.get(position));
                        intent.putExtra(Constants.CourseExtra.INTENT_EXTRA, bundle);
                        startActivityForResult(intent, 1);
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
        mTouchListener =
                new SwipeDismissRecyclerViewTouchListener(
                        mRecyclerView,
                        new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(RecyclerView recyclerView, final int[] reverseSortedPositions) {
                                int selectedPosition = 0;
                                mWorkHnalder.removeCallbacks(mWorkRunnable);

                                if (reverseSortedPositions.length > 0) {
                                    selectedPosition = reverseSortedPositions[0];
                                }

                                Course deletedCourse = mList.get(selectedPosition);
                                mDeletedPositionList.add(0, selectedPosition);
                                mDeletedCourseList.add(0, deletedCourse);

                                CoordinatorLayout coordinatorLayout = (CoordinatorLayout) recyclerView.getRootView()
                                        .findViewById(R.id.coordinatorlayout_course_list);

                                Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "", Snackbar.LENGTH_LONG);

                                if (mDeletedCourseList.size() > 1) {

                                    snackbar.setText(mDeletedCourseList.size() + " " + getString(R.string.courselist_snackbar_itens));
                                } else {
                                    snackbar.setText(deletedCourse.name + " " + getString(R.string.courselist_snackbar_title));
                                }
                                mList.remove(selectedPosition);
                                mAdapter.notifyDataSetChanged();
                                snackbar.setAction(getString(R.string.fragment_courses_list_undo), mSnackBarClickListener);
                                snackbar.show();
                                mWorkHnalder.postDelayed(mWorkRunnable, WAIT_TIMEOUT);
                            }
                        });


    }

    private void createRunnable() {
        mWorkRunnable = new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    mWorkHnalder.removeCallbacks(mWorkRunnable);
                    for (Course course : mDeletedCourseList) {
                        deleteCourse(course);
                    }
                }
            }
        };
    }

    @Override
    public void getAllCourses(List<Course> courseList) {
        mList.clear();
        mList.addAll(courseList);
        Collections.sort(mList, new CourseSemesterComparator());
        mAdapter.notifyDataSetChanged();
    }

    private void getAllCourses() {
        if (getActivity() != null) {
            SQliteAsyncTask task = new SQliteAsyncTask(getActivity().getApplicationContext(), this, null);
            task.execute(Constants.CourseDatabaseAction.GET_ALL_COURSES);
        }
    }

    private void deleteCourse(Course course) {
        if (getActivity() != null) {
            SQliteAsyncTask task = new SQliteAsyncTask(getActivity().getApplicationContext(), this, course);
            task.execute(Constants.CourseDatabaseAction.DELETE_COURSE);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

        private static final long DELAY_MILLIS = 100;

        private View mPressedView = null;
        private RecyclerView mRecyclerView;
        private boolean mIsPrepressed = false;
        private boolean mIsShowPress = false;
        private GestureDetector mGestureDetector;
        private OnItemClickListener mListener;

        public RecyclerItemClickListener(RecyclerView recyclerView, OnItemClickListener listener) {
            mListener = listener;
            mRecyclerView = recyclerView;
            setGestureDetector();
        }

        private void setGestureDetector() {
            mGestureDetector = new GestureDetector(mRecyclerView.getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    mIsPrepressed = true;
                    mPressedView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                    super.onDown(e);
                    return false;
                }

                @Override
                public void onShowPress(MotionEvent e) {
                    if (mIsPrepressed && mPressedView != null) {
                        mPressedView.setPressed(true);
                        mIsShowPress = true;
                    }
                    super.onShowPress(e);
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    if (mIsPrepressed && mPressedView != null) {
                        mPressedView.setPressed(true);
                        final View pressedView = mPressedView;
                        pressedView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pressedView.setPressed(false);
                            }
                        }, DELAY_MILLIS);
                        mIsPrepressed = false;
                        mPressedView = null;
                    }
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildPosition(childView));
            } else if (e.getActionMasked() == MotionEvent.ACTION_UP && mPressedView != null && mIsShowPress) {
                mPressedView.setPressed(false);
                mIsShowPress = false;
                mIsPrepressed = false;
                mPressedView = null;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}
