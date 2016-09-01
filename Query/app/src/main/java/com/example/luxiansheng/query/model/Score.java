package com.example.luxiansheng.query.model;

import java.io.Serializable;

public class Score implements Serializable{
    private String Course;
    private String Score;
    private String CouseId;
    private String CourseProperty;
    private String Credit;
    private String GPA;
    private String RetestScore;

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public String getCourse() {
        return Course;
    }

    public void setCourse(String course) {
        Course = course;
    }

    public String getCouseId() {
        return CouseId;
    }

    public void setCouseId(String couseId) {
        CouseId = couseId;
    }

    public String getCourseProperty() {
        return CourseProperty;
    }

    public void setCourseProperty(String courseProperty) {
        CourseProperty = courseProperty;
    }

    public String getCredit() {
        return Credit;
    }

    public void setCredit(String credit) {
        Credit = credit;
    }

    public String getGPA() {
        return GPA;
    }

    public void setGPA(String GPA) {
        this.GPA = GPA;
    }

    public String getRetestScore() {
        return RetestScore;
    }

    public void setRetestScore(String retestScore) {
        this.RetestScore = retestScore;
    }
}
