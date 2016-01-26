package com.wellingtonmb88.aprovado.presenter.interfaces;


import com.wellingtonmb88.aprovado.entity.Course;

import java.util.List;

public interface CourseListFragmentView {

    void openCourseDetails(int position);

    void undoCourseDeleted();

    void notifyItemInserted(int position);

    void notifyCourseDeleted(Course course);

    void setCourseList(List<Course> courseList);

    void showSnackBar(String deletedCourseName);

    void addCourse();
}
