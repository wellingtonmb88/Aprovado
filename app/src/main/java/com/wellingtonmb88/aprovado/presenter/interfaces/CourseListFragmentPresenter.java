package com.wellingtonmb88.aprovado.presenter.interfaces;

import com.wellingtonmb88.aprovado.database.DatabaseHelper;
import com.wellingtonmb88.aprovado.entity.Course;

public interface CourseListFragmentPresenter {

    void onAddCourse();

    void onOpenCourseDetails(int position);

    void onUndoCourseDeleted();

    void onShowSnackBar(String deletedCourseName);

    void onNotifyItemInserted(int position);

    void onNotifyCourseDeleted(Course course);

    void onSetCourseList();

    void onDestroy();

    void registerView(CourseListFragmentView mainView);

    void registerDatabaseHelper(DatabaseHelper databaseHelper);
}
