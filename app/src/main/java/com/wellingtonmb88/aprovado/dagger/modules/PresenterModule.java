package com.wellingtonmb88.aprovado.dagger.modules;

import com.wellingtonmb88.aprovado.dagger.scopes.ActivityScope;
import com.wellingtonmb88.aprovado.presenter.CalculatorFragmentPresenterImpl;
import com.wellingtonmb88.aprovado.presenter.CourseDetailsPresenterImpl;
import com.wellingtonmb88.aprovado.presenter.CourseListFragmentPresenterImpl;
import com.wellingtonmb88.aprovado.presenter.MainPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @Provides
    @ActivityScope
    MainPresenterImpl providesMainPresenter() {
        return new MainPresenterImpl();
    }

    @Provides
    @ActivityScope
    CourseDetailsPresenterImpl providesCourseDetailsPresenter() {
        return new CourseDetailsPresenterImpl();
    }

    @Provides
    @ActivityScope
    CalculatorFragmentPresenterImpl providesCalculatorFragmentPresenter() {
        return new CalculatorFragmentPresenterImpl();
    }

    @Provides
    @ActivityScope
    CourseListFragmentPresenterImpl providesCourseListFragmentPresenter() {
        return new CourseListFragmentPresenterImpl();
    }
}
