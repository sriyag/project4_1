package com.example.sriyag.teacherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Select subject class
        Button bDstn = (Button) findViewById(R.id.bDstn);
        Button bOS = (Button) findViewById(R.id.bOS);
        Button bNet = (Button) findViewById(R.id.bNetworks);



    }

    public void onClick (View v) {

        if (v.getId() == R.id.bDstn || v.getId() == R.id.bOS || v.getId() == R.id.bNetworks) {

            Button b = (Button) v;
//            Toast.makeText(getApplicationContext(), b.getText().toString(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, SelectQuestionPaper.class);
            intent.putExtra("subject", b.getText().toString());
            startActivity(intent);

//            startActivity(new Intent(MainActivity.this, Test_Drag_View.class));

        }

    }
}
