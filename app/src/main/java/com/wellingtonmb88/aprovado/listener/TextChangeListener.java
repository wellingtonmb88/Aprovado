package com.wellingtonmb88.aprovado.listener;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.wellingtonmb88.aprovado.utils.CommonUtils;

import java.lang.ref.WeakReference;

public class TextChangeListener implements TextWatcher {

    private String mBeforeTextChanged;
    private WeakReference<EditText> mEditText;

    public TextChangeListener(EditText editText){
        mEditText = new WeakReference<>(editText);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        mBeforeTextChanged = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(s)) {
            EditText editText = mEditText.get();
            if(editText != null) {
                int location = CommonUtils.validateLengthWithComma(editText, this, mBeforeTextChanged);
                if (location > -1) {
                    editText.setSelection(location);
                }
            }
        }
    }
}
