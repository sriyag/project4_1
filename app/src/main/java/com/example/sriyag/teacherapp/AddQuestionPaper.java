package com.example.sriyag.teacherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by sriyag on 07/09/16.
 */
public class AddQuestionPaper extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_question_paper);

        Button bAddAQs = (Button) findViewById(R.id.bAddAQs);
        Button bSetQP = (Button) findViewById(R.id.bSetQP);
        final EditText etQPTitle = (EditText) findViewById(R.id.etQPTitle);

        bAddAQs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etQPTitle.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter the Question Title",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(new Intent(AddQuestionPaper.this, AddQuestion.class));
                }
            }
        });

        bSetQP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etQPTitle.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter the Question Title",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(new Intent(AddQuestionPaper.this, QuestionPaper.class));
                }
            }
        });
    }
}
