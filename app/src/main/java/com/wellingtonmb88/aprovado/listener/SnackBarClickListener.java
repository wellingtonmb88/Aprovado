package com.wellingtonmb88.aprovado.listener;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import com.wellingtonmb88.aprovado.entity.Course;
import com.wellingtonmb88.aprovado.presenter.interfaces.CourseListFragmentPresenter;

import java.util.List;

public class SnackBarClickListener implements OnClickListener {

    private CourseListFragmentPresenter mCourseListFragmentPresenter;
    private RecyclerView mRecyclerView;
    private List<Course> mList;
    private List<Course> mDeletedCourseList;
    private List<Integer> mDeletedPositionList;

    public SnackBarClickListener(CourseListFragmentPresenter courseListFragmentPresenter,
                                 RecyclerView recyclerView, List<Course> courseList,
                                 List<Course> deletedCourseList, List<Integer> deletedPositionList) {
        mCourseListFragmentPresenter = courseListFragmentPresenter;
        mRecyclerView = recyclerView;
        mList = courseList;
        mDeletedCourseList = deletedCourseList;
        mDeletedPositionList = deletedPositionList;
    }

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
                mCourseListFragmentPresenter.onNotifyItemInserted(deletedPosition);
            } else if (mDeletedCourseList.size() > 1) {
                int index = 0;
                for (Course course : mDeletedCourseList) {

                    if (mList.size() <= deletedPosition) {
                        mList.add(course);
                    } else {
                        mList.add(mDeletedPositionList.get(index), mDeletedCourseList.get(index));
                    }
                    mCourseListFragmentPresenter.onNotifyItemInserted(mDeletedPositionList.get(index));
                    index++;
                }
            }

            mCourseListFragmentPresenter.onUndoCourseDeleted();
        }
    }
}
