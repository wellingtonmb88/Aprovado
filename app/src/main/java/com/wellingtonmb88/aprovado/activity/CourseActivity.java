package com.wellingtonmb88.aprovado.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.async.SQliteAsyncTask;
import com.wellingtonmb88.aprovado.entity.Course;
import com.wellingtonmb88.aprovado.utils.CommonUtils;
import com.wellingtonmb88.aprovado.utils.Constants;

import java.text.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CourseActivity extends AppCompatActivity {

    @Bind(R.id.editText_disciplina)
    EditText mDisciplina;
    @Bind(R.id.editText_professor)
    EditText mProfessor;
    @Bind(R.id.editText_m1)
    EditText mEditTextM1;
    @Bind(R.id.editText_m2)
    EditText mEditTextM2;
    @Bind(R.id.editText_b1)
    EditText mEditTextB1;
    @Bind(R.id.editText_b2)
    EditText mEditTextB2;
    @Bind(R.id.editText_mb1)
    EditText mEditTextMB1;
    @Bind(R.id.editText_mb2)
    EditText mEditTextMB2;
    @Bind(R.id.editText_mf)
    EditText mEditTextMF;
    @Bind(R.id.spinner)
    Spinner mSpinnerSemestre;
    @Bind(R.id.toolbar_layout)
    Toolbar mToolbarLayout;

    private Course mCourse;
    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (!TextUtils.isEmpty(s)) {
                mDisciplina.setError(null);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        ButterKnife.bind(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        loadDataUI();

        mDisciplina.addTextChangedListener(mTextWatcher);
    }

    @Override
    public void onBackPressed() {
        backForResult();
        super.onBackPressed();
    }

    private void loadDataUI() {

        mCourse = new Course();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CourseActivity.this,
                R.array.semester_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSemestre.setAdapter(adapter);

        mToolbarLayout.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(mToolbarLayout);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(Constants.CourseExtra.INTENT_EXTRA)) {
            Bundle bundle = intent.getBundleExtra(Constants.CourseExtra.INTENT_EXTRA);
            if (bundle != null) {
                mCourse = bundle.getParcelable(Constants.CourseExtra.BUNDLE_EXTRA);
                if (mCourse != null) {

                    if (mCourse.name != null) {
                        mDisciplina.setText(mCourse.name);
                        mProfessor.setText(mCourse.professor);
                        mSpinnerSemestre.setSelection(mCourse.semester);
                        validateFields();
                    }
                }
            }
        }

    }

    @OnClick(R.id.floatingActionButton_save)
    public void onSave() {
        try {
            String action = Constants.CourseDatabaseAction.INSERT_COURSE;
            if (mCourse != null && mCourse.name != null && mCourse.name.length() > 0) {
                action = Constants.CourseDatabaseAction.UPDATE_COURSE;
            }
            getCourse();
            if (mDisciplina.getText().length() > 0) {
                SQliteAsyncTask task = new SQliteAsyncTask(getApplicationContext(), null, mCourse);
                task.execute(action);
                finish();
            } else {
                mDisciplina.setError(getString(R.string.calculator_edittext_error_message));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void getCourse() throws ParseException {

        mCourse.name = mDisciplina.getText().toString();
        mCourse.professor = mProfessor.getText().toString();
        mCourse.semester = mSpinnerSemestre.getSelectedItemPosition();
        validateNullFields();
    }

    private void validateNullFields() throws ParseException {

        if (TextUtils.isEmpty(mEditTextM1.getText())) {
            mCourse.m1 = -1;
        } else {
            mCourse.m1 = CommonUtils.parseFloatLocaleSensitive(mEditTextM1.getText().toString());
        }
        if (TextUtils.isEmpty(mEditTextB1.getText())) {
            mCourse.b1 = -1;
        } else {
            mCourse.b1 = CommonUtils.parseFloatLocaleSensitive(mEditTextB1.getText().toString());
        }
        if (TextUtils.isEmpty(mEditTextMB1.getText())) {
            mCourse.mediaB1 = -1;
        } else {
            mCourse.mediaB1 = CommonUtils.parseFloatLocaleSensitive(mEditTextMB1.getText().toString());
        }
        if (TextUtils.isEmpty(mEditTextM2.getText())) {
            mCourse.m2 = -1;
        } else {
            mCourse.m2 = CommonUtils.parseFloatLocaleSensitive(mEditTextM2.getText().toString());
        }
        if (TextUtils.isEmpty(mEditTextB2.getText())) {
            mCourse.b2 = -1;
        } else {
            mCourse.b2 = CommonUtils.parseFloatLocaleSensitive(mEditTextB2.getText().toString());
        }
        if (TextUtils.isEmpty(mEditTextMB2.getText())) {
            mCourse.mediaB2 = -1;
        } else {
            mCourse.mediaB2 = CommonUtils.parseFloatLocaleSensitive(mEditTextMB2.getText().toString());
        }
        if (TextUtils.isEmpty(mEditTextMF.getText())) {
            mCourse.mediaFinal = -1;
        } else {
            mCourse.mediaFinal = CommonUtils.parseFloatLocaleSensitive(mEditTextMF.getText().toString());
        }
    }

    private void validateFields() {
        String m1 = String.valueOf(mCourse.m1);
        String m2 = String.valueOf(mCourse.m2);
        String b1 = String.valueOf(mCourse.b1);
        String b2 = String.valueOf(mCourse.b2);
        String mb1 = String.valueOf(mCourse.mediaB1);
        String mb2 = String.valueOf(mCourse.mediaB2);
        String mf = String.valueOf(mCourse.mediaFinal);

        String MINUS_ONE = "-1.0";

        if (!MINUS_ONE.equals(m1)) {
            mEditTextM1.setText(m1);
        }
        if (!MINUS_ONE.equals(m2)) {
            mEditTextM2.setText(m2);
        }
        if (!MINUS_ONE.equals(b1)) {
            mEditTextB1.setText(b1);
        }
        if (!MINUS_ONE.equals(b2)) {
            mEditTextB2.setText(b2);
        }
        if (!MINUS_ONE.equals(mb1)) {
            mEditTextMB1.setText(mb1);
        }
        if (!MINUS_ONE.equals(mb2)) {
            mEditTextMB2.setText(mb2);
        }
        if (!MINUS_ONE.equals(mf)) {
            mEditTextMF.setText(mf);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            backForResult();
        }
        return super.onOptionsItemSelected(item);
    }

    private void backForResult() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.TabSharedPreferences.SELECTED_TAB, 1);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
