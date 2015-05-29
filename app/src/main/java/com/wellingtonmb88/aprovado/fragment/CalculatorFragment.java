package com.wellingtonmb88.aprovado.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.custom.CustomRippleView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

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
       // mEditTextCourseM2.addTextChangedListener(notaFormatter);
        mEditTextCourseM1.addTextChangedListener(notaFormatter2);
        mEditTextCourseM2.addTextChangedListener(notaFormatter3);
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

                    float m1 = 0;
                    try {
                        m1 = parseFloatLocaleSensitive(mEditTextCourseM1.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

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

                    float m2 = 0;
                    try {
                        m2 = parseFloatLocaleSensitive(mEditTextCourseM2.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

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
                    float mb1 = 0;
                    try {
                        mb1 = parseFloatLocaleSensitive(mEditTextCourseM2.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    float m2 = 0;
                    try {
                        m2 = parseFloatLocaleSensitive(mEditTextCourseM2.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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

            Log.d("CalulatorFragment", "beforeTextChanged " + charSequence.toString());
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            Log.d("CalulatorFragment", "onTextChanged " + charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

            if(!isValidFloatFormatterValue(editable.toString())) {
                return;
            }
            Log.d("CalulatorFragment", "afterTextChanged " + editable.toString());
            if (mEditTextCourseB1.getText().length() > 0 && mEditTextCourseM1.getText().length() > 0) {
                float m1 = 0;
                try {
                    m1 = parseFloatLocaleSensitive(mEditTextCourseM1.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                float b1 = 0;
                try {
                    b1 = parseFloatLocaleSensitive(mEditTextCourseB1.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                float media = ((m1 * 4) + (b1 * 6)) / 10;
                mEditTextCourseMB1.setError(null);
                mEditTextCourseMB1.setText(String.valueOf(media));
            }
            if (mEditTextCourseB2.getText().length() > 0 && mEditTextCourseM2.getText().length() > 0) {
                float m2 = 0;
                try {
                    m2 = parseFloatLocaleSensitive(mEditTextCourseM2.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                float b2 = 0;
                try {
                    b2 = parseFloatLocaleSensitive(mEditTextCourseB2.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                float media = ((m2 * 4) + (b2 * 6)) / 10;

                mEditTextCourseMB2.setText(String.valueOf(media));

            }

            if (mEditTextCourseMB1.getText().length() > 0 && mEditTextCourseMB2.getText().length() > 0) {
                float mb1 = 0;
                try {
                    mb1 = parseFloatLocaleSensitive(mEditTextCourseMB1.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                float mb2 = 0;
                try {
                    mb2 = parseFloatLocaleSensitive(mEditTextCourseMB2.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

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


    private TextWatcher notaFormatter2 = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            Log.d("CalulatorFragment", "beforeTextChanged " + charSequence.toString());
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            Log.d("CalulatorFragment", "onTextChanged " + charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (!isValidFloatFormatterValue(editable.toString())) {
                return;
            }
        }
    };

    String mBeforeTextChanded;
    private TextWatcher notaFormatter3 = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            Log.d("CalulatorFragment", "beforeTextChanged " + charSequence.toString());
            mBeforeTextChanded = charSequence.toString();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            Log.d("CalulatorFragment", "onTextChanged " + charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (!(s.toString().isEmpty() && s.toString().equals(""))) {
                int location =  validateLengthWithComma(mEditTextCourseM2, this, mBeforeTextChanded);
                if(location > -1)
                {
                    mEditTextCourseM2.setSelection(location);
                }
            }else{
                return;
            }
        }
    };

    public static float parseFloatLocaleSensitive(String str) throws ParseException {
        String st = str.replace(",.", ".");
        st = st.replace(".,", ".");
        st = st.replace(",", ".");
        st = st.replace("...", ".");
        st = st.replace("..", ".");
        float f = 0.0f;
        int commaNumber = st.length() - st.replace(".", "").length();
        int dotNumber = st.length() - st.replace(",", "").length();
        if (!st.equals("") && !st.equals(".") && commaNumber <= 1 && dotNumber <= 1) {
            f = Float.valueOf(st);
        }
        return roundFloat(f);
    }

    public static float roundFloat(float value){

        return Float.parseFloat(String.format(Locale.US, "%.1f", value));
    }

    public static boolean isValidFloatFormatterValue(String value) {
        boolean isValid = false;
        try {
            DecimalFormat df = new DecimalFormat("#.0");
            df.format(parseFloatLocaleSensitive(value));
            if(  Math.round(parseFloatLocaleSensitive(df.format(parseFloatLocaleSensitive(value)))) >=0 &&
                    Float.valueOf(parseFloatLocaleSensitive(value)) <= 10
            ){
                isValid = true;
            }
        } catch (Exception e) {
            Log.d("CalulatorFragment", "" + e);
        }
        return isValid;
    }

    /**
     * Remove Comma or Period if there´s already one.
     * Verify the max input's length according with the input's length after the
     * comma or period, to avoid more than one input.
     * @param editText
     */
    public static int validateLengthWithComma(EditText editText, TextWatcher textWatcher,
                                              String beforeTextChanded) {

        int cursorLocation = -1;
        final String PERIOD = ".";
        final String COMMA = ",";

        String currentInput = editText.getText().toString();
         if (!currentInput.isEmpty()) {

            editText.removeTextChangedListener(textWatcher);

            int selectionStart = (editText.getSelectionStart() - 1);

            /** Remove Comma or Period if there´s already one.
             Limit only one house after Comma or Period.**/
            if((selectionStart >= 0) && (currentInput.length() > beforeTextChanded.length())){

                String characterFound = String.valueOf(currentInput.charAt(selectionStart));

                StringBuilder stringBuilder = new StringBuilder(currentInput.toString());
                if(isValidFloatFormatterValue(currentInput)) {
                    /** Remove Comma or Period if there´s already one.**/
                    if ((characterFound.equals(PERIOD) || characterFound.equals(COMMA))
                            && ((beforeTextChanded.contains(PERIOD) || beforeTextChanded.contains(COMMA)))) {

                        stringBuilder.deleteCharAt(selectionStart);
                        editText.setText(stringBuilder.toString());
                        cursorLocation = selectionStart;

                    } else if (currentInput.contains(PERIOD) || currentInput.contains(COMMA)) {

                        int indexOfCommaAndPeriod = currentInput.lastIndexOf((currentInput.contains(PERIOD) ? PERIOD : COMMA));
                        int lengthAfterCommaAndPeriod = (currentInput.substring(indexOfCommaAndPeriod, currentInput.length())).length();

                        /** Remove any digit/character if the house
                         after Comma or Period is greater than one. **/
                        if ((lengthAfterCommaAndPeriod - 1) > 1) {
                            stringBuilder.deleteCharAt(selectionStart);
                            editText.setText(stringBuilder.toString());
                            cursorLocation = selectionStart;
                        }
                    }
                }else{
                    stringBuilder.deleteCharAt(selectionStart);
                    editText.setText(stringBuilder.toString());
                    cursorLocation = selectionStart;
                }

            }

            editText.addTextChangedListener(textWatcher);
        }

        return cursorLocation;
    }
}
