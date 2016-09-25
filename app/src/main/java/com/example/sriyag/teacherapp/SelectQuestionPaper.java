package com.example.sriyag.teacherapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
public class SelectQuestionPaper extends Activity implements AdapterView.OnItemLongClickListener,
        AdapterView.OnCreateContextMenuListener {

    ListView lvQPs;
    ArrayAdapter<String> adapter;
    String subject, exam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_question_paper);

        Button bAddQP = (Button) findViewById(R.id.bAddQP);
        lvQPs = (ListView) findViewById(R.id.listViewQPs);
        String[] exams = new String[] {"T1", "T1_makeup", "T2", "Compre"};
        TextView tvCourseName = (TextView) findViewById(R.id.tvCourseName);

        subject = getIntent().getExtras().getString("subject"); //app crash on
        // pressing back button
        tvCourseName.setText(subject);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Fourth - the Array of data

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, exams);


        // Assign adapter to ListView
        lvQPs.setAdapter(adapter);
        lvQPs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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

        lvQPs.setBackgroundColor(Color.BLACK);

        lvQPs.setLongClickable(true);
        lvQPs.setOnItemLongClickListener(this);

        bAddQP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //NOT YET MADE
                startActivity(new Intent(SelectQuestionPaper.this, CreateNewQuestionPaper.class));
            }
        });

    } //onCreate closed

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                   int pos, long id) {
        //pop up context menu

        exam = adapter.getItem(pos);

        registerForContextMenu(lvQPs);
        openContextMenu(lvQPs); //important to make the context menu visible!!

        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.export_qp_menu, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        switch (item.getItemId()) {

            case R.id.export_qp:

                //go to new class where you can zip question paper and push to server
                try {
                    Intent iz = new Intent(SelectQuestionPaper.this, ZipQuestionPaper.class);
                    iz.putExtra("subject", subject);
                    iz.putExtra("exam", exam);
                    startActivity(iz);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "zip error: " + e.getMessage(), Toast
                            .LENGTH_SHORT).show();
                }

                return true;

            default:
                return super.onContextItemSelected(item);
        }

    }
}
