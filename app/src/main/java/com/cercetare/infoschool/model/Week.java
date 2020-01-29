package com.cercetare.infoschool.model;

/**
 * Created by Gianina on 5/6/2019.
 */

public class Week {

    int semester;
    int number;
    String subject;
    String mainSubject;
    String startDate;
    String endDate;

    public Week(){};

    public Week(int semester, int number, String subject, String startDate, String endDate) {
        this.number = number;
        this.subject = subject;
        this.semester = semester;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public Week(int semester, int number, String subject, String mainSubject) {
        this.number = number;
        this.subject = subject;
        this.semester = semester;
        this.mainSubject = mainSubject;
    }

    public Week(int semester, int number, String subject, String mainSubject, String startDate, String endDate) {
        this.number = number;
        this.subject = subject;
        this.semester = semester;
        this.mainSubject = mainSubject;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMainSubject() {
        return mainSubject;
    }

    public void setMainSubject(String mainSubject) {
        this.mainSubject = mainSubject;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Semester: " + number + " -- number: " + semester + " -- subject: " + subject;
    }
}
