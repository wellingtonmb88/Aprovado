package com.wellingtonmb88.aprovado.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.custom.CustomRippleView;
import com.wellingtonmb88.aprovado.listener.TextChangeListener;
import com.wellingtonmb88.aprovado.utils.CommonUtils;

import java.text.ParseException;

/**
 * Created by Wellington on 25/05/2015.
 */
public class CalculatorFragment extends Fragment {

    private String mEditTextErrorMessage;

    private EditText mEditTextCourseM1;
    private EditText mEditTextCourseM2;
    private EditText mEditTextCourseB1;
    private EditText mEditTextCourseB2;
    private EditText mEditTextCourseMB1;
    private EditText mEditTextCourseMB2;
    private EditText mEditTextCourseMF;

    private CustomRippleView mSimulateB1;
    private CustomRippleView mSimulateB2;
    private CustomRippleView mSimulateMF;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        loadUI(view);
        loadDataUI();
        setButtonListener();
        return view;
    }

    private void loadUI(View view){
        mEditTextCourseM1 = (EditText) view.findViewById(R.id.editText_m1);
        mEditTextCourseM2 = (EditText) view.findViewById(R.id.editText_m2);
        mEditTextCourseB1 = (EditText) view.findViewById(R.id.editText_b1);
        mEditTextCourseB2 = (EditText) view.findViewById(R.id.editText_b2);
        mEditTextCourseMB1 = (EditText) view.findViewById(R.id.editText_mb1);
        mEditTextCourseMB2 = (EditText) view.findViewById(R.id.editText_mb2);
        mEditTextCourseMF = (EditText) view.findViewById(R.id.editText_mf);

        mSimulateB1 = (CustomRippleView) view.findViewById(R.id.button_simulate_mb1);
        mSimulateB2 = (CustomRippleView) view.findViewById(R.id.button_simulate_mb2);
        mSimulateMF = (CustomRippleView) view.findViewById(R.id.button_simulate_mf);
    }

    private  void loadDataUI(){

        mEditTextErrorMessage = getString(R.string.calculator_edittext_error_message);

        mEditTextCourseM1.addTextChangedListener(mTextWatcher);
        mEditTextCourseM2.addTextChangedListener(mTextWatcher);
        mEditTextCourseB1.addTextChangedListener(mTextWatcher);
        mEditTextCourseB2.addTextChangedListener(mTextWatcher);

        mEditTextCourseM1.addTextChangedListener(new TextChangeListener(mEditTextCourseM1).textWatcher);
        mEditTextCourseM2.addTextChangedListener(new TextChangeListener(mEditTextCourseM2).textWatcher);
        mEditTextCourseB1.addTextChangedListener(new TextChangeListener(mEditTextCourseB1).textWatcher);
        mEditTextCourseB2.addTextChangedListener(new TextChangeListener(mEditTextCourseB2).textWatcher);
    }

    private void setButtonListener() {

        mSimulateB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditTextCourseM1.getText().length() < 1 ) {
                    mEditTextCourseM1.setError(mEditTextErrorMessage);
                } else {
                    builderDialogBimestral(mEditTextCourseM1.getText().toString());
                }

            }
        });

        mSimulateB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mEditTextCourseM2.getText().length() < 1 ) {
                    mEditTextCourseM2.setError(mEditTextErrorMessage);
                } else {
                    builderDialogBimestral(mEditTextCourseM2.getText().toString());
                }

            }
        });

        mSimulateMF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mEditTextCourseMB1.getText().length() < 1 || mEditTextCourseM2.getText().length() < 1) {
                    if (mEditTextCourseMB1.getText().length() < 1) {
                        mEditTextCourseMB1.setError(mEditTextErrorMessage);
                        if (mEditTextCourseM1.getText().length() < 1) {

                            mEditTextCourseM1.setError(mEditTextErrorMessage);
                        }

                        if (mEditTextCourseB1.getText().length() < 1) {

                            mEditTextCourseB1.setError(mEditTextErrorMessage);
                        }
                    }

                    if (mEditTextCourseM2.getText().length() < 1) {

                        mEditTextCourseM2.setError(mEditTextErrorMessage);
                    }
                } else {
                    builderDialogMediaFinal(mEditTextCourseM2.getText().toString(), mEditTextCourseMB1.getText().toString());
                }
            }
        });
    }

    private void builderDialogBimestral(String mensal) {
        float m2 = 0;
        try {
            m2 = CommonUtils.parseFloatLocaleSensitive(mensal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        double b2 = CommonUtils.roundFloatOneHouse((5 - (m2 * 0.4)) / 0.6);
        double result = (m2 * 0.4)+(b2*0.6);

        if(result < 5){
            b2  = Math.round(b2 + 0.40);
        }

        new MaterialDialog.Builder(getActivity())
                .title(getString(R.string.calculator_dialog_title))
                .content(getString(R.string.calculator_dialog_to_approve) + " " + CommonUtils.roundFloatOneHouse(b2))
                .positiveText("Ok")
                .show();
    }

    private void builderDialogMediaFinal(String mensal, String bimestral) {
        double mb1 = 0;
        double m2 = 0;

        try {
            mb1 = CommonUtils.parseFloatLocaleSensitive(bimestral);
            m2 = CommonUtils.parseFloatLocaleSensitive(mensal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        double mb2 = CommonUtils.roundFloatOneHouse((((((mb1 * 2) - 25) / 5) * 5) / 3) * -1);
        double b2 = (mb2 - (m2 * 0.4))/0.6;
        double result = ((mb1 * 2)+(mb2 * 3))/5;

        if(result < 5){
            b2  = Math.round(b2 + 0.40);
        }
        if(b2 < 0){
            b2  = 0;
        }

        if(b2 > 10){
            new MaterialDialog.Builder(getActivity())
                    .title(getString(R.string.calculator_dialog_title))
                    .content(getString(R.string.calculator_dialog_unpassed))
                    .positiveText("Ok")
                    .show();
        }else{
            new MaterialDialog.Builder(getActivity())
                    .title(getString(R.string.calculator_dialog_title))
                    .content(getString(R.string.calculator_dialog_to_approve)+" " + CommonUtils.roundFloatOneHouse(b2))
                    .positiveText("Ok")
                    .show();
        }
    }

    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (mEditTextCourseB1.getText().length() > 0 && mEditTextCourseM1.getText().length() > 0) {
                float m1 = 0;
                float b1 = 0;
                try {
                    m1 = CommonUtils.parseFloatLocaleSensitive(mEditTextCourseM1.getText().toString());
                    b1 = CommonUtils.parseFloatLocaleSensitive(mEditTextCourseB1.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                float media = CommonUtils.roundFloatTwoHouse(((m1 * 4) + (b1 * 6)) / 10);
                mEditTextCourseMB1.setError(null);
                mEditTextCourseMB1.setText(String.valueOf(media));
            }

            if (mEditTextCourseB2.getText().length() > 0 && mEditTextCourseM2.getText().length() > 0) {
                float m2 = 0;
                float b2 = 0;
                try {
                    m2 = CommonUtils.parseFloatLocaleSensitive(mEditTextCourseM2.getText().toString());
                    b2 = CommonUtils.parseFloatLocaleSensitive(mEditTextCourseB2.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                float media = CommonUtils.roundFloatTwoHouse(((m2 * 4) + (b2 * 6)) / 10);
                mEditTextCourseMB2.setText(String.valueOf(media));
            }

            if (mEditTextCourseMB1.getText().length() > 0 && mEditTextCourseMB2.getText().length() > 0) {
                float mb1 = 0;
                float mb2 = 0;
                try {
                    mb1 = CommonUtils.parseFloatLocaleSensitive(mEditTextCourseMB1.getText().toString());
                    mb2 = CommonUtils.parseFloatLocaleSensitive(mEditTextCourseMB2.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                float media = CommonUtils.roundFloatTwoHouse(((mb1 * 2)  + (mb2 * 3))/ 5);
                mEditTextCourseMF.setText(String.valueOf(media));
            }

            if (mEditTextCourseM1.getText().length() < 1 || mEditTextCourseB1.getText().length() < 1) {
                mEditTextCourseMB1.setText("");
            }

            if (mEditTextCourseM2.getText().length() < 1 || mEditTextCourseB2.getText().length() < 1) {
                mEditTextCourseMB2.setText("");
            }

            if (mEditTextCourseMB1.getText().length() < 1 || mEditTextCourseMB2.getText().length() < 1) {
                mEditTextCourseMF.setText("");
            }
        }
    };




}
