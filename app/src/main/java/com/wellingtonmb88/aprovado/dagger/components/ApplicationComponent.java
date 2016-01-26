package com.wellingtonmb88.aprovado.dagger.components;

import android.app.Application;

import com.wellingtonmb88.aprovado.dagger.modules.ApplicationModule;
import com.wellingtonmb88.aprovado.dagger.modules.LocalModule;
import com.wellingtonmb88.aprovado.dagger.modules.PresenterModule;
import com.wellingtonmb88.aprovado.database.DatabaseHelper;
import com.wellingtonmb88.aprovado.entity.Course;
import com.wellingtonmb88.aprovado.presenter.MainPresenterImpl;
import com.wellingtonmb88.aprovado.presenter.interfaces.MainPresenter;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {ApplicationModule.class, LocalModule.class})
public interface ApplicationComponent {
    Application getApplication();

    DatabaseHelper<Course> getDatabaseHelper();
}