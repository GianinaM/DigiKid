package com.cercetare.infoschool;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cercetare.infoschool.model.Week;
import com.cercetare.infoschool.ui.login.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
//TODO Geometrie cls VIII
public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    ArrayList<Week> weeksList = new ArrayList<>();
    boolean logged = false;

    public static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Log.i("MainActivity", "BottomNavigationView - navigation_home");
                    setContentView(R.layout.activity_select_class);
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mTextMessage = (TextView) findViewById(R.id.message);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        get_from_json();
        configureButton();

    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (!logged) {
//            startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), Constants.TO_LOGIN);
//        }
    }

    //    @Override
//    protected void onStart(Bundle savedInstanceState) {
//        startActivity(new Intent(MainActivity.this, LoginActivity.class));
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Constants.BACK_FROM_LOGIN == resultCode || resultCode == Constants.BACK_FROM_GOOGLE_LOGIN) {
            logged = true;
            configureButton();
            configureContent();
            if (resultCode == Constants.BACK_FROM_GOOGLE_LOGIN){
                Log.println(Log.ASSERT, "USER", data.getStringExtra("userName"));
            }
        }
    }

    public void configureContent(){
        viewToast("Back from Login");
    }
    private void configureButton() {
        Button nextActiv = (Button) findViewById(R.id.five_class_button);
        nextActiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, ClassActivity.class), Constants.TO_CLASS);
            }
        });
    }

    public void get_from_json() {
        String json = null;
        try {
            InputStream is = getAssets().open("courses.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            JSONObject obj = new JSONObject(json);
            JSONArray courses = obj.getJSONArray("classes");

            for (int i = 0; i < courses.length(); i++) {
                JSONObject course = courses.getJSONObject(i);

                String auxName = course.getString("name");
                String nameOfCourse = auxName.substring(0,1).toUpperCase() + auxName.substring(1);
                JSONArray classes = course.getJSONArray("values");

                for (int j = 0; j < classes.length(); j++) {
                    JSONObject classObj = classes.getJSONObject(j);

                    String classNumber = classObj.getString("id");
                    JSONObject classValue = classObj.getJSONObject("value");

                    JSONArray weeks = classValue.getJSONArray("weeks");

                    for (int k = 0; k < weeks.length(); k++) {
                        JSONObject weeksJSON = weeks.getJSONObject(k);
                        Week week = new Week();
                        if (weeksJSON.has("sem") && weeksJSON.has("main")) {
                            week = new Week(weeksJSON.getInt("sem"), weeksJSON.getInt("id")
                                    , weeksJSON.getString("value"), weeksJSON.getString("main")
                                    , weeksJSON.getString("startDate"), weeksJSON.getString("endDate"));
                        }
                        if (weeksJSON.has("sem") && !weeksJSON.has("main")) {
                            week = new Week(weeksJSON.getInt("sem"), weeksJSON.getInt("id")
                                    , weeksJSON.getString("value"), ""
                                    , weeksJSON.getString("startDate")
                                    , weeksJSON.getString("endDate"));
                        }
                        DatabaseReference databaseReference = mDatabase.child("classes").child(classNumber).child(nameOfCourse)
                                .child("sem" + week.getSemester()).child("weeks")
                                .child("" + week.getNumber());
                        databaseReference.child("weekSubject").setValue(week.getSubject());
                        if (week.getMainSubject() != null) {
                            databaseReference.child("mainSubject").setValue(week.getMainSubject());
                        }
                        databaseReference.child("startDate").setValue(getTimestamp(week.getStartDate()));
                        databaseReference.child("endDate").setValue(getTimestamp(week.getEndDate()));
                    }
//                    Log.println(Log.DEBUG, "readAndWrite", classObj.getJSONObject("value").toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTimestamp(String perfectDate){

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(perfectDate);
            return  String.valueOf((parsedDate.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void viewToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
