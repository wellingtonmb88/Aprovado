package com.wellingtonmb88.aprovado.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.wellingtonmb88.aprovado.database.sqlite.table.CourseTable;

public class CourseSQLite implements Parcelable {

    public long id;
    public String name;
    public String professor;
    public int semester;
    public float m1;
    public float b1;
    public float mediaB1;
    public float m2;
    public float b2;
    public float mediaB2;
    public float mediaFinal;

    /**
     * A constructor that initializes the Course object
     **/
    public CourseSQLite(){}

    public CourseSQLite(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(CourseTable.ID));
        this.name = cursor.getString(cursor.getColumnIndex(CourseTable.COURSE_NAME));
        this.professor = cursor.getString(cursor.getColumnIndex(CourseTable.COURSE_TEACHER));
        this.semester = cursor.getInt(cursor.getColumnIndex(CourseTable.COURSE_SEMESTER));
        this.m1 = cursor.getFloat(cursor.getColumnIndex(CourseTable.COURSE_M1));
        this.b1 = cursor.getFloat(cursor.getColumnIndex(CourseTable.COURSE_B1));
        this.mediaB1 = cursor.getFloat(cursor.getColumnIndex(CourseTable.COURSE_MB1));
        this.m2 = cursor.getFloat(cursor.getColumnIndex(CourseTable.COURSE_M2));
        this.b2 = cursor.getFloat(cursor.getColumnIndex(CourseTable.COURSE_B2));
        this.mediaB2 = cursor.getFloat(cursor.getColumnIndex(CourseTable.COURSE_MB2));
        this.mediaFinal = cursor.getFloat(cursor.getColumnIndex(CourseTable.COURSE_MF));
    }


    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Storing the Course data to Parcel object
     **/
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(professor);
        dest.writeInt(semester);
        dest.writeFloat(m1);
        dest.writeFloat(b1);
        dest.writeFloat(mediaB1);
        dest.writeFloat(m2);
        dest.writeFloat(b2);
        dest.writeFloat(mediaB2);
        dest.writeFloat(mediaFinal);
    }

    /**
     * Retrieving Course data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    private CourseSQLite(Parcel in){
        this.id = in.readLong();
        this.name = in.readString();
        this.professor = in.readString();
        this.semester = in.readInt();
        this.m1 = in.readFloat();
        this.b1 = in.readFloat();
        this.mediaB1 = in.readFloat();
        this.m2 = in.readFloat();
        this.b2 = in.readFloat();
        this.mediaB2 = in.readFloat();
        this.mediaFinal = in.readFloat();
    }

    public static final Parcelable.Creator<CourseSQLite> CREATOR = new Parcelable.Creator<CourseSQLite>() {

        @Override
        public CourseSQLite createFromParcel(Parcel source) {
            return new CourseSQLite(source);
        }

        @Override
        public CourseSQLite[] newArray(int size) {
            return new CourseSQLite[size];
        }
    };


}
