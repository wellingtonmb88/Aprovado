package com.wellingtonmb88.aprovado.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.wellingtonmb88.aprovado.utils.CommonUtils;
import com.wellingtonmb88.aprovado.utils.Constants;

import java.text.ParseException;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

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
    @Inject
    DatabaseHelper<Course> mDatabaseHelper;
    private Course mCourse;
    private Action1<Course> getCourseAction = new Action1<Course>() {
        @Override
        public void call(Course course) {
            mCourse = course;
            if (mCourse.getName() != null) {
                mDisciplina.setText(mCourse.getName());
                mProfessor.setText(mCourse.getProfessor());
                mSpinnerSemestre.setSelection(mCourse.getSemester());
                validateFields();
            }
        }
    };

    private Subscription mSubscription;

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

        loadDataUI();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCourse = null;
        mDatabaseHelper = null;
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mSubscription = null;
        getCourseAction = null;
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
                String courseId = bundle.getString(Constants.CourseExtra.BUNDLE_EXTRA);
                mSubscription = mDatabaseHelper.getById(Course.class, courseId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getCourseAction);
            }
        } else {
            mCourse.setId(UUID.randomUUID().toString());
        }

    }

    @OnClick(R.id.floatingActionButton_save)
    public void onSave() {
        try {
            getCourse();
            if (!TextUtils.isEmpty(mDisciplina.getText())) {
                mDatabaseHelper.createOrUpdate(mCourse);
                finish();
            } else {
                mDisciplina.setError(getString(R.string.calculator_edittext_error_message));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void getCourse() throws ParseException {

        mCourse.setName(mDisciplina.getText().toString());
        mCourse.setProfessor(mProfessor.getText().toString());
        mCourse.setSemester(mSpinnerSemestre.getSelectedItemPosition());
        validateNullFields();
    }

    private void validateNullFields() throws ParseException {

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
