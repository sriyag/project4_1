package com.example.sriyag.teacherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    ListView lvCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Select subject class
        lvCourses = (ListView) findViewById(R.id.lvCourses);
        lvCourses.setOnItemClickListener(this);
    }

    public void onItemClick (AdapterView<?> parent, View view, int position, long id) {

        try {

            String subject = ((TextView)view).getText().toString();
            Intent intent = new Intent(MainActivity.this, SelectQuestionPaper.class);
            intent.putExtra("subject", subject);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "list view: " + e.getMessage(), Toast
                    .LENGTH_SHORT).show();
        }


    }
}
