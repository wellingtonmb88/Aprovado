package com.wellingtonmb88.aprovado.presenter;

import com.wellingtonmb88.aprovado.database.DatabaseHelper;
import com.wellingtonmb88.aprovado.entity.Course;
import com.wellingtonmb88.aprovado.presenter.interfaces.CourseDetailsPresenter;
import com.wellingtonmb88.aprovado.presenter.interfaces.CourseDetailsView;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class CourseDetailsPresenterImpl implements CourseDetailsPresenter {

    private CourseDetailsView mView;
    private DatabaseHelper<Course> mDatabaseHelper;
    private Subscription mSubscription;

    private Action1<Course> getCourseAction = new Action1<Course>() {
        @Override
        public void call(Course course) {
            mView.getCourse(course);
        }
    };

    @Override
    public void registerView(CourseDetailsView mainView) {
        this.mView = mainView;
    }

    @Override
    public void registerDatabaseHelper(DatabaseHelper databaseHelper) {
        this.mDatabaseHelper = databaseHelper;
    }

    @Override
    public void onDestroy() {
        mView = null;

        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mSubscription = null;
        getCourseAction = null;
    }

    @Override
    public void onSaveCourse() {
        mView.saveCourse();
    }

    @Override
    public void onGetCourse(String courseId) {
        mSubscription = mDatabaseHelper.getById(Course.class, courseId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getCourseAction);
    }
}
