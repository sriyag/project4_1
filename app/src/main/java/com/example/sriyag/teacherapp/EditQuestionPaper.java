package com.example.sriyag.teacherapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by sriyag on 07/09/16.
 */
public class EditQuestionPaper extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_question_paper);

        Button bAddQs = (Button) findViewById(R.id.bAddQs);
        Button bDelQs = (Button) findViewById(R.id.bDelQs);
        Button bEditQs = (Button) findViewById(R.id.bEditQs);
        Button bChngTag = (Button) findViewById(R.id.bChngTag);

        bAddQs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditQuestionPaper.this, AddQuestion.class));
            }
        });

        bDelQs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //spinner to choose question number
                AlertDialog.Builder builder = new AlertDialog.Builder(EditQuestionPaper.this);
                builder.setTitle("Choose question to delete");
                builder.setItems(R.array.questions_list, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        Toast.makeText(getApplicationContext(), "Question deleted: Q" +
                                (item + 1), Toast.LENGTH_SHORT).show();
                        //mDoneButton.setText(items[item]);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
            });

        bEditQs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    //first choose the question number, based on its tag, load appropriate fragment
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditQuestionPaper.this);
                    builder.setTitle("Choose question to edit");
                    builder.setItems(R.array.questions_list, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            // Do something with the selection
                            Toast.makeText(getApplicationContext(), "Question to edit: Q" +
                                    (item + 1), Toast.LENGTH_SHORT).show();
                            //go to edit question page!!
                            AlertDialog.Builder builder = new AlertDialog.Builder(EditQuestionPaper.this);
                            builder.setTitle("Edit question");

                            EditText et = new EditText(getApplicationContext());
                            et.setVisibility(View.VISIBLE);
                            et.setHint("Enter question content");
                            et.setHeight(150);
                            et.setTextColor(Color.BLACK);
                            String content = et.getText().toString();
                            et.setText(content);
                            Button b = new Button(getApplicationContext());
                            b.setText("Save");
                            b.setHeight(40);
                            b.setWidth(80);

                            LinearLayout ll=new LinearLayout(getApplicationContext());
                            ll.setOrientation(LinearLayout.VERTICAL);
                            ll.addView(et);
                            ll.addView(b);
                            builder.setView(ll);


                            /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);*/
                            final AlertDialog alert = builder.create();
                            alert.show();


                            b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //close alert dialog box
                                    alert.dismiss();
                                }
                            });



                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        bChngTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //choose question number, choose tag spinner thereafter
                AlertDialog.Builder builder = new AlertDialog.Builder(EditQuestionPaper.this);
                builder.setTitle("Choose question: ");
                builder.setItems(R.array.questions_list, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        Toast.makeText(getApplicationContext(), "Question selected: Q" +
                                (item + 1), Toast.LENGTH_SHORT).show();
                        //choose new tag
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditQuestionPaper
                                .this);
                        builder.setTitle("Select new tag");
                        builder.setItems(R.array.tag, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {

                                /*try {
                                    // Do something with the selection
                                    ListView lw = ((AlertDialog) dialog).getListView();
                                    Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                                    Toast.makeText(getApplicationContext(), "Tag changed to: " +
                                            "" + checkedItem.toString(), Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast
                                            .LENGTH_SHORT).show();
                                }*/

                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}
