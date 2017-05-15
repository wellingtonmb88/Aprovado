package com.wellingtonmb88.aprovado.dagger.components;

import com.wellingtonmb88.aprovado.dagger.scopes.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(modules = PresenterModule.class, dependencies = {BaseComponent.class})
public interface FragmentInjectorComponent {

    void inject(CalculatorFragment fragment);

    void inject(CourseListFragment fragment);

    void inject(SignInDialogFragment fragment);
}
