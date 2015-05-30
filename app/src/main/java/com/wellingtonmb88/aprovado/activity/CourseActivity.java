package com.wellingtonmb88.aprovado.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.entity.Course;
import com.wellingtonmb88.aprovado.utils.Constants;

/**
 * Created by Wellington on 26/05/2015.
 */
public class CourseActivity extends AppCompatActivity {

    private EditText mDisciplina;
    private EditText mProfessor;
    private EditText mEditTextM1;
    private EditText mEditTextM2;
    private EditText mEditTextB1;
    private EditText mEditTextB2;
    private EditText mEditTextMB1;
    private EditText mEditTextMB2;
    private EditText mEditTextMF;
    
    private Spinner mSpinnerSemestre;
    private Toolbar mToolbarLayout;
    private FloatingActionButton mSaveFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        loadUI();
        loadDataUI();
    }

    private void loadUI(){

        FrameLayout toolbar = (FrameLayout) findViewById(R.id.toolbar);
        mToolbarLayout = (Toolbar) toolbar.findViewById(R.id.toolbar_layout);

        mSaveFAB = (FloatingActionButton) findViewById(R.id.floatingActionButton_save);
        mDisciplina = (EditText) findViewById(R.id.editText_disciplina);
        mProfessor = (EditText) findViewById(R.id.editText_professor);
        mEditTextM1 = (EditText) findViewById(R.id.editText_m1);
        mEditTextM2 = (EditText) findViewById(R.id.editText_m2);
        mEditTextB1 = (EditText) findViewById(R.id.editText_b1);
        mEditTextB2 = (EditText) findViewById(R.id.editText_b2);
        mEditTextMB1 = (EditText) findViewById(R.id.editText_mb1);
        mEditTextMB2 = (EditText) findViewById(R.id.editText_mb2);
        mEditTextMF = (EditText) findViewById(R.id.editText_mf);

        mSpinnerSemestre = (Spinner) findViewById(R.id.spinner);
    }

    private void loadDataUI(){

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CourseActivity.this,
                R.array.semester_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSemestre.setAdapter(adapter);

        mToolbarLayout.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolbarLayout);

        if( getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        if(intent != null && intent.hasExtra(Constants.CourseExtra.INTENT_EXTRA)){
            Bundle bundle = intent.getBundleExtra(Constants.CourseExtra.INTENT_EXTRA);
            if(bundle != null){
                Course course = (Course)bundle.getParcelable(Constants.CourseExtra.BUNDLE_EXTRA);
                if(course != null){

                    mDisciplina.setText(course.name);
                    mProfessor.setText(course.professor);
                    mSpinnerSemestre.setSelection(course.semester);
                    mEditTextM1.setText(String.valueOf(course.m1));
                    mEditTextM2.setText(String.valueOf(course.m2));
                    mEditTextB1.setText(String.valueOf(course.b1));
                    mEditTextB2.setText(String.valueOf(course.b2));
                    mEditTextMB1.setText(String.valueOf(course.mediaB1));
                    mEditTextMB2.setText(String.valueOf(course.mediaB2));
                    mEditTextMF.setText(String.valueOf(course.mediaFinal));
                }
            }
        }

    }

}
