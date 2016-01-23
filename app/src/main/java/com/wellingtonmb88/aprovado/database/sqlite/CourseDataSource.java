package com.wellingtonmb88.aprovado.database.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.wellingtonmb88.aprovado.database.sqlite.table.CourseTable;
import com.wellingtonmb88.aprovado.entity.CourseSQLite;

import java.util.ArrayList;
import java.util.List;

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


    public void insert(CourseSQLite course) {
        ContentValues values = new ContentValues();

        values.put(CourseTable.COURSE_NAME, course.name);
        values.put(CourseTable.COURSE_TEACHER, course.professor);
        values.put(CourseTable.COURSE_SEMESTER, course.semester);
        values.put(CourseTable.COURSE_M1, course.m1);
        values.put(CourseTable.COURSE_B1, course.b1);
        values.put(CourseTable.COURSE_MB1, course.mediaB1);
        values.put(CourseTable.COURSE_M2, course.m2);
        values.put(CourseTable.COURSE_B2, course.b2);
        values.put(CourseTable.COURSE_MB2, course.mediaB2);
        values.put(CourseTable.COURSE_MF, course.mediaFinal);

        database.insert(CourseTable.TABLE_NAME, null, values);
    }

    public void update(CourseSQLite course) {
        ContentValues values = new ContentValues();

        values.put(CourseTable.COURSE_NAME, course.name);
        values.put(CourseTable.COURSE_TEACHER, course.professor);
        values.put(CourseTable.COURSE_SEMESTER, course.semester);
        values.put(CourseTable.COURSE_M1, course.m1);
        values.put(CourseTable.COURSE_B1, course.b1);
        values.put(CourseTable.COURSE_MB1, course.mediaB1);
        values.put(CourseTable.COURSE_M2, course.m2);
        values.put(CourseTable.COURSE_B2, course.b2);
        values.put(CourseTable.COURSE_MB2, course.mediaB2);
        values.put(CourseTable.COURSE_MF, course.mediaFinal);

        database.update(CourseTable.TABLE_NAME, values, CourseTable.WHERE_ID_EQUALS, new String[]{String.valueOf(course.id)});
    }

    public void delete(CourseSQLite course) {
        long id = course.id;
        database.delete(CourseTable.TABLE_NAME, CourseTable.WHERE_ID_EQUALS, new String[]{String.valueOf(id)});
    }

    public List<CourseSQLite> getAllCourses() {
        List<CourseSQLite> courseList = new ArrayList<CourseSQLite>();

        Cursor cursor = database.query(CourseTable.TABLE_NAME,
                CourseTable.ALL_COLUMNS, null, null, null, null, null);

        if(cursor != null && cursor.moveToFirst()){

            do{
                CourseSQLite course = new CourseSQLite(cursor);
                courseList.add(course);
            }while (cursor.moveToNext());

            cursor.close();
        }

        return courseList;
    }
}
