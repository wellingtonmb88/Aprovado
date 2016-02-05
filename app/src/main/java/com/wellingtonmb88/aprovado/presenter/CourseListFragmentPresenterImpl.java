package com.wellingtonmb88.aprovado.presenter;

import android.content.Context;
import android.os.Handler;

import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.database.DatabaseHelper;
import com.wellingtonmb88.aprovado.entity.Course;
import com.wellingtonmb88.aprovado.presenter.interfaces.CourseListFragmentPresenter;
import com.wellingtonmb88.aprovado.presenter.view.CourseListFragmentView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class CourseListFragmentPresenterImpl implements CourseListFragmentPresenter {

    private static final int HANDLER_TIMEOUT = 5000;
    private DatabaseHelper<Course> mDatabaseHelper;
    private CourseListFragmentView mView;
    private Subscription mSubscription;
    private List<Course> mList;
    private final List<Course> mDeletedCourseList;
    private final List<Integer> mDeletedPositionList;
    private final Handler mWorkHandler;
    private Runnable mWorkRunnable = new Runnable() {
        @Override
        public void run() {
            executeDeletedCourseList();
        }
    };

    private Action1<List<Course>> getAllCoursesAction = new Action1<List<Course>>() {
        @Override
        public void call(List<Course> courseList) {
            mView.setCourseList(courseList);
        }
    };

    public CourseListFragmentPresenterImpl() {
        mWorkHandler = new Handler();
        mDeletedCourseList = new ArrayList<>();
        mDeletedPositionList = new ArrayList<>();
    }

    @Override
    public void registerView(CourseListFragmentView mainView) {
        this.mView = mainView;
    }

    @Override
    public void registerDatabaseHelper(DatabaseHelper<Course> databaseHelper) {
        this.mDatabaseHelper = databaseHelper;
    }

    @Override
    public void registerList(List<Course> list) {
        mList = list;
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
    public void onSnackBarClicked() {
        snackBarClicked();
    }

    @Override
    public void onSetCourseList() {
        mSubscription = mDatabaseHelper.getAll(Course.class)
                .delay(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getAllCoursesAction);
    }

    @Override
    public void onPause() {
        executeDeletedCourseList();
    }

    @Override
    public void onDestroy() {

        if (mSubscription != null) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
        getAllCoursesAction = null;
        mView = null;
        if (mWorkHandler != null) {
            mWorkHandler.removeCallbacksAndMessages(null);
        }
        mWorkRunnable = null;
    }

    @Override
    public void onDismissRecyclerViewItem(Context context, int selectedPosition) {

        if (mWorkHandler != null) {
            mWorkHandler.removeCallbacks(mWorkRunnable);

            Course deletedCourse = mList.get(selectedPosition);
            mDeletedPositionList.add(0, selectedPosition);
            mDeletedCourseList.add(0, deletedCourse);
            mList.remove(deletedCourse);
            mWorkHandler.postDelayed(mWorkRunnable, HANDLER_TIMEOUT);
            String snackBarText;
            if (mDeletedCourseList.size() > 1) {
                snackBarText = mDeletedCourseList.size() + " " + context.getString(R.string.courselist_snackbar_itens);
            } else {
                snackBarText = deletedCourse.getName() + " " + context.getString(R.string.courselist_snackbar_title);
            }
            mView.showSnackBar(snackBarText);
        }
    }

    private void snackBarClicked() {
        if (!mDeletedCourseList.isEmpty()) {
            Integer deletedPosition = mDeletedPositionList.get(0);
            Course deletedCourse = mDeletedCourseList.get(0);
            if (mDeletedCourseList.size() == 1) {
                if (mList.size() <= deletedPosition) {
                    mList.add(deletedCourse);
                } else {
                    mList.add(deletedPosition, deletedCourse);
                }
                mView.notifyItemInserted(deletedPosition);
            } else if (mDeletedCourseList.size() > 1) {
                int index = 0;
                for (Course course : mDeletedCourseList) {

                    if (mList.size() <= deletedPosition) {
                        mList.add(course);
                    } else {
                        mList.add(mDeletedPositionList.get(index), mDeletedCourseList.get(index));
                    }
                    mView.notifyItemInserted(mDeletedPositionList.get(index));
                    index++;
                }
            }
            mDeletedCourseList.clear();
            mDeletedPositionList.clear();
            mWorkHandler.removeCallbacksAndMessages(null);
        }
    }

    private void executeDeletedCourseList() {
        if (mWorkHandler != null && !mDeletedCourseList.isEmpty()) {
            mWorkHandler.removeCallbacks(mWorkRunnable);
            for (Course course : mDeletedCourseList) {
                mView.notifyCourseDeleted(course);
            }
            mDeletedCourseList.clear();
            mDeletedPositionList.clear();
        }
    }
}
