package com.wellingtonmb88.aprovado.dagger.modules;

import android.app.Application;

import com.wellingtonmb88.aprovado.database.DatabaseHelper;
import com.wellingtonmb88.aprovado.database.realm.RealmDataSource;
import com.wellingtonmb88.aprovado.entity.Course;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LocalModule {

    @Provides
    @Singleton
    RealmDataSource<Course> providesDataSource(Application application) {
        return new RealmDataSource<>(application);
    }

    @Provides
    @Singleton
    DatabaseHelper<Course> providesDatabaseHelper(RealmDataSource<Course> realmDataSource) {
        return new DatabaseHelper<>(realmDataSource);
    }
}
