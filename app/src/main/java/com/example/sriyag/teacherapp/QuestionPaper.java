package com.example.sriyag.teacherapp;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by sriyag on 10/09/16.
 */
public class QuestionPaper extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_paper);

        final String subject = getIntent().getExtras().getString("subject");
        final String test = getIntent().getExtras().getString("test");
    }
}
