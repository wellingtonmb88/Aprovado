package com.wellingtonmb88.aprovado;

import android.app.Application;
import android.content.Context;

import com.wellingtonmb88.aprovado.dagger.components.ApplicationComponent;
import com.wellingtonmb88.aprovado.dagger.components.BaseComponent;
import com.wellingtonmb88.aprovado.dagger.components.DaggerApplicationComponent;
import com.wellingtonmb88.aprovado.dagger.components.DaggerBaseComponent;
import com.wellingtonmb88.aprovado.dagger.modules.ApplicationModule;

public class AppApplication extends Application {

    private static Context sContext;
    private static BaseComponent sBaseComponent;

    public static BaseComponent getBaseComponent() {
        return sBaseComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();

        ApplicationComponent applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        sBaseComponent = DaggerBaseComponent.builder()
                .applicationComponent(applicationComponent)
                .build();
    }

    public static Context getAppContext(){
        return sContext;
    }
}
