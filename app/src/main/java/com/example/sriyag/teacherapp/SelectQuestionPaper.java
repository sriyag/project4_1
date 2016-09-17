package com.example.sriyag.teacherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by sriyag on 07/09/16.
 */
public class SelectQuestionPaper extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_question_paper);

        Button bAddQP = (Button) findViewById(R.id.bAddQP);
        ListView lvQPs = (ListView) findViewById(R.id.listViewQPs);
        String[] exams = new String[] {"T1", "T1_makeup", "T2", "Compre"};
        TextView tvCourseName = (TextView) findViewById(R.id.tvCourseName);

        final String subject = getIntent().getExtras().getString("subject");
        tvCourseName.setText(subject);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Fourth - the Array of data

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, exams);


        // Assign adapter to ListView
        lvQPs.setAdapter(adapter);
        lvQPs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*Toast.makeText(getApplicationContext(), "subj: " + subject + "test: " + adapter.getItem(position),
                        Toast.LENGTH_SHORT).show();*/
                try {
                    Intent intent = new Intent(SelectQuestionPaper.this, SeeQuestionPaper.class);
                    intent.putExtra("subject", subject);
                    intent.putExtra("test", adapter.getItem(position));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "seeqp error: " + e.getMessage(), Toast
                            .LENGTH_LONG).show();
                }
            }
        });

        /*

        // ListView Item Click Listener
            listView.setOnItemClickListener(new OnItemClickListener() {

                  @Override
                  public void onItemClick(AdapterView<?> parent, View view,
                     int position, long id) {

                   // ListView Clicked item index
                   int itemPosition     = position;

                   // ListView Clicked item value
                   String  itemValue    = (String) listView.getItemAtPosition(position);

                    // Show Alert
                    Toast.makeText(getApplicationContext(),
                      "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                      .show();

                  }

             });

         */

        bAddQP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //NOT YET MADE

                startActivity(new Intent(SelectQuestionPaper.this, CreateNewQuestionPaper.class));
            }
        });
    }
}
