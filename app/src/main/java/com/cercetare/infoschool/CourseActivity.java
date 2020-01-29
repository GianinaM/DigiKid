package com.cercetare.infoschool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.flags.impl.DataUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Gianina on 5/5/2019.
 */

public class CourseActivity extends AppCompatActivity {

    private TextView mTextMessage;
    public static DatabaseReference refCourseDB = FirebaseDatabase.getInstance().getReference();
    int coursesNumber;
    String classNumber;
    String[] coursesItems;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Log.i("CourseActivity", "BottomNavigationView - navigation_home");
                    setResult(Constants.BACK_FROM_COURSE);
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

    Context context;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_course);
        context=this;

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //////////List
//
//        lstview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
////                TextView txt = (TextView) view;
//                viewToast("Line " + position + "has been clicked");
//            }
//        });

        configureNextForActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.println(Log.ASSERT, "TEST-ACT", String.valueOf(resultCode));

        configureNextForActivity();
    }

    public void configureNextForActivity(){
        final Intent prevIntent = getIntent();
        classNumber = prevIntent.getStringExtra("classNumber");

        refCourseDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListView listView = (ListView)findViewById(R.id.listv);
                DataSnapshot resultFromDB = dataSnapshot.child("classes").child(classNumber);
                coursesNumber = (int) resultFromDB.getChildrenCount();
                coursesItems = new String[coursesNumber];

                Iterator<DataSnapshot> it = resultFromDB.getChildren().iterator();
                int i = 0;
                while (it.hasNext()) {
                    coursesItems[i] = it.next().getKey();
                    i++;
                }
                LstViewAdapter adapter = new LstViewAdapter(context,R.layout.list_item_course,R.id.course,coursesItems);
                // Bind data to the ListView
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void clickMe(View view){
        TextView textView = (TextView)view;
        viewToast(textView.getText().toString());

        Intent newIntent = new Intent(CourseActivity.this, GetInfoActivity.class);
        newIntent.putExtra("classNumber", classNumber);
        newIntent.putExtra("courseName", textView.getText().toString());
        startActivityForResult(newIntent, Constants.TO_GET_INFO);
    }

    public void beCute(View view) {
        LinearLayout linearLayout = (LinearLayout) view;
        clickMe(linearLayout.getChildAt(0));
    }

    public void viewToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
