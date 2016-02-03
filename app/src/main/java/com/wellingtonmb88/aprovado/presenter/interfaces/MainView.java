package com.wellingtonmb88.aprovado.presenter.interfaces;


import com.wellingtonmb88.aprovado.entity.User;

public interface MainView {
    void updateDrawerLayout(User user);
    void openLoginScreen();
    void openAboutScreen();
    void openFeedbackScreen();
}
