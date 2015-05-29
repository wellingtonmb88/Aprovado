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

/**
 * Created by Wellington on 25/05/2015.
 */
public class CalculatorFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.calculator, container, false);

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

        mEditTextCourseM1.addTextChangedListener(notaFormatter);
        mEditTextCourseM2.addTextChangedListener(notaFormatter);
        mEditTextCourseB1.addTextChangedListener(notaFormatter);
        mEditTextCourseB2.addTextChangedListener(notaFormatter);

        setButtonListener();
        return view;
    }

    private void setButtonListener() {
        mSimulateB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditTextCourseM1.getText().length() < 1 ) {

                    mEditTextCourseM1.setError("Campo Obrigatorio");
                } else {

                    float m1 = Float.parseFloat(mEditTextCourseM1.getText().toString());

                    float b1 = (((50-(m1*4))/10)*10)/6;
                    builderDialog(b1);
                }

            }
        });

        mSimulateB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mEditTextCourseM2.getText().length() < 1 ) {

                    mEditTextCourseM2.setError("Campo Obrigatorio");
                } else {

                    float m2 = Float.parseFloat(mEditTextCourseM2.getText().toString());

                    float b2 = (((50-(m2*4))/10)*10)/6;
                    builderDialog(b2);
                }

            }
        });

        mSimulateMF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mEditTextCourseMB1.getText().length() < 1 || mEditTextCourseM2.getText().length() < 1) {
                    if (mEditTextCourseMB1.getText().length() < 1) {
                        mEditTextCourseMB1.setError("Campo Obrigatorio");
                        if (mEditTextCourseM1.getText().length() < 1) {

                            mEditTextCourseM1.setError("Campo Obrigatorio");
                        }

                        if (mEditTextCourseB1.getText().length() < 1) {

                            mEditTextCourseB1.setError("Campo Obrigatorio");
                        }
                    }

                    if (mEditTextCourseM2.getText().length() < 1) {

                        mEditTextCourseM2.setError("Campo Obrigatorio");
                    }
                } else {
                    float mb1 = Float.parseFloat(mEditTextCourseM2.getText().toString());
                    float m2 = Float.parseFloat(mEditTextCourseM2.getText().toString());
                    float media = (((((mb1 * 2) - 25) / 5) * 5) / 3) * -1;
                    float b2 = ((((media*10)-(m2*4))/10)*10)/6;
                    builderDialog(b2);
                }
            }
        });
    }


    private void builderDialog(float value) {
        new MaterialDialog.Builder(getActivity())
                .title("Simulacao")
                .content("Voce precisa tirar no minimo uma nota " + value)
                .positiveText("Ok")
                .show();
    }

    private TextWatcher notaFormatter = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (mEditTextCourseB1.getText().length() > 0 && mEditTextCourseM1.getText().length() > 0) {
                float m1 = Float.parseFloat(mEditTextCourseM1.getText().toString());
                float b1 = Float.parseFloat(mEditTextCourseB1.getText().toString());

                float media = ((m1 * 4) + (b1 * 6)) / 10;
                mEditTextCourseMB1.setError(null);
                mEditTextCourseMB1.setText(String.valueOf(media));
            }
            if (mEditTextCourseB2.getText().length() > 0 && mEditTextCourseM2.getText().length() > 0) {
                float m2 = Float.parseFloat(mEditTextCourseM2.getText().toString());
                float b2 = Float.parseFloat(mEditTextCourseB2.getText().toString());

                float media = ((m2 * 4) + (b2 * 6)) / 10;

                mEditTextCourseMB2.setText(String.valueOf(media));

            }

            if (mEditTextCourseMB1.getText().length() > 0 && mEditTextCourseMB2.getText().length() > 0) {
                float mb1 = Float.parseFloat(mEditTextCourseMB1.getText().toString());
                float mb2 = Float.parseFloat(mEditTextCourseMB2.getText().toString());

                float media = ((mb1 * 2) / 5) + ((mb2 * 3) / 5);

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
