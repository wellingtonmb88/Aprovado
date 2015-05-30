package com.wellingtonmb88.aprovado.listener;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.wellingtonmb88.aprovado.utils.CommonUtils;

/**
 * Created by Wellington on 29/05/2015.
 */
public class TextChangeListener {

    private String mBeforeTextChanded;
    private EditText mEditText;

    public TextChangeListener(EditText editText){
        mEditText = editText;
    }

    public TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mBeforeTextChanded = charSequence.toString();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (!(s.toString().isEmpty() && s.toString().equals(""))) {
                int location = CommonUtils.validateLengthWithComma(mEditText, this, mBeforeTextChanded);
                if(location > -1)  {
                    mEditText.setSelection(location);
                }
            }

        }
    };


}
