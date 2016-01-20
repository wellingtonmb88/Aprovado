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
import android.widget.TextView;

import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.listener.TextChangeListener;
import com.wellingtonmb88.aprovado.utils.CommonUtils;

import java.text.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CalculatorFragment extends Fragment {

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
    private String mEditTextErrorMessage;

    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (mEditTextCourseB1.getText().length() > 0 || mEditTextCourseM1.getText().length() > 0) {
                float m1 = 0;
                float b1 = 0;
                try {
                    m1 = CommonUtils.parseFloatLocaleSensitive(mEditTextCourseM1.getText().toString());
                    b1 = CommonUtils.parseFloatLocaleSensitive(mEditTextCourseB1.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (mEditTextCourseB1.getText().length() < 1 && mEditTextCourseM1.getText().length() > 0) {
                    builderDialogBimestral(mSimulateB1, mEditTextCourseM1.getText().toString());
                } else if (mEditTextCourseB1.getText().length() > 0 && mEditTextCourseM1.getText().length() > 0) {
                    float media = CommonUtils.roundFloatTwoHouse(((m1 * 4) + (b1 * 6)) / 10);
                    mEditTextCourseMB1.setError(null);
                    mEditTextCourseMB1.setText(String.valueOf(media));
                }
            }

            if (mEditTextCourseB2.getText().length() > 0 || mEditTextCourseM2.getText().length() > 0) {
                float m2 = 0;
                float b2 = 0;
                try {
                    m2 = CommonUtils.parseFloatLocaleSensitive(mEditTextCourseM2.getText().toString());
                    b2 = CommonUtils.parseFloatLocaleSensitive(mEditTextCourseB2.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (mEditTextCourseB2.getText().length() < 1 && mEditTextCourseM2.getText().length() > 0) {
                    builderDialogBimestral(mSimulateB2, mEditTextCourseM2.getText().toString());
                } else if (mEditTextCourseB2.getText().length() > 0 && mEditTextCourseM2.getText().length() > 0) {
                    float media = CommonUtils.roundFloatTwoHouse(((m2 * 4) + (b2 * 6)) / 10);
                    mEditTextCourseMB2.setText(String.valueOf(media));
                }
            }

            if (mEditTextCourseMB1.getText().length() > 0 && mEditTextCourseM2.getText().length() > 0) {

                builderDialogMediaFinal(mSimulateMF, mEditTextCourseM2.getText().toString(), mEditTextCourseMB1.getText().toString());
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

                float media = CommonUtils.roundFloatTwoHouse(((mb1 * 2) + (mb2 * 3)) / 5);
                mEditTextCourseMF.setText(String.valueOf(media));
            }

            if (mEditTextCourseM1.getText().length() < 1 || mEditTextCourseM2.getText().length() < 1) {
                mSimulateMF.setText(getString(R.string.calculator_dialog_to_approve_final));
                if (mEditTextCourseM1.getText().length() < 1) {
                    mSimulateB1.setText(getString(R.string.calculator_dialog_to_approve));
                }
                if (mEditTextCourseM2.getText().length() < 1) {
                    mSimulateB2.setText(getString(R.string.calculator_dialog_to_approve));
                }
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

            if (mEditTextCourseM1.getText().length() < 1) {
                mEditTextCourseMB1.setText("");
            }

            if (mEditTextCourseM2.getText().length() < 1) {
                mEditTextCourseMB2.setText("");
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        ButterKnife.bind(this, view);

        loadDataUI();
        setButtonListener();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void loadDataUI() {

        mEditTextErrorMessage = getString(R.string.calculator_edittext_error_message);

        mEditTextCourseM1.addTextChangedListener(mTextWatcher);
        mEditTextCourseM2.addTextChangedListener(mTextWatcher);
        mEditTextCourseB1.addTextChangedListener(mTextWatcher);
        mEditTextCourseB2.addTextChangedListener(mTextWatcher);

        mEditTextCourseM1.addTextChangedListener(new TextChangeListener(mEditTextCourseM1));
        mEditTextCourseM2.addTextChangedListener(new TextChangeListener(mEditTextCourseM2));
        mEditTextCourseB1.addTextChangedListener(new TextChangeListener(mEditTextCourseB1));
        mEditTextCourseB2.addTextChangedListener(new TextChangeListener(mEditTextCourseB2));
    }

    private void setButtonListener() {

        mSimulateB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditTextCourseM1.getText().length() < 1) {
                    mEditTextCourseM1.setError(mEditTextErrorMessage);
                } else {
                    // builderDialogBimestral(mEditTextCourseM1.getText().toString());
                }

            }
        });

        mSimulateB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mEditTextCourseM2.getText().length() < 1) {
                    mEditTextCourseM2.setError(mEditTextErrorMessage);
                } else {
                    // builderDialogBimestral(mEditTextCourseM2.getText().toString());
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
                    //builderDialogMediaFinal(mEditTextCourseM2.getText().toString(), mEditTextCourseMB1.getText().toString());
                }
            }
        });
    }

    private void builderDialogBimestral(TextView textView, String mensal) {
        float notaMensal = 0;
        try {
            notaMensal = CommonUtils.parseFloatLocaleSensitive(mensal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        double notaBimestral = CommonUtils.roundFloatOneHouse((5 - (notaMensal * 0.4)) / 0.6);
        double result = (notaMensal * 0.4) + (notaBimestral * 0.6);

        if (result < 5) {
            notaBimestral = Math.abs(notaBimestral + 0.37);
        }

        textView.setText(getString(R.string.calculator_dialog_to_approve) + " " +
                String.valueOf(CommonUtils.roundFloatOneHouse(notaBimestral)));
    }

    private void builderDialogMediaFinal(TextView textView, String mensal, String bimestral) {
        double mb1 = 0;
        double notaMensal2 = 0;

        try {
            mb1 = CommonUtils.parseFloatLocaleSensitive(bimestral);
            notaMensal2 = CommonUtils.parseFloatLocaleSensitive(mensal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        double mb2 = CommonUtils.roundFloatOneHouse((((((mb1 * 2) - 25) / 5) * 5) / 3) * -1);
        double notaBimestral = (mb2 - (notaMensal2 * 0.4)) / 0.6;
        double result = ((mb1 * 2) + (mb2 * 3)) / 5;

        if (notaBimestral <= 0) {
            notaBimestral = 0;
        } else if (notaBimestral > 0 && notaBimestral < 1) {
            notaBimestral = 1;
        } else if (result < 5) {
            notaBimestral = Math.abs(notaBimestral + 0.36);
        }

        if (notaBimestral > 10) {
            textView.setText(getString(R.string.calculator_dialog_unpassed));
        } else {
            textView.setText(getString(R.string.calculator_dialog_to_approve_final) + " " + String.valueOf(CommonUtils.roundFloatOneHouse(notaBimestral)));
        }
    }


}
