package com.wellingtonmb88.aprovado.utils;

/**
 * Created by Wellington on 30/05/2015.
 */
public interface Constants {

     interface CourseExtra{
        String INTENT_EXTRA = "disciplinaIntent";
        String BUNDLE_EXTRA = "disciplinaBundle";
    }

    interface CourseDatabaseAction{
        String INSERT_COURSE = "insert_course";
        String UPDATE_COURSE = "update_course";
        String DELETE_COURSE = "delete_course";
        String GET_ALL_COURSES = "get_all_courses";
        String GET_COURSE = "get_course";
    }
}
