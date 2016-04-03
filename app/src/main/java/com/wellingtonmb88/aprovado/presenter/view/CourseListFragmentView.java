package com.wellingtonmb88.aprovado.presenter.view;


import android.support.v4.app.Fragment;

import com.wellingtonmb88.aprovado.entity.Course;
import com.wellingtonmb88.aprovado.fragment.CourseListFragment;

import java.util.List;

public interface CourseListFragmentView {

    void openCourseDetails(int position);

    void notifyItemInserted(int position);

    void notifyCourseDeleted(Course course);

    void setCourseList(List<Course> courseList);

    void showSnackBar(String deletedCourseName);

    void addCourse();

    CourseListFragment getFragment();
}
