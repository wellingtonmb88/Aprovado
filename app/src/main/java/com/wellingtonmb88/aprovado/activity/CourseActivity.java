package com.wellingtonmb88.aprovado.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.wellingtonmb88.aprovado.AppApplication;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.dagger.components.DaggerActivityInjectorComponent;
import com.wellingtonmb88.aprovado.database.DatabaseHelper;
import com.wellingtonmb88.aprovado.entity.Course;
import com.wellingtonmb88.aprovado.presenter.CourseDetailsPresenterImpl;
import com.wellingtonmb88.aprovado.presenter.view.CourseDetailsView;
import com.wellingtonmb88.aprovado.utils.AprovadoLogger;
import com.wellingtonmb88.aprovado.utils.CommonUtils;
import com.wellingtonmb88.aprovado.utils.Constants;

import java.text.ParseException;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CourseActivity extends BaseActivity implements CourseDetailsView {

    @Bind(R.id.editText_disciplina)
    EditText mSubject;
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
    Spinner mSpinnerSemester;
    @Bind(R.id.toolbar_layout)
    Toolbar mToolbarLayout;
    @Inject
    DatabaseHelper<Course> mDatabaseHelper;
    @Inject
    CourseDetailsPresenterImpl mCourseDetailsPresenter;
    private Course mCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        ButterKnife.bind(this);

        DaggerActivityInjectorComponent.builder()
                .baseComponent(AppApplication.getBaseComponent())
                .build()
                .inject(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mCourseDetailsPresenter.registerView(this);
        mCourseDetailsPresenter.registerDatabaseHelper(mDatabaseHelper);
        loadDataUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCourse = null;
        mDatabaseHelper = null;
        mCourseDetailsPresenter.onDestroy();
    }

    @Override
    public void onBackPressed() {
        CommonUtils.backForResult(CourseActivity.this, 0);
        super.onBackPressed();
    }

    private void loadDataUI() {

        mCourse = new Course();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CourseActivity.this,
                R.array.semester_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSemester.setAdapter(adapter);

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
                String courseId = bundle.getString(Constants.CourseExtra.BUNDLE_EXTRA);
                mCourseDetailsPresenter.onGetCourse(courseId);
            }
        } else {
            mCourse.setId(UUID.randomUUID().toString());
        }

    }

    @OnClick(R.id.floatingActionButton_save)
    public void saveButton() {
        mCourseDetailsPresenter.onSaveCourse();
    }

    private void populateCourse() {
        mCourse.setName(mSubject.getText().toString());
        mCourse.setProfessor(mProfessor.getText().toString());
        mCourse.setSemester(mSpinnerSemester.getSelectedItemPosition());
        validateNullFields();
    }

    private void validateNullFields() {

        try {
            if (TextUtils.isEmpty(mEditTextM1.getText())) {
                mCourse.setM1(-1);
            } else {
                mCourse.setM1(CommonUtils.parseFloatLocaleSensitive(mEditTextM1.getText().toString()));
            }
            if (TextUtils.isEmpty(mEditTextB1.getText())) {
                mCourse.setB1(-1);
            } else {
                mCourse.setB1(CommonUtils.parseFloatLocaleSensitive(mEditTextB1.getText().toString()));
            }
            if (TextUtils.isEmpty(mEditTextMB1.getText())) {
                mCourse.setMediaB1(-1);
            } else {
                mCourse.setMediaB1(CommonUtils.parseFloatLocaleSensitive(mEditTextMB1.getText().toString()));
            }
            if (TextUtils.isEmpty(mEditTextM2.getText())) {
                mCourse.setM2(-1);
            } else {
                mCourse.setM2(CommonUtils.parseFloatLocaleSensitive(mEditTextM2.getText().toString()));
            }
            if (TextUtils.isEmpty(mEditTextB2.getText())) {
                mCourse.setB2(-1);
            } else {
                mCourse.setB2(CommonUtils.parseFloatLocaleSensitive(mEditTextB2.getText().toString()));
            }
            if (TextUtils.isEmpty(mEditTextMB2.getText())) {
                mCourse.setMediaB2(-1);
            } else {
                mCourse.setMediaB2(CommonUtils.parseFloatLocaleSensitive(mEditTextMB2.getText().toString()));
            }
            if (TextUtils.isEmpty(mEditTextMF.getText())) {
                mCourse.setMediaFinal(-1);
            } else {
                mCourse.setMediaFinal(CommonUtils.parseFloatLocaleSensitive(mEditTextMF.getText().toString()));
            }

        } catch (ParseException e) {
            AprovadoLogger.e("Error to parse String to Float: " + e.getLocalizedMessage());
        }
    }

    private void validateFields() {
        String m1 = String.valueOf(mCourse.getM1());
        String m2 = String.valueOf(mCourse.getM2());
        String b1 = String.valueOf(mCourse.getB1());
        String b2 = String.valueOf(mCourse.getB2());
        String mb1 = String.valueOf(mCourse.getMediaB1());
        String mb2 = String.valueOf(mCourse.getMediaB2());
        String mf = String.valueOf(mCourse.getMediaFinal());

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
            CommonUtils.backForResult(CourseActivity.this, 0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void saveCourse() {
        populateCourse();
        if (!TextUtils.isEmpty(mSubject.getText())) {
            mDatabaseHelper.createOrUpdate(mCourse);
            setResult(RESULT_OK);
            finish();
        } else {
            mSubject.setError(getString(R.string.calculator_edittext_error_message));
        }
    }

    @Override
    public void getCourse(Course course) {
        mCourse = course;
        if (mCourse.getName() != null) {
            mSubject.setText(mCourse.getName());
            mProfessor.setText(mCourse.getProfessor());
            mSpinnerSemester.setSelection(mCourse.getSemester());
            validateFields();
        }
    }

    @Override
    public CourseActivity getActivity() {
        return this;
    }
}
