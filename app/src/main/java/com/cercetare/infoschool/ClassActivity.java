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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Gianina on 5/5/2019.
 */

public class ClassActivity extends AppCompatActivity implements AdapterView.OnClickListener{

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Log.i("ClassActivity", "BottomNavigationView - navigation_home");
                    setResult(Constants.BACK_FROM_CLASS);
                    finish();
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
        setContentView(R.layout.activity_select_class);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        configureBack();
        configureButtons();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.println(Log.ASSERT, "TEST-ACT-CL", String.valueOf(resultCode));

        configureContent();
    }

    public void configureContent(){
        viewToast("Back from Courses");
    }

    private void configureBack() {
        final Intent intent = new Intent(ClassActivity.this, CourseActivity.class);

        Button fiveActiv = (Button) findViewById(R.id.five_class_button);
        fiveActiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("classNumber", "5");
                startActivityForResult(intent, Constants.TO_COURSE);
            }
        });

        Button sixActiv = (Button) findViewById(R.id.six_class_button);
        sixActiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("classNumber", "6");
                startActivityForResult(intent, Constants.TO_COURSE);
            }
        });

        Button sevenActiv = (Button) findViewById(R.id.seven_class_button);
        sevenActiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("classNumber", "7");
                startActivityForResult(intent, Constants.TO_COURSE);
            }
        });

        Button eightActiv = (Button) findViewById(R.id.eight_class_button);
        eightActiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("classNumber", "8");
                startActivityForResult(intent, Constants.TO_COURSE);
            }
        });
    }

    private void configureButtons() {


    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.five_class_button) {
            startActivity(new Intent(ClassActivity.this, MainActivity.class));
        }
    }

    public void viewToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
