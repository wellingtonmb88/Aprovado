package com.wellingtonmb88.aprovado.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.custom.FloatActionButtonHideShow;

/**
 * Created by Wellington on 26/05/2015.
 */
public class CourseActivity extends AppCompatActivity {

    FloatingActionButton mSaveFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        mSaveFAB = (FloatingActionButton) findViewById(R.id.floatingActionButton_save);

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

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.semester_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        mSaveFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FloatActionButtonHideShow fb = new FloatActionButtonHideShow(mSaveFAB);
                fb.hide();
            }
        });

    }

}
