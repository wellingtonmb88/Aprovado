package com.wellingtonmb88.aprovado.listener;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;

import com.wellingtonmb88.aprovado.entity.Course;
import com.wellingtonmb88.aprovado.presenter.interfaces.CourseListFragmentPresenter;

import java.util.List;

public class RecyclerViewSwipeDismissCallBacks implements SwipeDismissRecyclerViewTouchListener.DismissCallbacks {
    private static final int WAIT_TIMEOUT = 5000;

    private CourseListFragmentPresenter mCourseListFragmentPresenter;
    private List<Course> mList;
    private List<Course> mDeletedCourseList;
    private List<Integer> mDeletedPositionList;
    private Handler mWorkHandler;
    private Runnable mWorkRunnable = new Runnable() {
        @Override
        public void run() {
            if (mWorkHandler != null) {
                mWorkHandler.removeCallbacks(mWorkRunnable);
                for (Course course : mDeletedCourseList) {
                    mCourseListFragmentPresenter.onNotifyCourseDeleted(course);
                }
                mDeletedCourseList.clear();
                mDeletedPositionList.clear();
            }
        }
    };

    public RecyclerViewSwipeDismissCallBacks(CourseListFragmentPresenter courseListFragmentPresenter, List<Course> courseList,
                                             List<Course> deletedCourseList, List<Integer> deletedPositionList) {
        mCourseListFragmentPresenter = courseListFragmentPresenter;
        mList = courseList;
        mDeletedCourseList = deletedCourseList;
        mDeletedPositionList = deletedPositionList;
    }

    @Override
    public boolean canDismiss(int position) {
        return true;
    }

    @Override
    public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {
        int selectedPosition = 0;

        if (mWorkHandler != null) {
            mWorkHandler.removeCallbacks(mWorkRunnable);

            if (reverseSortedPositions.length > 0) {
                selectedPosition = reverseSortedPositions[0];
            }

            Course deletedCourse = mList.get(selectedPosition);
            mDeletedPositionList.add(0, selectedPosition);
            mDeletedCourseList.add(0, deletedCourse);
            mList.remove(selectedPosition);
            mWorkHandler.postDelayed(mWorkRunnable, WAIT_TIMEOUT);
            mCourseListFragmentPresenter.onShowSnackBar(deletedCourse.getName());
        }
    }

    public void setHandler(Handler handler) {
        this.mWorkHandler = handler;
    }
}
