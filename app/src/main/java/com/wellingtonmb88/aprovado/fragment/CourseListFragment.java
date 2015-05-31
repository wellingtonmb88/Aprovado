package com.wellingtonmb88.aprovado.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.activity.CourseActivity;
import com.wellingtonmb88.aprovado.adapter.CourseRecyclerViewAdapter;
import com.wellingtonmb88.aprovado.async.SQliteAsyncTask;
import com.wellingtonmb88.aprovado.custom.FloatActionButtonHideShow;
import com.wellingtonmb88.aprovado.entity.Course;
import com.wellingtonmb88.aprovado.listener.SwipeDismissRecyclerViewTouchListener;
import com.wellingtonmb88.aprovado.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wellington on 25/05/2015.
 */
public class CourseListFragment extends Fragment implements SQliteAsyncTask.SQliteCallBack {

    private static final int WAIT_TIMEOUT = 5000;
    private static final int ANIMATION_DURATION = 500;

    private int selectedPosition = 0;
    private boolean isUndo = false;

    private LinearLayout mSnackBar;
    private TextView snakBarText;
    private TextView snakBarButton;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton mAddCourseFAB;

    private SwipeDismissRecyclerViewTouchListener mTouchListener;
    private FloatActionButtonHideShow mFloatActionButtonHideShow;
    private Runnable mWorkRunnable;
    private Course mLastCourseDeleted;
    private Handler mWorkHnalder;
    private List<Course> mList;
    private Animation mAnimation;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_courses_list, container, false);

        loadUI(v);
        loadDataUI();
        setListener();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllCourses();
    }

    private void loadUI(View v){

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mSnackBar = (LinearLayout) v.findViewById(R.id.snackbar);
        snakBarButton = (TextView) mSnackBar.findViewById(R.id.textView_snackbar_button);
        snakBarText = (TextView) mSnackBar.findViewById(R.id.textView_snackbar_text);
        mAddCourseFAB = (FloatingActionButton) v.findViewById(R.id.floatingActionButton_add);

        mSnackBar.setVisibility(View.GONE);
    }

    private void loadDataUI(){

        //mList = bulkInsert();
        mList = new ArrayList<>();
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

        mAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_out_bottom);
        mAnimation.setDuration(ANIMATION_DURATION);

    }

    private void setListener(){

        mAddCourseFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getActivity().getApplicationContext(), CourseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.CourseExtra.BUNDLE_EXTRA, new Course());
                intent.putExtra(Constants.CourseExtra.INTENT_EXTRA, bundle);
                startActivity(intent);
            }
        });

        snakBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isUndo) {
                    isUndo = true;
                    if (selectedPosition == 0) {
                        mRecyclerView.scrollToPosition(selectedPosition);
                    }
                    mList.add(selectedPosition, mLastCourseDeleted);
                    mAdapter.notifyItemInserted(selectedPosition);
                    mSnackBar.startAnimation(mAnimation);
                    mAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mSnackBar.setVisibility(View.GONE);
                            mWorkHnalder.removeCallbacks(mWorkRunnable);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                }
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mRecyclerView,
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), CourseActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(Constants.CourseExtra.BUNDLE_EXTRA, mList.get(position));
                        intent.putExtra(Constants.CourseExtra.INTENT_EXTRA, bundle);
                        startActivity(intent);
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

    private void createTouchListener(){
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
                                for (int position : reverseSortedPositions) {
                                    selectedPosition = position;
                                    break;
                                }
                                isUndo = false;
                                mLastCourseDeleted = (Course)mList.get(selectedPosition);
                                snakBarText.setText(mLastCourseDeleted.name +" "+ getString(R.string.courselist_snackbar_title));
                                mList.remove(selectedPosition);
                                mAdapter.notifyDataSetChanged();
                                mSnackBar.setVisibility(View.VISIBLE);
                                mWorkHnalder.postDelayed(mWorkRunnable, WAIT_TIMEOUT);
                            }
                        });


    }

    private void createRunnable(){
        mWorkRunnable = new Runnable() {
            @Override
            public void run() {
                if(!isUndo && getActivity() != null){
                    mSnackBar.startAnimation(mAnimation);
                    mAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mSnackBar.setVisibility(View.GONE);
                            mWorkHnalder.removeCallbacks(mWorkRunnable);
                            deleteCourse();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                }
            }
        };
    }

    @Override
    public void getAllCourses(List<Course> courseList) {
        mList.clear();
        mList.addAll(courseList);
        mAdapter.notifyDataSetChanged();
    }

    private void getAllCourses(){
        if(getActivity() != null){
            SQliteAsyncTask task = new SQliteAsyncTask(getActivity().getApplicationContext(), this, null);
            task.execute(Constants.CourseDatabaseAction.GET_ALL_COURSES);
        }
    }

    private void deleteCourse(){
        if(getActivity() != null){
            SQliteAsyncTask task = new SQliteAsyncTask(getActivity().getApplicationContext(), this, mLastCourseDeleted);
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

        private void setGestureDetector(){
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
    }



    private List<Course> bulkInsert() {
        List<Course> listCourses = new ArrayList<>();

        for (int i = 1; i < 20; i++) {
            Course course = new Course();
            course.name = "nome" + i;
            course.professor = "professor" + i;
            if (i < 11) {

                course.semester = i - 1;
            } else {

                course.semester = 6;
            }
            course.m1 = i;
            course.b1 = i + 5;
            course.m2 = i + 10;
            course.b2 = i + 15;
            course.mediaB1 = i +2;
            course.mediaB2 = i +3;
            course.mediaFinal= i +8;

            listCourses.add(course);
        }

        return listCourses;
    }
}
