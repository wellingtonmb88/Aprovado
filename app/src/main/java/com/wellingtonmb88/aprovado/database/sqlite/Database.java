package com.wellingtonmb88.aprovado.database.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wellingtonmb88.aprovado.database.sqlite.table.CourseTable;

public class Database extends SQLiteOpenHelper {

    public final static String DB_NAME = "aprovado.db";
    public final static SQLiteDatabase.CursorFactory CURSOR_FACTORY = null;
    public final static int VERSION = 1;

    public Database(Context context) {
        super(context, DB_NAME, CURSOR_FACTORY, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CourseTable.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//        if ((oldVersion == 1) && (newVersion == 2)) {
//            //Update DataBase
//        }
    }

}
