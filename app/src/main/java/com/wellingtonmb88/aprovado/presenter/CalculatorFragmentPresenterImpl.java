package com.wellingtonmb88.aprovado.presenter;

import android.content.Context;

import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.presenter.interfaces.CalculatorFragmentPresenter;
import com.wellingtonmb88.aprovado.presenter.view.CalculatorFragmentView;
import com.wellingtonmb88.aprovado.utils.AprovadoLogger;
import com.wellingtonmb88.aprovado.utils.CommonUtils;

import java.text.ParseException;

public class CalculatorFragmentPresenterImpl implements CalculatorFragmentPresenter {

    private CalculatorFragmentView mView;

    @Override
    public void registerView(CalculatorFragmentView mainView) {
        this.mView = mainView;
    }

    @Override
    public void calculateBimonthlyGrade1(Context context, String grade) {
        mView.setTextBimonthlyGrade1(calculateBimonthlyGrade(context, grade));
    }

    @Override
    public void calculateBimonthlyGrade2(Context context, String grade) {
        mView.setTextBimonthlyGrade2(calculateBimonthlyGrade(context, grade));
    }

    private String calculateBimonthlyGrade(Context context, String grade) {
        float monthGrade = 0;
        try {
            monthGrade = CommonUtils.parseFloatLocaleSensitive(grade);
        } catch (ParseException e) {
            AprovadoLogger.e("Error to parse String to Float: " + e.getLocalizedMessage());
        }

        double bimonthlyGrade = CommonUtils.roundFloatOneHouse((5 - (monthGrade * 0.4)) / 0.6);
        double result = (monthGrade * 0.4) + (bimonthlyGrade * 0.6);

        if (result < 5) {
            bimonthlyGrade = Math.abs(bimonthlyGrade + 0.37);
        }

        return context.getString(R.string.calculator_dialog_to_approve) +
                " " + String.valueOf(CommonUtils.roundFloatOneHouse(bimonthlyGrade));
    }

    @Override
    public void calculateFinalAverage(Context context, String grade, String bimonthly) {
        double mb1 = 0;
        double monthGrade = 0;

        try {
            mb1 = CommonUtils.parseFloatLocaleSensitive(bimonthly);
            monthGrade = CommonUtils.parseFloatLocaleSensitive(grade);
        } catch (ParseException e) {
            AprovadoLogger.e("Error to parse String to Float: " + e.getLocalizedMessage());
        }

        double mb2 = CommonUtils.roundFloatOneHouse((((((mb1 * 2) - 25) / 5) * 5) / 3) * -1);
        double bimonthlyGrade = (mb2 - (monthGrade * 0.4)) / 0.6;
        double result = ((mb1 * 2) + (mb2 * 3)) / 5;

        if (bimonthlyGrade <= 0) {
            bimonthlyGrade = 0;
        } else if (bimonthlyGrade > 0 && bimonthlyGrade < 1) {
            bimonthlyGrade = 1;
        } else if (result < 5) {
            bimonthlyGrade = Math.abs(bimonthlyGrade + 0.36);
        }

        if (bimonthlyGrade > 10) {
            mView.setTextFinalAverage(context.getString(R.string.calculator_dialog_unpassed));
        } else {
            String dialog = context.getString(R.string.calculator_dialog_to_approve_final) +
                    " " + String.valueOf(CommonUtils.roundFloatOneHouse(bimonthlyGrade));
            mView.setTextFinalAverage(dialog);
        }

    }

    @Override
    public void onCleanFields() {
        mView.cleanFields();
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

}
