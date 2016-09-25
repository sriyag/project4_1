package com.example.sriyag.teacherapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;

/**
 * Created by sriyag on 25/09/16.
 */
public class ZipQuestionPaper extends Activity {

    TextView tv;
    Button b;
    EditText et;
    String subject, exam, password, zipFileName;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zip_question_paper);

        ActionBar ab = getActionBar();

        subject = getIntent().getExtras().getString("subject");
        exam = getIntent().getExtras().getString("exam");

        tv = (TextView) findViewById(R.id.tvCourseExam_ZipClass);
        b = (Button) findViewById(R.id.btnZipQP);
        et = (EditText) findViewById(R.id.etSetPassword);

        tv.setText(subject + " - " + exam);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                password = et.getText().toString();

                if (password.trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter a password!", Toast
                            .LENGTH_SHORT).show();
                }

                else {

                    //creating a encrypted-zip of the xml file and going to the email-screen.
                    AddFilesWithAESEncryption addFilesWithAESEncryption = new AddFilesWithAESEncryption();

                    Toast.makeText(getApplicationContext(), "Question paper zipped.\nTo implement " +
                                    "pushing to server functionality",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //go back
        intent = new Intent(ZipQuestionPaper.this, SelectQuestionPaper.class);
        intent.putExtra("subject", subject);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle "up" button behavior here.
            startActivity(intent);
            return true;
        } else {
            // handle other items here
        }
        // return true if you handled the button click, otherwise return false.
        return true;
    }

    class AddFilesWithAESEncryption {

        public AddFilesWithAESEncryption()
        {
            /*String filePath = Environment.getExternalStorageDirectory() + "/" + subject + "_" + exam
                    + "_" + "questionpaper" ;*/

            String filePath = Environment.getExternalStorageDirectory() + "/" + "datastorage_t1_" +
                    "questionpaper" + ".xml" ;
            zipFileName = Environment.getExternalStorageDirectory() + "/" +
                    "datastorage_t1_questionpaper_final" + ".zip" ;
            File file = new File(filePath) ;
            if(file.exists())
            {
                try {
                    ZipFile zipFile = new ZipFile(zipFileName);
                    ZipParameters parameters = new ZipParameters();
                    parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // set compression method to deflate compression
                    parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
                    parameters.setEncryptFiles(true);
                    parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
                    parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
                    parameters.setPassword(password);
                    zipFile.addFile(file, parameters);
                } catch (ZipException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage() + " - zip error msg",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    } //class ...Encryption closed
}
