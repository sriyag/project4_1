package com.example.sriyag.teacherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by sriyag on 07/09/16.
 */
public class AddQuestion extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_question);



        final EditText etQs = (EditText) findViewById(R.id.etQs);

        final Spinner chooseTag = (Spinner) findViewById(R.id.chooseTag);
        //chooseTag.setPrompt("Choose Question Tag");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tags, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        chooseTag.setAdapter(adapter);
        chooseTag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                if (pos == 0) {
                    /*Toast.makeText(getApplicationContext(), "Please select a question tag", Toast
                            .LENGTH_SHORT).show();*/
                }
                else {

                }
            }


            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });


        Button bAddQs = (Button) findViewById(R.id.bAddQs);
        bAddQs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(etQs.getText().toString().equals("")) && chooseTag.getSelectedItemPosition
                        () != 0) {

                    //app crash:
                    //startActivity(new Intent(AddQuestion.this, SeeQuestionPaper.class));

                    Intent i = new Intent(AddQuestion.this, SeeQuestionPaper.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }

                else {
                    if (etQs.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please enter the question.", Toast
                                .LENGTH_SHORT).show();
                    }

                    if (chooseTag.getSelectedItemPosition() == 0) {
                        Toast.makeText(getApplicationContext(), "Please select a question tag", Toast
                                .LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
