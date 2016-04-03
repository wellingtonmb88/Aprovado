package com.wellingtonmb88.aprovado;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.wellingtonmb88.aprovado.dagger.components.ApplicationComponent;
import com.wellingtonmb88.aprovado.dagger.components.BaseComponent;
import com.wellingtonmb88.aprovado.dagger.components.DaggerApplicationComponent;
import com.wellingtonmb88.aprovado.dagger.components.DaggerBaseComponent;
import com.wellingtonmb88.aprovado.dagger.modules.ApplicationModule;

import io.fabric.sdk.android.Fabric;

public class AppApplication extends Application {

    private static Context sContext;
    private static BaseComponent sBaseComponent;

    public static BaseComponent getBaseComponent() {
        return sBaseComponent;
    }

    public static Context getAppContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }

        sContext = getApplicationContext();

        ApplicationComponent applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        sBaseComponent = DaggerBaseComponent.builder()
                .applicationComponent(applicationComponent)
                .build();
    }
}
