package com.cercetare.infoschool;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cercetare.infoschool.model.CoursesArray;
import com.cercetare.infoschool.model.Week;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UtilService {
    public UtilService() {}

    public CoursesArray createCoursesArray(DataSnapshot dataSnapshot){
        CoursesArray restulCourseArr = new CoursesArray(dataSnapshot.getKey()
                , getlistWeek("1", dataSnapshot)
                , getlistWeek("2", dataSnapshot));

        return restulCourseArr;
    }

    public List<Week> getlistWeek(String semester, DataSnapshot dataSnapshot){
        dataSnapshot = dataSnapshot.child("sem" + semester).child("weeks");
        List<Week> resultList= new ArrayList<Week>();
        DataSnapshot ds;
        Week week;

        for(int i = 1; i <= dataSnapshot.getChildrenCount(); i++){
            ds = dataSnapshot.child(String.valueOf(i));
            week = new Week(Integer.parseInt(semester), i
                    , ds.child("weekSubject").getValue().toString()
                    , ds.child("mainSubject").getValue().toString()
                    , ds.child("startDate").getValue().toString()
                    , ds.child("endDate").getValue().toString());
            resultList.add(week);
        }
        return resultList;
    }



    public String[] weeksToList(List<Week> weeks){
        String[] resultWeeks = new String[weeks.size() + 1];
        resultWeeks[0] = "Selectează";

        for(Week week : weeks){
            resultWeeks[week.getNumber()] = "Săptămâna " + week.getNumber() + " ("
                    + getDateOfWeek(week.getStartDate()) + " -> "
                    + getDateOfWeek(week.getEndDate()) + ")";
        }

        return resultWeeks;
    }

    public void setSelectedWeekContent(Week week, TextView textViewSubject, TextView textViewMain){

//        Log.println(Log.ASSERT, "TEST-FUNC", week.toString());
//        Log.println(Log.ASSERT, "TEST-FUNC-SUBJ", week.getSubject());

        textViewSubject.setText(week.getSubject());
        textViewSubject.setVisibility(View.VISIBLE);
        textViewMain.setText(week.getMainSubject());
        textViewMain.setVisibility(View.VISIBLE);
    }

    public void stopVisible(TextView textViewSubject, TextView textViewMain){
        textViewSubject.setVisibility(View.INVISIBLE);
        textViewMain.setVisibility(View.INVISIBLE);
    }

    public Week setTextViewsForCurrentDate(long timestamp, CoursesArray coursesArray){
        Week week = new Week();
        for (Week wk : coursesArray.getSem1Weeks()){
            if (Long.parseLong(wk.getStartDate()) < timestamp && Long.parseLong(wk.getEndDate()) > timestamp){
                return wk;
            }
        }

        for (Week wk : coursesArray.getSem2Weeks()){
            if (Long.parseLong(wk.getStartDate()) < timestamp && Long.parseLong(wk.getEndDate()) > timestamp){
                return wk;
            }
        }
        return week;
    }


    public String getDateOfWeek(String str){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(str));
        int monthInt = cal.get(Calendar.MONTH) + 1;
        String month = monthInt > 9 ? String.valueOf(monthInt) : "0" + String.valueOf(monthInt);
        int dayInt = cal.get(Calendar.DAY_OF_MONTH);
        String day = dayInt > 9 ? String.valueOf(dayInt) : "0" + String.valueOf(dayInt);
        return "" + day + "/"+ month;
    }
}
