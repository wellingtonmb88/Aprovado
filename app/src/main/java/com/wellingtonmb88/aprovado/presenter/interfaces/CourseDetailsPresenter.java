package com.wellingtonmb88.aprovado.presenter.interfaces;


import com.wellingtonmb88.aprovado.database.DatabaseHelper;
import com.wellingtonmb88.aprovado.presenter.view.CourseDetailsView;

public interface CourseDetailsPresenter {

    void onSaveCourse();

    void onGetCourse(String courseId);

    void onDestroy();

    void registerView(CourseDetailsView mainView);

    void registerDatabaseHelper(DatabaseHelper databaseHelper);
}
