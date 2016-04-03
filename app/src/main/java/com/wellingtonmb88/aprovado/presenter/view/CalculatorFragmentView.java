package com.wellingtonmb88.aprovado.presenter.view;


import android.support.v4.app.Fragment;

import com.wellingtonmb88.aprovado.fragment.CalculatorFragment;

public interface CalculatorFragmentView {

    void setTextBimonthlyGrade1(String message);

    void setTextBimonthlyGrade2(String message);

    void setTextFinalAverage(String message);

    void cleanFields();

    CalculatorFragment getFragment();
}
