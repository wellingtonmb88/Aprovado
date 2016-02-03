package com.wellingtonmb88.aprovado.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.text.ParseException;
import java.util.Locale;

public abstract class CommonUtils {

    public static void hideSoftKeyBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    public static void backForResult(Activity activity, int selectedTab) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.TabSharedPreferences.SELECTED_TAB, selectedTab);
        activity.setResult(Activity.RESULT_OK, returnIntent);
        activity.finish();
    }

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

    public static float roundFloatTwoHouse(float value) {

        return Float.parseFloat(String.format(Locale.US, "%.2f", value));
    }

    public static float roundFloatOneHouse(double value) {

        return Float.parseFloat(String.format(Locale.US, "%.1f", value));
    }

    public static boolean isValidFloatFormatterValue(String value) {
        boolean isValid = false;
        try {
            if (parseFloatLocaleSensitive(value) >= 0 &&
                    parseFloatLocaleSensitive(value) <= 10
                    ) {
                isValid = true;
            }
        } catch (ParseException e) {
            AprovadoLogger.e("Error to parse String to Float: " + e.getLocalizedMessage());
        }
        return isValid;
    }

    /**
     * Remove Comma or Period if there's already one.
     * Verify the max input's length according with the input's length after the
     * comma or period, to avoid more than one input.
     */
    public static int validateLengthWithComma(EditText editText, TextWatcher textWatcher,
                                              String beforeTextChanged) {
        int cursorLocation = -1;
        final String PERIOD = ".";
        final String COMMA = ",";

        String currentInput = editText.getText().toString();

        if (!currentInput.isEmpty()) {

            editText.removeTextChangedListener(textWatcher);

            int selectionStart = (editText.getSelectionStart() - 1);

            /** Remove Comma or Period if there's already one.
             Limit only one house after Comma or Period.**/
            if ((selectionStart >= 0) && (currentInput.length() > beforeTextChanged.length())) {

                String characterFound = String.valueOf(currentInput.charAt(selectionStart));

                StringBuilder stringBuilder = new StringBuilder(currentInput);

                if (isValidFloatFormatterValue(currentInput) && !(currentInput.startsWith("0") && currentInput.length() > 1) && !currentInput.startsWith(".")) {
                    /** Remove Comma or Period if there's already one.**/
                    if ((characterFound.equals(PERIOD) || characterFound.equals(COMMA))
                            && ((beforeTextChanged.contains(PERIOD) || beforeTextChanged.contains(COMMA)))) {

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
                } else {
                    stringBuilder.deleteCharAt(selectionStart);
                    editText.setText(stringBuilder.toString());
                    cursorLocation = selectionStart;
                }

            } else {
                try {
                    float value = parseFloatLocaleSensitive(currentInput);
                    StringBuilder stringBuilder = new StringBuilder(currentInput);
                    if (value > 10 || currentInput.startsWith(".")) {
                        if (selectionStart > -1) {
                            stringBuilder.deleteCharAt(selectionStart);
                        } else {
                            stringBuilder.deleteCharAt(0);
                        }
                        editText.setText(stringBuilder.toString());
                        cursorLocation = editText.length();
                    }
                } catch (ParseException e) {
                    AprovadoLogger.e("Error to parse String to Float: " + e.getLocalizedMessage());
                }
            }

            editText.addTextChangedListener(textWatcher);
        }

        return cursorLocation;
    }
}
