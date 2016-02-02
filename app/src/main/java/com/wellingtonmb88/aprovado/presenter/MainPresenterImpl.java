package com.wellingtonmb88.aprovado.presenter;

import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.entity.User;
import com.wellingtonmb88.aprovado.presenter.interfaces.MainPresenter;
import com.wellingtonmb88.aprovado.presenter.interfaces.MainView;

public class MainPresenterImpl implements MainPresenter {

    private MainView mView;

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void registerView(MainView mainView) {
        this.mView = mainView;
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
        }
    }
}
