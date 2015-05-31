package com.wellingtonmb88.aprovado.async;

import android.content.Context;
import android.os.AsyncTask;

import com.wellingtonmb88.aprovado.database.CourseDataSource;
import com.wellingtonmb88.aprovado.entity.Course;
import com.wellingtonmb88.aprovado.utils.Constants;

import java.util.List;

/**
 * Created by Wellington on 30/05/2015.
 */
public class SQliteAsyncTask extends AsyncTask<String, Void, List<Course> > {

    private SQliteCallBack mListener;
    private CourseDataSource mCourseDataSource;
    private Context mContext;
    private Course mCourse;

    public SQliteAsyncTask(Context context, SQliteCallBack listener, Course course){
        mContext = context;
        mListener = listener;
        mCourse = course;
        mCourseDataSource = new CourseDataSource(context);
    }

    @Override
    protected List<Course>  doInBackground(String... act) {
        String action = act[0];
        mCourseDataSource.open();

        if(mCourse != null && Constants.CourseDatabaseAction.INSERT_COURSE.equals(action)){
            mCourseDataSource.insert(mCourse);
        }else if(mCourse != null && Constants.CourseDatabaseAction.UPDATE_COURSE.equals(action)){
            mCourseDataSource.update(mCourse);
        }else if(mCourse != null && Constants.CourseDatabaseAction.DELETE_COURSE.equals(action)){
            mCourseDataSource.delete(mCourse);
        }else if(Constants.CourseDatabaseAction.GET_ALL_COURSES.equals(action)){
            return mCourseDataSource.getAllCourses();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Course>  courseList) {
        super.onPostExecute(courseList);

        if(courseList != null && !courseList.isEmpty() && mListener != null){
            mListener.getAllCourses(courseList);
        }
        mCourseDataSource.close();
    }

    public interface SQliteCallBack{
        void getAllCourses(List<Course> courseList);
    }
}
