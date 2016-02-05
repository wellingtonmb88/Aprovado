package com.wellingtonmb88.aprovado.presenter.interfaces;


import com.wellingtonmb88.aprovado.entity.User;
import com.wellingtonmb88.aprovado.presenter.view.MainView;

public interface MainPresenter {

    void onUpdateDrawerLayout(User user);

    void onOptionsItemSelected(int id);

    void onNavigationItemSelected(int id);

    void onDestroy();

    void registerView(MainView mainView);
}
