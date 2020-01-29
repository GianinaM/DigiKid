package com.cercetare.infoschool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cercetare.infoschool.model.CoursesArray;
import com.cercetare.infoschool.model.Week;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import static java.sql.Types.TIMESTAMP;

public class GetInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    final static String SEM1 = "Semestrul 1";
    final static String SEM2 = "Semestrul 2";
    private String classNumber;
    private String courseName;
    public CoursesArray currentCourses;
    UtilService utilService = new UtilService();
    private static final String[] semesters = {"Select", SEM1, SEM2};
    public List<Week> currentWeeks;
    public String[] weeksItems;
    Spinner semSpinner, weekSpinner;
    Button currentDateButton;
    Context context;
    public static DatabaseReference refInfosDB;

    int i = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Log.println(Log.ASSERT,"GET-INFO", "BottomNavigationView - navigation_home");
                    setResult(Constants.BACK_FROM_GET_INFO);
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                    viewToast(String.valueOf(R.string.title_dashboard));
                    return true;
                case R.id.navigation_notifications:
                    viewToast(String.valueOf(R.string.title_notifications));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_get_info);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        final Intent prevIntent = getIntent();
        classNumber = prevIntent.getStringExtra("classNumber");
        courseName = prevIntent.getStringExtra("courseName");
        refInfosDB = FirebaseDatabase.getInstance().getReference().child("classes").child(classNumber).child(courseName);

        semSpinner = (Spinner)findViewById(R.id.spinnerSems);
        weekSpinner = (Spinner)findViewById(R.id.spinnerWeeks);
        configureDropdownSems();

        refInfosDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentCourses = utilService.createCoursesArray(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        currentDateButton = (Button) findViewById(R.id.current_date_button);
        currentDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                Log.println(Log.ASSERT, "DATE-CURRENT", String.valueOf(timestamp.getTime()));
                Log.println(Log.ASSERT, "CURRENT-COURSE", currentCourses.getName());
                Week week = utilService.setTextViewsForCurrentDate(timestamp.getTime(), currentCourses);

        Log.println(Log.ASSERT, "TEST-FUNC-WEEK", week == null ? "da" : week.toString());
                utilService.setSelectedWeekContent(week
                        , (TextView) findViewById(R.id.result_search)
                        , (TextView) findViewById(R.id.main_result_search));
            }
        });
    }

    private void configureDropdownSems() {
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(GetInfoActivity.this,
                android.R.layout.simple_spinner_item, semesters);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semSpinner.setAdapter(adapter);
        semSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tw = (TextView)view;
                String selectedItem = tw.getText().toString();
                if(selectedItem.equals(SEM1) || selectedItem.equals(SEM2)){
                    viewToast(selectedItem);
                    configureDropdownWeeks(selectedItem);
                } else {
                    TextView textViewSelect = (TextView) findViewById(R.id.select_week);
                    textViewSelect.setVisibility(View.INVISIBLE);
                    Spinner otherSpinner = (Spinner)findViewById(R.id.spinnerWeeks);
                    otherSpinner.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void configureDropdownWeeks(String semesterItem) {

        TextView textViewSelect = (TextView) findViewById(R.id.select_week);
        textViewSelect.setVisibility(View.VISIBLE);

        if (semesterItem.equals(SEM1)){
            currentWeeks = currentCourses.getSem1Weeks();
        }
        if (semesterItem.equals(SEM2)){
            currentWeeks = currentCourses.getSem2Weeks();
        }

        weeksItems = utilService.weeksToList(currentWeeks);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(GetInfoActivity.this,
                android.R.layout.simple_spinner_item, weeksItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekSpinner.setAdapter(adapter);
        weekSpinner.setVisibility(View.VISIBLE);
        weekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewToast(String.valueOf( parent.getItemIdAtPosition(position)));
                if ((int)parent.getItemIdAtPosition(position) > 0) {
                    utilService.setSelectedWeekContent(currentWeeks.get((int) parent.getItemIdAtPosition(position) - 1)
                            , (TextView) findViewById(R.id.result_search)
                            , (TextView) findViewById(R.id.main_result_search));
                } else {
                    utilService.stopVisible((TextView) findViewById(R.id.result_search)
                            , (TextView) findViewById(R.id.main_result_search));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void viewToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
