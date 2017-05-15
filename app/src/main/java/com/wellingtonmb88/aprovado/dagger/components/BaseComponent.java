package com.wellingtonmb88.aprovado.dagger.components;

import com.wellingtonmb88.aprovado.dagger.scopes.ApplicationScope;
import com.wellingtonmb88.aprovado.database.DatabaseHelper;
import com.wellingtonmb88.aprovado.entity.Course;

import dagger.Component;

@Component(dependencies = ApplicationComponent.class)
@ApplicationScope
public interface BaseComponent {

    DatabaseHelper<Course> getDatabaseHelper();
}