package com.wellingtonmb88.aprovado.dagger.components;

import com.wellingtonmb88.aprovado.dagger.modules.PresenterModule;
import com.wellingtonmb88.aprovado.dagger.scopes.ActivityScope;
import com.wellingtonmb88.aprovado.fragment.CalculatorFragment;
import com.wellingtonmb88.aprovado.fragment.CourseListFragment;
import com.wellingtonmb88.aprovado.presenter.interfaces.CalculatorFragmentPresenter;
import com.wellingtonmb88.aprovado.presenter.interfaces.CalculatorFragmentView;

import dagger.Component;

@ActivityScope
@Component(modules = PresenterModule.class, dependencies = {BaseComponent.class})
public interface FragmentInjectorComponent {

    void inject(CalculatorFragment fragment);

    void inject(CourseListFragment fragment);
}
