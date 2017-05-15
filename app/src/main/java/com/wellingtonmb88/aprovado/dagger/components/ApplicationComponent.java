package com.wellingtonmb88.aprovado.dagger.components;

import android.app.Application;

import com.wellingtonmb88.aprovado.database.DatabaseHelper;
import com.wellingtonmb88.aprovado.entity.Course;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, LocalModule.class})
public interface ApplicationComponent {

    Application getApplication();

    DatabaseHelper<Course> getDatabaseHelper();
}