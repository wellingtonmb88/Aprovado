package com.wellingtonmb88.aprovado.presenter.interfaces;

import android.content.Context;

import com.wellingtonmb88.aprovado.database.DatabaseHelper;
import com.wellingtonmb88.aprovado.entity.Course;
import com.wellingtonmb88.aprovado.presenter.view.CourseListFragmentView;

import java.util.List;

public interface CourseListFragmentPresenter {

    void onAddCourse();

    void onOpenCourseDetails(int position);

    void onSnackBarClicked();

    void onDismissRecyclerViewItem(Context context, int selectedPosition);

    void onSetCourseList();

    void onPause();

    void onDestroy();

    void registerView(CourseListFragmentView mainView);

    void registerDatabaseHelper(DatabaseHelper<Course> databaseHelper);

    void registerList(List<Course> list);
}
