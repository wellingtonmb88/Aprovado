package com.wellingtonmb88.aprovado.dagger.modules;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private static Application sApplication;

    public ApplicationModule(Application application) {
        sApplication = application;
    }

    @Singleton
    @Provides
    Application providesApplication() {
        return sApplication;
    }
}
