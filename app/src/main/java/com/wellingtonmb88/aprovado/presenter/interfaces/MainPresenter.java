package com.wellingtonmb88.aprovado.presenter.interfaces;


import com.wellingtonmb88.aprovado.database.DatabaseHelper;
import com.wellingtonmb88.aprovado.entity.Course;
import com.wellingtonmb88.aprovado.entity.User;
import com.wellingtonmb88.aprovado.presenter.view.MainView;

public interface MainPresenter {

    void onUpdateDrawerLayout(User user);

    void onOptionsItemSelected(int id);

    void onNavigationItemSelected(int id);

    void onCreate();

    void onResume();

    void onDestroy();

    void onGetDataFromDrive();

    void registerView(MainView mainView);

    void registerDatabaseHelper(DatabaseHelper<Course> databaseHelper);

    void onConnectToDrive();

    void onDisconnectFromDrive();

    void onHideNavMenuItem();
}
