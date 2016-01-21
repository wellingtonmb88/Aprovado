package com.wellingtonmb88.aprovado;

import android.app.Application;

import com.wellingtonmb88.aprovado.dagger.components.ApplicationComponent;
import com.wellingtonmb88.aprovado.dagger.components.BaseComponent;
import com.wellingtonmb88.aprovado.dagger.components.DaggerApplicationComponent;
import com.wellingtonmb88.aprovado.dagger.components.DaggerBaseComponent;
import com.wellingtonmb88.aprovado.dagger.modules.ApplicationModule;

public class AppApplication extends Application {

    private static BaseComponent sBaseComponent;

    public static BaseComponent getBaseComponent() {
        return sBaseComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationComponent applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        sBaseComponent = DaggerBaseComponent.builder()
                .applicationComponent(applicationComponent)
                .build();
    }

}
