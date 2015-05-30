package com.wellingtonmb88.aprovado.utils;

import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Wellington on 29/05/2015.
 */
public class CommonUtils {

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
        return roundFloatTwoHouse(f);
    }

    public static float roundFloatTwoHouse(float value){

        return Float.parseFloat(String.format(Locale.US, "%.2f", value));
    }

    public static float roundFloatOneHouse(double value){

        return Float.parseFloat(String.format(Locale.US, "%.1f", value));
    }

    public static boolean isValidFloatFormatterValue(String value) {
        boolean isValid = false;
        try {
            if(  Float.valueOf(parseFloatLocaleSensitive(value))  >=0 &&
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

                if(isValidFloatFormatterValue(currentInput) && !(currentInput.startsWith("0") && currentInput.length() >1) && !currentInput.startsWith(".") ) {
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

            }else{
                try {
                    float value = parseFloatLocaleSensitive(currentInput);
                    StringBuilder stringBuilder = new StringBuilder(currentInput.toString());
                    if(value > 10 || currentInput.startsWith(".")){
                        //editText.setText("");
                        if(selectionStart > -1){
                            stringBuilder.deleteCharAt(selectionStart);
                        }else {
                            stringBuilder.deleteCharAt(0);
                        }
                        editText.setText(stringBuilder.toString());
                        cursorLocation = editText.length();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            editText.addTextChangedListener(textWatcher);
        }

        return cursorLocation;
    }
}
