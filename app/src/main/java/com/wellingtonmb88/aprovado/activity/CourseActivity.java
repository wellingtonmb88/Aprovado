package com.wellingtonmb88.aprovado.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.custom.FloatActionButtonHideShow;
import com.wellingtonmb88.aprovado.entity.Course;

/**
 * Created by Wellington on 26/05/2015.
 */
public class CourseActivity extends AppCompatActivity {

    private FloatingActionButton mSaveFAB;
    private EditText mDisciplina;
    private EditText mProfessor;
    private Spinner mSemestre;

    private EditText mM1;
    private EditText mM2;
    private EditText mB1;
    private EditText mB2;
    private EditText mMB1;
    private EditText mMB2;
    private EditText mMF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        mSaveFAB = (FloatingActionButton) findViewById(R.id.floatingActionButton_save);

        mDisciplina = (EditText) findViewById(R.id.editText_disciplina);
        mProfessor = (EditText) findViewById(R.id.editText_professor);

        mM1 = (EditText) findViewById(R.id.editText_m1);
        mM2 = (EditText) findViewById(R.id.editText_m2);
        mB1 = (EditText) findViewById(R.id.editText_b1);
        mB2 = (EditText) findViewById(R.id.editText_b2);
        mMB1 = (EditText) findViewById(R.id.editText_mb1);
        mMB2 = (EditText) findViewById(R.id.editText_mb2);
        mMF = (EditText) findViewById(R.id.editText_mf);

        FrameLayout toolbar = (FrameLayout) findViewById(R.id.toolbar);
        Toolbar toolbarLayout = (Toolbar) toolbar.findViewById(R.id.toolbar_layout); // Attaching the layout to the toolbar object

        toolbarLayout.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbarLayout);

        if( getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mSemestre = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.semester_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSemestre.setAdapter(adapter);

        Intent intent = getIntent();

       Course course = (Course)intent.getBundleExtra("disciplinaExtra").getSerializable("disiclina");

        if(course != null){

            mDisciplina.setText(course.name);
            mProfessor.setText(course.professor);
            mSemestre.setSelection(course.semester);
            mM1.setText(String.valueOf(course.m1));
            mM2.setText(String.valueOf(course.m2));
            mB1.setText(String.valueOf(course.b1));
            mB2.setText(String.valueOf(course.b2));
            mMB1.setText(String.valueOf(course.mediaB1));
            mMB2.setText(String.valueOf(course.mediaB2));
            mMF.setText(String.valueOf(course.mediaFinal));
        }


    }

}
