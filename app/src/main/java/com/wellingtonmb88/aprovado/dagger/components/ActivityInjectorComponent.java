package com.wellingtonmb88.aprovado.dagger.components;

import com.wellingtonmb88.aprovado.activity.CourseActivity;
import com.wellingtonmb88.aprovado.activity.MainActivity;
import com.wellingtonmb88.aprovado.dagger.modules.PresenterModule;
import com.wellingtonmb88.aprovado.dagger.scopes.ActivityScope;
import com.wellingtonmb88.aprovado.presenter.MainPresenterImpl;
import com.wellingtonmb88.aprovado.presenter.interfaces.MainPresenter;
import com.wellingtonmb88.aprovado.presenter.interfaces.MainView;

import dagger.Component;

@ActivityScope
@Component(dependencies = {BaseComponent.class}, modules = PresenterModule.class)
public interface ActivityInjectorComponent {

    void inject(MainActivity activity);

    void inject(CourseActivity activity);
}
