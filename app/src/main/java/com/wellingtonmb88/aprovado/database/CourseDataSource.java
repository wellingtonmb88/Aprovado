package com.wellingtonmb88.aprovado.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.wellingtonmb88.aprovado.database.table.CourseTable;
import com.wellingtonmb88.aprovado.entity.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wellington on 30/05/2015.
 */
public class CourseDataSource {

    private SQLiteDatabase database;
    private Database dbHelper;

    public CourseDataSource(Context context){
        dbHelper = new Database(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    public void insert(Course course) {
        ContentValues values = new ContentValues();

        values.put(CourseTable.COURSE_NAME, course.name);
        values.put(CourseTable.COURSE_TEACHER, course.professor);
        values.put(CourseTable.COURSE_M1, course.m1);
        values.put(CourseTable.COURSE_B1, course.b1);
        values.put(CourseTable.COURSE_MB1, course.mediaB1);
        values.put(CourseTable.COURSE_M2, course.m2);
        values.put(CourseTable.COURSE_B2, course.b2);
        values.put(CourseTable.COURSE_MB2, course.mediaB2);
        values.put(CourseTable.COURSE_MF, course.mediaFinal);

        database.insert(CourseTable.TABLE_NAME, null, values);
    }

    public void update(Course course) {
        ContentValues values = new ContentValues();

        values.put(CourseTable.COURSE_NAME, course.name);
        values.put(CourseTable.COURSE_TEACHER, course.professor);
        values.put(CourseTable.COURSE_M1, course.m1);
        values.put(CourseTable.COURSE_B1, course.b1);
        values.put(CourseTable.COURSE_MB1, course.mediaB1);
        values.put(CourseTable.COURSE_M2, course.m2);
        values.put(CourseTable.COURSE_B2, course.b2);
        values.put(CourseTable.COURSE_MB2, course.mediaB2);
        values.put(CourseTable.COURSE_MF, course.mediaFinal);

        database.update(CourseTable.TABLE_NAME, values, CourseTable.WHERE_ID_EQUALS, new String[]{String.valueOf(course.id)});
    }

    public void delete(Course course) {
        long id = course.id;
        database.delete(CourseTable.TABLE_NAME, CourseTable.WHERE_ID_EQUALS, new String[]{String.valueOf(id)});
    }

    public List<Course> getAllCourses() {
        List<Course> courseList = new ArrayList<Course>();

        Cursor cursor = database.query(CourseTable.TABLE_NAME,
                CourseTable.ALL_COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Course course = new Course(cursor);
            courseList.add(course);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return courseList;
    }

    public Course getCourse(long id) {

        Cursor cursor = database.query(CourseTable.TABLE_NAME,
                CourseTable.ALL_COLUMNS, CourseTable.WHERE_ID_EQUALS, new String[]{String.valueOf(id)}, null, null, null);

        cursor.moveToFirst();
        Course course = new Course(cursor);
        cursor.close();

        return course;
    }
}
