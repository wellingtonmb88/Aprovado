package com.wellingtonmb88.aprovado.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.activity.CourseActivity;
import com.wellingtonmb88.aprovado.adapter.CourseRecyclerViewAdapter;
import com.wellingtonmb88.aprovado.custom.FloatActionButtonHideShow;
import com.wellingtonmb88.aprovado.entity.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wellington on 25/05/2015.
 */
public class CourseListFragment extends Fragment implements CourseRecyclerViewAdapter.RecyclerViewCallBack {
    private FloatingActionButton mAddCourseFAB;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.courses_list,container,false);


        mAddCourseFAB = (FloatingActionButton) v.findViewById(R.id.floatingActionButton_add);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        mAdapter = new CourseRecyclerViewAdapter(getActivity().getApplicationContext(), this, bulkInsert() );
        mRecyclerView.setAdapter(mAdapter);

        final FloatActionButtonHideShow fb = new FloatActionButtonHideShow(mAddCourseFAB);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Callback method to be invoked when the RecyclerView has been scrolled. This will be
             * called after the scroll has completed.
             *
             * @param recyclerView The RecyclerView which scrolled.
             * @param dx The amount of horizontal scroll.
             * @param dy The amount of vertical scroll.
             */
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if(dy > 0){
                    fb.hide();
                }else if(dy < 1){
                    fb.show();
                }
            }
        }) ;
        mAddCourseFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent  = new Intent(getActivity().getApplicationContext(),CourseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("disiclina", new Course());
                intent.putExtra("disciplinaExtra", bundle);

                startActivity(intent);
            }
        });

        return v;
    }

    private List<Course> bulkInsert(){
        List<Course> listCourses = new ArrayList<>();

        for(int i = 1 ; i < 20; i++){
            Course course = new Course();
            course.name = "nome"+i;
            course.professor = "professor"+i;
            if(i < 11){

                course.semester = i-1;
            }else{

                course.semester = 6;
            }
            course.m1 = i;
            course.b1 = i+5;
            course.m2 = i+10;
            course.b2 = i+15;

            listCourses.add(course);
        }

        return listCourses;
    }
    @Override
    public void Operation(String operation, Course course) {

        Intent intent  = new Intent(getActivity().getApplicationContext(),CourseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("disiclina", course);
        intent.putExtra("disciplinaExtra", bundle);

        startActivity(intent);
    }
}
