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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.activity.CourseActivity;
import com.wellingtonmb88.aprovado.adapter.CourseRecyclerViewAdapter;
import com.wellingtonmb88.aprovado.custom.FloatActionButtonHideShow;
import com.wellingtonmb88.aprovado.entity.Course;
import com.wellingtonmb88.aprovado.listener.SwipeDismissRecyclerViewTouchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wellington on 25/05/2015.
 */
public class CourseListFragment extends Fragment implements CourseRecyclerViewAdapter.RecyclerViewCallBack {
    private FloatingActionButton mAddCourseFAB;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Course> mList;
    private RelativeLayout mRecyclerViewLayout;
    private LinearLayout mSnackBar;
    private TextView snakBarText;
    private TextView snakBarButton;
    private Course mLastCourseDeleted;
    private boolean isUndo;


    int selectedPosition = 0;

    private static final int WAIT_TIMEOUT = 5000;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.courses_list, container, false);

        mRecyclerViewLayout = (RelativeLayout) v.findViewById(R.id.recycler_view_layout);
        mSnackBar = (LinearLayout) v.findViewById(R.id.snackbar);
        mAddCourseFAB = (FloatingActionButton) v.findViewById(R.id.floatingActionButton_add);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mList = bulkInsert();

        // specify an adapter (see also next example)
        mAdapter = new CourseRecyclerViewAdapter(getActivity().getApplicationContext(), this, mList);
        mRecyclerView.setAdapter(mAdapter);

        final FloatActionButtonHideShow fb = new FloatActionButtonHideShow(mAddCourseFAB);

        mAddCourseFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity().getApplicationContext(), CourseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("disiclina", new Course());
                intent.putExtra("disciplinaExtra", bundle);

                startActivity(intent);
            }
        });

        snakBarButton = (TextView) mSnackBar.findViewById(R.id.textView_snackbar_button);
        snakBarText = (TextView) mSnackBar.findViewById(R.id.textView_snackbar_text);

        mSnackBar.setVisibility(View.GONE);

        isUndo = false;
        snakBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isUndo){
                    isUndo = true;
                    if(selectedPosition == 0){
                        mRecyclerView.scrollToPosition(selectedPosition);
                    }
                    mList.add(selectedPosition, mLastCourseDeleted);
                    mAdapter.notifyItemInserted(selectedPosition);
                    final Animation anim = AnimationUtils.loadAnimation(getActivity(),  R.anim.abc_slide_out_bottom);
                    anim.setDuration(500);
                    mSnackBar.startAnimation(anim);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mSnackBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });
                }
            }
        });


        SwipeDismissRecyclerViewTouchListener touchListener =
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
                                snakBarText.setText(mLastCourseDeleted.name +" item removido.");
                                mList.remove(selectedPosition);
                                // do not call notifyItemRemoved for every item, it will cause gaps on deleting items
                                mAdapter.notifyDataSetChanged();
                                mSnackBar.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                    if(!isUndo){
                                        final Animation anim = AnimationUtils.loadAnimation(getActivity(),  R.anim.abc_slide_out_bottom);
                                        mSnackBar.startAnimation(anim);
                                        mSnackBar.setVisibility(View.GONE);
                                    }
                                    }
                                }, WAIT_TIMEOUT);
                            }
                        });
        mRecyclerView.setOnTouchListener(touchListener);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Callback method to be invoked when the RecyclerView has been scrolled. This will be
             * called after the scroll has completed.
             *
             * @param recyclerView The RecyclerView which scrolled.
             * @param dx The amount of horizontal scroll.
             * @param dy The amount of vertical scroll.
             */
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    fb.hide();
                } else if (dy < 1) {
                    fb.show();
                }
            }
        });

        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        mRecyclerView.addOnScrollListener(touchListener.makeScrollListener());
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mRecyclerView,
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Intent intent = new Intent(getActivity().getApplicationContext(), CourseActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("disiclina", mList.get(position));
                        intent.putExtra("disciplinaExtra", bundle);
                        startActivity(intent);
                        //Toast.makeText(getActivity().getApplicationContext(), "Clicked " + mList.get(position).name, Toast.LENGTH_SHORT).show();
                    }
                }));


        return v;
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

            listCourses.add(course);
        }

        return listCourses;
    }

    @Override
    public void Operation(String operation, Course course) {

        Intent intent = new Intent(getActivity().getApplicationContext(), CourseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("disiclina", course);
        intent.putExtra("disciplinaExtra", bundle);

        startActivity(intent);
    }



    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        private static final long DELAY_MILLIS = 100;

        private RecyclerView mRecyclerView;
        private GestureDetector mGestureDetector;
        private boolean mIsPrepressed = false;
        private boolean mIsShowPress = false;
        private View mPressedView = null;

        public RecyclerItemClickListener(RecyclerView recyclerView, OnItemClickListener listener) {
            mListener = listener;
            mRecyclerView = recyclerView;
            mGestureDetector = new GestureDetector(recyclerView.getContext(), new GestureDetector.SimpleOnGestureListener() {
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
}
