package com.wellingtonmb88.aprovado.dagger.components;


import com.wellingtonmb88.aprovado.dagger.scopes.ApplicationScope;
import com.wellingtonmb88.aprovado.database.realm.RealmManager;
import com.wellingtonmb88.aprovado.database.realm.model.Course;

import dagger.Component;
import io.realm.RealmObject;

@Component(dependencies = ApplicationComponent.class)
@ApplicationScope
public interface BaseComponent {

    RealmManager<Course> getRealmManager();
}