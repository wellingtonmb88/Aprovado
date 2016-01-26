package com.wellingtonmb88.aprovado.presenter.interfaces;


public interface MainPresenter {

    void onOptionsItemSelected(int id);

    void onDestroy();
    void registerView(MainView mainView);
}
