package com.wellingtonmb88.aprovado.dagger.components;

import android.app.Application;

import com.wellingtonmb88.aprovado.dagger.modules.ApplicationModule;
import com.wellingtonmb88.aprovado.dagger.modules.LocalModule;
import com.wellingtonmb88.aprovado.database.realm.RealmManager;
import com.wellingtonmb88.aprovado.database.realm.model.Course;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {ApplicationModule.class, LocalModule.class})
public interface ApplicationComponent {
    Application getApplication();

    RealmManager<Course> getRealmManager();
}