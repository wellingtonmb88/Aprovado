package com.wellingtonmb88.aprovado.dagger.components;

import com.wellingtonmb88.aprovado.activity.MainActivity;
import com.wellingtonmb88.aprovado.dagger.scopes.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(dependencies = {BaseComponent.class})
public interface ActivityInjectorComponent {

    void inject(MainActivity activity);
}
