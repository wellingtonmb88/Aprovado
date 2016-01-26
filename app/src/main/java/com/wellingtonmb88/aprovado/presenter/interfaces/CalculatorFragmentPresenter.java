package com.wellingtonmb88.aprovado.presenter.interfaces;

import android.content.Context;

public interface CalculatorFragmentPresenter {

    void calculateBimonthlyGrade1(Context context, String grade);

    void calculateBimonthlyGrade2(Context context, String grade);

    void calculateFinalAverage(Context context, String grade, String bimonthly);

    void onDestroy();

    void registerView(CalculatorFragmentView mainView);
}
