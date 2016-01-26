package com.wellingtonmb88.aprovado.presenter;

import com.wellingtonmb88.aprovado.database.DatabaseHelper;
import com.wellingtonmb88.aprovado.entity.Course;
import com.wellingtonmb88.aprovado.presenter.interfaces.CourseListFragmentPresenter;
import com.wellingtonmb88.aprovado.presenter.interfaces.CourseListFragmentView;
import com.wellingtonmb88.aprovado.presenter.interfaces.MainView;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class CourseListFragmentPresenterImpl implements CourseListFragmentPresenter {

    private DatabaseHelper<Course> mDatabaseHelper;
    private CourseListFragmentView mView;

    private Subscription mSubscription;

    private Action1<List<Course>> getAllCoursesAction = new Action1<List<Course>>() {
        @Override
        public void call(List<Course> courseList) {
            mView.setCourseList(courseList);
        }
    };

    @Override
    public void registerView(CourseListFragmentView mainView) {
        this.mView = mainView;
    }

    @Override
    public void registerDatabaseHelper(DatabaseHelper databaseHelper) {
        this.mDatabaseHelper = databaseHelper;
    }

    @Override
    public void onAddCourse() {
        mView.addCourse();
    }

    @Override
    public void onOpenCourseDetails(int position) {
        mView.openCourseDetails(position);
    }

    @Override
    public void onUndoCourseDeleted() {
        mView.undoCourseDeleted();
    }

    @Override
    public void onShowSnackBar(String deletedCourseName) {
        mView.showSnackBar(deletedCourseName);
    }

    @Override
    public void onNotifyItemInserted(int position) {
        mView.notifyItemInserted(position);
    }

    @Override
    public void onNotifyCourseDeleted(Course course) {
        mView.notifyCourseDeleted(course);
    }

    @Override
    public void onSetCourseList() {
        mSubscription = mDatabaseHelper.getAll(Course.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getAllCoursesAction);
    }

    @Override
    public void onDestroy() {

        if (mSubscription != null) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
        getAllCoursesAction = null;
        mView = null;
    }
}
