package com.wellingtonmb88.aprovado.presenter.view;


import android.app.Activity;

import com.wellingtonmb88.aprovado.entity.Course;
import com.wellingtonmb88.aprovado.entity.User;

import java.util.List;

public interface MainView {
    void updateDrawerLayout(User user);

    void openLoginScreen();

    void openAboutScreen();

    void openFeedbackScreen();

    void courseListFromDriveApi(List<Course> courseList);

    void showAlertDialog();

    void showProgress(String message);

    void hideProgress();

    void onDriveApiDisconnectedChangeVisibilityOfNavMenuItem();

    void onDriveApiConnectedChangeVisibilityOfNavMenuItem();

    Activity getActivity();
}
