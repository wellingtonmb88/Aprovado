package com.wellingtonmb88.aprovado.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wellingtonmb88.aprovado.AppApplication;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.dagger.components.DaggerFragmentInjectorComponent;
import com.wellingtonmb88.aprovado.listener.TextChangeListener;
import com.wellingtonmb88.aprovado.presenter.CalculatorFragmentPresenterImpl;
import com.wellingtonmb88.aprovado.presenter.interfaces.CalculatorFragmentView;
import com.wellingtonmb88.aprovado.utils.CommonUtils;

import java.text.ParseException;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CalculatorFragment extends Fragment implements CalculatorFragmentView {

    @Bind(R.id.editText_m1)
    EditText mEditTextCourseM1;
    @Bind(R.id.editText_m2)
    EditText mEditTextCourseM2;
    @Bind(R.id.editText_b1)
    EditText mEditTextCourseB1;
    @Bind(R.id.editText_b2)
    EditText mEditTextCourseB2;
    @Bind(R.id.editText_mb1)
    EditText mEditTextCourseMB1;
    @Bind(R.id.editText_mb2)
    EditText mEditTextCourseMB2;
    @Bind(R.id.editText_mf)
    EditText mEditTextCourseMF;
    @Bind(R.id.textview_simulate_b1)
    TextView mSimulateB1;
    @Bind(R.id.textview_simulate_b2)
    TextView mSimulateB2;
    @Bind(R.id.textview_simulate_mf)
    TextView mSimulateMF;

    @Inject
    CalculatorFragmentPresenterImpl mCalculatorFragmentPresenter;

    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (!TextUtils.isEmpty(mEditTextCourseB1.getText()) || !TextUtils.isEmpty(mEditTextCourseM1.getText())) {
                float m1 = 0;
                float b1 = 0;
                try {
                    m1 = CommonUtils.parseFloatLocaleSensitive(mEditTextCourseM1.getText().toString());
                    b1 = CommonUtils.parseFloatLocaleSensitive(mEditTextCourseB1.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (TextUtils.isEmpty(mEditTextCourseB1.getText()) && !TextUtils.isEmpty(mEditTextCourseM1.getText())) {
                    mCalculatorFragmentPresenter.calculateBimonthlyGrade1(getContext(), mEditTextCourseM1.getText().toString());
                } else if (!TextUtils.isEmpty(mEditTextCourseB1.getText()) && !TextUtils.isEmpty(mEditTextCourseM1.getText())) {
                    float media = CommonUtils.roundFloatTwoHouse(((m1 * 4) + (b1 * 6)) / 10);
                    mEditTextCourseMB1.setError(null);
                    mEditTextCourseMB1.setText(String.valueOf(media));
                }
            }

            if (!TextUtils.isEmpty(mEditTextCourseB2.getText()) || !TextUtils.isEmpty(mEditTextCourseM2.getText())) {
                float m2 = 0;
                float b2 = 0;
                try {
                    m2 = CommonUtils.parseFloatLocaleSensitive(mEditTextCourseM2.getText().toString());
                    b2 = CommonUtils.parseFloatLocaleSensitive(mEditTextCourseB2.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (TextUtils.isEmpty(mEditTextCourseB2.getText()) && !TextUtils.isEmpty(mEditTextCourseM2.getText())) {
                    mCalculatorFragmentPresenter.calculateBimonthlyGrade2(getContext(), mEditTextCourseM2.getText().toString());
                } else if (!TextUtils.isEmpty(mEditTextCourseB2.getText()) && !TextUtils.isEmpty(mEditTextCourseM2.getText())) {
                    float media = CommonUtils.roundFloatTwoHouse(((m2 * 4) + (b2 * 6)) / 10);
                    mEditTextCourseMB2.setText(String.valueOf(media));
                }
            }

            if (!TextUtils.isEmpty(mEditTextCourseMB1.getText()) && !TextUtils.isEmpty(mEditTextCourseM2.getText())) {
                mCalculatorFragmentPresenter.calculateFinalAverage(getContext(), mEditTextCourseM2.getText().toString(), mEditTextCourseMB1.getText().toString());
            }

            if (!TextUtils.isEmpty(mEditTextCourseMB1.getText()) && !TextUtils.isEmpty(mEditTextCourseMB2.getText())) {
                float mb1 = 0;
                float mb2 = 0;
                try {
                    mb1 = CommonUtils.parseFloatLocaleSensitive(mEditTextCourseMB1.getText().toString());
                    mb2 = CommonUtils.parseFloatLocaleSensitive(mEditTextCourseMB2.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                float media = CommonUtils.roundFloatTwoHouse(((mb1 * 2) + (mb2 * 3)) / 5);
                mEditTextCourseMF.setText(String.valueOf(media));
            }

            if (TextUtils.isEmpty(mEditTextCourseM1.getText()) || TextUtils.isEmpty(mEditTextCourseM2.getText())) {
                mSimulateMF.setText(getString(R.string.calculator_dialog_to_approve_final));
                if (TextUtils.isEmpty(mEditTextCourseM1.getText())) {
                    mSimulateB1.setText(getString(R.string.calculator_dialog_to_approve));
                }
                if (TextUtils.isEmpty(mEditTextCourseM2.getText())) {
                    mSimulateB2.setText(getString(R.string.calculator_dialog_to_approve));
                }
            }

            if (TextUtils.isEmpty(mEditTextCourseM1.getText()) || TextUtils.isEmpty(mEditTextCourseB1.getText())) {
                mEditTextCourseMB1.setText("");
            }

            if (TextUtils.isEmpty(mEditTextCourseM2.getText()) || TextUtils.isEmpty(mEditTextCourseB2.getText())) {
                mEditTextCourseMB2.setText("");
            }

            if (TextUtils.isEmpty(mEditTextCourseMB1.getText()) || TextUtils.isEmpty(mEditTextCourseMB2.getText())) {
                mEditTextCourseMF.setText("");
            }

            if (TextUtils.isEmpty(mEditTextCourseM1.getText())) {
                mEditTextCourseMB1.setText("");
            }

            if (TextUtils.isEmpty(mEditTextCourseM2.getText())) {
                mEditTextCourseMB2.setText("");
            }
        }
    };

    public CalculatorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        ButterKnife.bind(this, view);

        DaggerFragmentInjectorComponent.builder().baseComponent(AppApplication.getBaseComponent())
                .build()
                .inject(this);

        mCalculatorFragmentPresenter.registerView(this);
        loadDataUI();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mCalculatorFragmentPresenter.onDestroy();
    }

    private void loadDataUI() {

        mEditTextCourseM1.addTextChangedListener(mTextWatcher);
        mEditTextCourseM2.addTextChangedListener(mTextWatcher);
        mEditTextCourseB1.addTextChangedListener(mTextWatcher);
        mEditTextCourseB2.addTextChangedListener(mTextWatcher);

        mEditTextCourseM1.addTextChangedListener(new TextChangeListener(mEditTextCourseM1));
        mEditTextCourseM2.addTextChangedListener(new TextChangeListener(mEditTextCourseM2));
        mEditTextCourseB1.addTextChangedListener(new TextChangeListener(mEditTextCourseB1));
        mEditTextCourseB2.addTextChangedListener(new TextChangeListener(mEditTextCourseB2));
    }

    @Override
    public void setTextBimonthlyGrade1(String message) {
        mSimulateB1.setText(message);
    }

    @Override
    public void setTextBimonthlyGrade2(String message) {
        mSimulateB2.setText(message);
    }

    @Override
    public void setTextFinalAverage(String message) {
        mSimulateMF.setText(message);
    }
}
