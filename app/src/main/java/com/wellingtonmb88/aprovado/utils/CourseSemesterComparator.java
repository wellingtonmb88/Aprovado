package com.wellingtonmb88.aprovado.utils;

import com.wellingtonmb88.aprovado.entity.Course;

import java.util.Comparator;

public class CourseSemesterComparator implements Comparator<Course> {

    @Override
    public int compare(Course c, Course c1) {
        return c.semester - c1.semester;
    }
}
