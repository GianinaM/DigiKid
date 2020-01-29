package com.cercetare.infoschool.model;

import java.util.HashMap;
import java.util.List;

public class CoursesArray {

    String name;
    List<Week> sem1Weeks;
    List<Week> sem2Weeks;

    public CoursesArray(String name) {
        this.name = name;
    }


    public CoursesArray(String name, List<Week> sem1Weeks, List<Week> sem2Weeks){
        this.name = name;
        this.sem1Weeks = sem1Weeks;
        this.sem2Weeks = sem2Weeks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Week> getSem1Weeks() {
        return sem1Weeks;
    }

    public void setSem1Weeks(List<Week> sem1Weeks) {
        this.sem1Weeks = sem1Weeks;
    }

    public List<Week> getSem2Weeks() {
        return sem2Weeks;
    }

    public void setSem2Weeks(List<Week> sem2Weeks) {
        this.sem2Weeks = sem2Weeks;
    }
}
