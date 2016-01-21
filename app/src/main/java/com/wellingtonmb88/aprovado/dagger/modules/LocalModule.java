package com.wellingtonmb88.aprovado.dagger.modules;

import android.app.Application;

import com.wellingtonmb88.aprovado.database.realm.RealmManager;
import com.wellingtonmb88.aprovado.database.realm.model.Course;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.RealmObject;

@Module
public class LocalModule {

    @Provides
    @Singleton
    RealmManager<Course> providesRealmManager(Application application) {
        return new RealmManager<>(application);
    }
}
