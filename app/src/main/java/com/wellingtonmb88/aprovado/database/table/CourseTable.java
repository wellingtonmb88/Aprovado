package com.wellingtonmb88.aprovado.database.table;

/**
 * Created by Wellington on 30/05/2015.
 */
public interface CourseTable {
    String TABLE_NAME = "course";

    String ID = "_id";

    String COURSE_NAME = "course_name";

    String COURSE_TEACHER= "course_teacher";

    String COURSE_SEMESTER= "course_semester";

    String COURSE_M1= "course_m1";

    String COURSE_M2 = "course_m2";

    String COURSE_B1 = "course_b1";

    String COURSE_B2 = "course_b2";
    
    String COURSE_MB1 = "course_mb1";

    String COURSE_MB2 = "course_mb2";

    String COURSE_MF = "course_mf";

    String WHERE_ID_EQUALS = "_id = ?";

    String[] ALL_COLUMNS = {ID, COURSE_NAME, COURSE_TEACHER, COURSE_SEMESTER, COURSE_M1,
            COURSE_B1, COURSE_MB1, COURSE_M2, COURSE_B2, COURSE_MB2, COURSE_MF };

    String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ( "
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
            + "," + COURSE_NAME + " TEXT "
            + "," + COURSE_TEACHER+ " TEXT "
            + "," + COURSE_SEMESTER+ " INTEGER "
            + "," + COURSE_M1 + " REAL "
            + "," + COURSE_B1 + " REAL "
            + "," + COURSE_MB1 + " REAL "
            + "," + COURSE_M2 + " REAL "
            + "," + COURSE_B2 + " REAL "
            + "," + COURSE_MB2 + " REAL "
            + "," + COURSE_MF + " REAL " + ")";

}
