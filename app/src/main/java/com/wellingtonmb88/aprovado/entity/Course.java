package com.wellingtonmb88.aprovado.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Course extends RealmObject {

    @PrimaryKey
    private String id;
    private String name;
    private String professor;
    private int semester;
    private float m1;
    private float b1;
    private float mediaB1;
    private float m2;
    private float b2;
    private float mediaB2;
    private float mediaFinal;

    /**
     * A constructor that initializes the Course object
     **/
    public Course() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public float getM1() {
        return m1;
    }

    public void setM1(float m1) {
        this.m1 = m1;
    }

    public float getB1() {
        return b1;
    }

    public void setB1(float b1) {
        this.b1 = b1;
    }

    public float getMediaB1() {
        return mediaB1;
    }

    public void setMediaB1(float mediaB1) {
        this.mediaB1 = mediaB1;
    }

    public float getM2() {
        return m2;
    }

    public void setM2(float m2) {
        this.m2 = m2;
    }

    public float getB2() {
        return b2;
    }

    public void setB2(float b2) {
        this.b2 = b2;
    }

    public float getMediaB2() {
        return mediaB2;
    }

    public void setMediaB2(float mediaB2) {
        this.mediaB2 = mediaB2;
    }

    public float getMediaFinal() {
        return mediaFinal;
    }

    public void setMediaFinal(float mediaFinal) {
        this.mediaFinal = mediaFinal;
    }
}
