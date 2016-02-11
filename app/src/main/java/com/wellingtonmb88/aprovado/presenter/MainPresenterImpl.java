package com.wellingtonmb88.aprovado.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wellingtonmb88.aprovado.AppApplication;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.database.DatabaseHelper;
import com.wellingtonmb88.aprovado.entity.Course;
import com.wellingtonmb88.aprovado.entity.CourseJSON;
import com.wellingtonmb88.aprovado.entity.User;
import com.wellingtonmb88.aprovado.manager.DriveApiManager;
import com.wellingtonmb88.aprovado.preferences.DriveApiPreferences;
import com.wellingtonmb88.aprovado.preferences.UserPreferences;
import com.wellingtonmb88.aprovado.presenter.interfaces.MainPresenter;
import com.wellingtonmb88.aprovado.presenter.view.MainView;
import com.wellingtonmb88.aprovado.utils.AprovadoLogger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainPresenterImpl implements MainPresenter,
        DriveApiManager.GetDataResultCallback, DriveApiManager.DriveConnectionCallback {

    private DatabaseHelper<Course> mDatabaseHelper;
    private MainView mView;
    private Subscription mSubscription;

    private Action1<List<Course>> getAllCoursesAction = new Action1<List<Course>>() {
        @Override
        public void call(List<Course> courseList) {

            List<CourseJSON> lisCourseJSON = new ArrayList<>();

            for (Course course : courseList) {
                CourseJSON courseJSON = new CourseJSON(course.getId(), course.getName(), course.getProfessor(),
                        course.getSemester(), course.getM1(), course.getB1(), course.getMediaB1(), course.getM2()
                        , course.getB2(), course.getMediaB2(), course.getMediaFinal());

                lisCourseJSON.add(courseJSON);
            }

            DriveApiManager driveApiManager = DriveApiManager.getInstance();

            if (!lisCourseJSON.isEmpty()) {
                Gson gson = new Gson();

                Type listType = new TypeToken<List<CourseJSON>>() {
                }.getType();
                String json = gson.toJson(lisCourseJSON, listType);
                driveApiManager.saveDataToDrive(json);
            } else {
                driveApiManager.deleteAllDataAndCreateNew(false);
            }
        }
    };

    private Action1<List<Course>> getAllCoursesFromDriveAction = new Action1<List<Course>>() {
        @Override
        public void call(List<Course> courseList) {
            mView.hideProgress();
            mView.courseListFromDriveApi(courseList);
        }
    };

    @Override
    public void onCreate() {
        DriveApiManager driveApiManager = DriveApiManager.getInstance();
        driveApiManager.setConnectionCallback(this);
        driveApiManager.setDataResultCallback(this);
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onDestroy() {
        mView = null;
        DriveApiManager.getInstance().disConnectGoogleDriveApi();

        if (mSubscription != null) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }

    @Override
    public void registerView(MainView mainView) {
        this.mView = mainView;
        DriveApiManager.getInstance().setActivity(mainView.getActivity());
    }

    @Override
    public void registerDatabaseHelper(DatabaseHelper<Course> databaseHelper) {
        mDatabaseHelper = databaseHelper;
    }

    @Override
    public void onConnectToDrive() {
        DriveApiManager.getInstance().connectGoogleDriveApi();
    }

    @Override
    public void onDisconnectFromDrive() {
        DriveApiManager.getInstance().disConnectGoogleDriveApi();
    }

    @Override
    public void onHideNavMenuItem() {
        mView.onDriveApiDisconnectedChangeVisibilityOfNavMenuItem();
    }

    @Override
    public void onUpdateDrawerLayout(User user) {
        mView.updateDrawerLayout(user);
    }

    @Override
    public void onOptionsItemSelected(int id) {
        if (id == R.id.action_settings) {
            mView.openAboutScreen();
        }
    }

    @Override
    public void onNavigationItemSelected(int id) {
        if (id == R.id.nav_login) {
            mView.openLoginScreen();
        } else if (id == R.id.nav_feedback) {
            mView.openFeedbackScreen();
        } else if (id == R.id.nav_about) {
            mView.openAboutScreen();
        } else if (id == R.id.nav_sync_data) {
            saveFileToDrive();
        } else if (id == R.id.nav_drive_sign_in) {
            DriveApiManager.getInstance().connectGoogleDriveApi();
        }
    }

    private void saveFileToDrive() {
        mView.showProgress(AppApplication.getAppContext().getString(R.string.progress_message_syncing_data));
        mSubscription = mDatabaseHelper.getAll(Course.class)
                .observeOn(Schedulers.computation())
                .subscribe(getAllCoursesAction);
    }

    @Override
    public void onGetDataFromDrive() {
        mView.showProgress(AppApplication.getAppContext().getString(R.string.progress_message_downloading_data));
        DriveApiManager.getInstance().getDataFromDrive();
    }

    @Override
    public void onResultCallback(String result) {
        AprovadoLogger.i("onResultCallback : description: " + result);

        if (result != null) {
            Gson gson = new Gson();

            Type listType = new TypeToken<List<CourseJSON>>() {
            }.getType();

            List<CourseJSON> lisCourseJSON = gson.fromJson(result, listType);

            List<Course> lisCourse = new ArrayList<>();

            for (CourseJSON courseJSON : lisCourseJSON) {
                Course course = new Course(courseJSON.getId(), courseJSON.getName(), courseJSON.getProfessor(),
                        courseJSON.getSemester(), courseJSON.getM1(), courseJSON.getB1(), courseJSON.getMediaB1(), courseJSON.getM2()
                        , courseJSON.getB2(), courseJSON.getMediaB2(), courseJSON.getMediaFinal());

                lisCourse.add(course);
            }

            if (!lisCourse.isEmpty() && mDatabaseHelper != null) {
                mDatabaseHelper.createOrUpdateBatch(lisCourse, Course.class)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getAllCoursesFromDriveAction);
            }
        }
    }

    @Override
    public void onDataSyncFinished() {
        mView.hideProgress();
    }

    @Override
    public void onDriveApiConnected() {
        mView.onDriveApiConnectedChangeVisibilityOfNavMenuItem();

        boolean isFirstUserFlow = UserPreferences.isFirstFlow();
        boolean isConnectionDenied = DriveApiPreferences.isDriveApiConnectionDenied();

        DriveApiPreferences.setDriveApiConnected(true);
        DriveApiPreferences.setDriveApiConnectionDenied(false);

        if (isFirstUserFlow || isConnectionDenied) {
            UserPreferences.setFirstFlow(false);
            this.onGetDataFromDrive();
        }
    }

    @Override
    public void onDriveApiConnectionFailed() {
        mView.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mView.hideProgress();
                mView.showAlertDialog();
            }
        });
    }
}
