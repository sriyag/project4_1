package com.example.sriyag.teacherapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by HP1 on 15-04-2016.
 */
public class SeeQuestionPaper extends Activity implements AdapterView.OnItemClickListener,
        View.OnClickListener {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

//    Fragment_MCQ fragment_mcq;
    FragmentMCQQuestion fragment_mcq_question;
    FragmentExplainTextQuestion frag_explain;
    FragmentDrawQuestion frag_draw;

    FragmentLabelQuestion fragment_label_question;

    String subject;    //CourseName
    String test;  //ExamNameOfCourse

    Button prevQuestion, nextQuestion, btnAddQs, btnDelQs;  //button for next question
    //Button delete, modify, save;

    Spinner questionTag;

    TextView title, qsNum, saveStatus;       //title at top
    ListView questionNumberList;   //listview
    private ArrayList<String> questionNumber = new ArrayList<>();

    int numberOfQuestions;   //total no. of questions

    String questionPaperFileName;    //file name
    String tagOfQuestion;            //tag of question

    int numberOfLabels;
    int questionNumberSelected = 1;      // selected question

    String questionFullIfMcq;    //if tag is MCQ use these variables.
    String optionA;
    String optionB;
    String optionC;
    String optionD;

    String explainQuestion;     //if tag is EXPLAIN_TEXT use these variables
    String drawQuestion ;      //if Draw tag is used
    String labelQuestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_question_paper);

        prevQuestion = (Button) findViewById(R.id.prev_question);
        prevQuestion.setOnClickListener(this);

        nextQuestion = (Button) findViewById(R.id.next_question);
        nextQuestion.setOnClickListener(this);

        btnAddQs = (Button) findViewById(R.id.btnAddQs);
        btnAddQs.setOnClickListener(this);

        btnDelQs = (Button) findViewById(R.id.btnDelQs);
        btnDelQs.setOnClickListener(this);

        /*delete = (Button) findViewById(R.id.DeleteQs);
        delete.setOnClickListener(this);

        modify = (Button) findViewById(R.id.ModifyQs);
        modify.setOnClickListener(this);

        save = (Button) findViewById(R.id.SaveQs);
        save.setOnClickListener(this);*/

        subject = getIntent().getExtras().getString("subject");
        test = getIntent().getExtras().getString("test");

        questionTag = (Spinner) findViewById(R.id.questionTag);
        qsNum = (TextView) findViewById(R.id.tvQsNum);
        saveStatus = (TextView) findViewById(R.id.tvSaveStatus);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tag, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        questionTag.setAdapter(adapter);
        questionTag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

               /*Toast.makeText(getApplicationContext(), "Tag: " + adapter.getItem(pos), Toast
                       .LENGTH_SHORT).show();*/

                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SeeQuestionPaper.this);
                    builder
                            .setTitle("Another question selected")
                            .setMessage("Have you saved your current question?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Yes button clicked, do something
                                    //CHANGE TAG!?
                                }
                            })
                            .setNegativeButton("No", null)                        //Do nothing on no
                            .show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "spinner alert: " + e.getMessage(), Toast.LENGTH_LONG)
                            .show();
                }

            }
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });


//        questionPaperFileName = subject.concat("_").concat(test).concat
            questionPaperFileName="datastorage_t1_questionpaper";
            numberOfQuestions=

            findNumberOfQuestions(questionPaperFileName);   //to get number of questions

            title=(TextView)

            findViewById(R.id.titleTop);

            title.setText(subject.concat("-").

            concat(test)

            );

            questionNumberList=(ListView)findViewById(R.id.questionNumberList);


            int x;
            for(x=1;x<=numberOfQuestions;x++)

            {
                questionNumber.add("Q" + x);
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, questionNumber);
            questionNumberList.setAdapter(arrayAdapter);
            questionNumberList.setOnItemClickListener(this);    //displaying ListView done.

        //Toast.makeText(this, "Q: " + questionNumberSelected, Toast.LENGTH_SHORT).show();



            displayQuestion(questionNumberSelected);   //displaying the first question by-default
        } // onCreate closed

    private int findNumberOfQuestions(String questionPaperFileName) {

        String filepath = Environment.getExternalStorageDirectory() + "/" + questionPaperFileName;
        File file = new File(filepath);
        DocumentBuilder dbuilder = null;
        try {
            DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
            dbuilder = dbfactory.newDocumentBuilder();
            Document document = dbuilder.parse(file);
            Element element = document.getDocumentElement();
            NodeList questions_list = document.getElementsByTagName("question");
            numberOfQuestions = questions_list.getLength();   //number of questions
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return numberOfQuestions;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

        //alert dialog pop up on choosing new question
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Another question selected")
                .setMessage("Have you saved your current question?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Yes button clicked, do something
                        view.setSelected(true);
                        questionNumberSelected = position + 1;
                        displayQuestion(questionNumberSelected); //spinner tag changed in this method
                        qsNum.setText("Q" + questionNumberSelected);

                        //do not show prev button for the first question
                        if (questionNumberSelected == 1) {
                            prevQuestion.setVisibility(View.INVISIBLE);
                        } else {
                            prevQuestion.setVisibility(View.VISIBLE);
                        }

                        //do not show next button for the last question
                        int lastQs = numberOfQuestions;
                        if (questionNumberSelected == lastQs) {
                            nextQuestion.setVisibility(View.INVISIBLE);
                        } else {
                            nextQuestion.setVisibility(View.VISIBLE);
                        }


                    }
                })
                .setNegativeButton("No", null)						//Do nothing on no
                .show();

        //highlight current question
        //view.setBackgroundColor(Color.YELLOW);
    }

    private void displayQuestion(int questionNumberSelected) {
        Node current_question = null;
        Node current_item = null;
        NodeList current_children_childnodes;
        String filepath = Environment.getExternalStorageDirectory() + "/" + questionPaperFileName;
        File file = new File(filepath);
        DocumentBuilder dbuilder = null;
        DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
        int i, j;
        try {
            dbuilder = dbfactory.newDocumentBuilder();
            Document document = dbuilder.parse(file);
            Element element = document.getDocumentElement();
            NodeList questions_list = document.getElementsByTagName("question");  //elements with tag question
            numberOfQuestions = questions_list.getLength();   //number of questions
            for (i = 0; i < questions_list.getLength(); i++) {
                if (i == (questionNumberSelected - 1)) {
                    current_question = questions_list.item(i);
                    current_children_childnodes = current_question.getChildNodes();
                    for (j = 0; j < current_children_childnodes.getLength(); j++) {
                        current_item = current_children_childnodes.item(j);
                        if (current_item.getNodeName().equalsIgnoreCase("tag")) {
                            tagOfQuestion = current_item.getTextContent().toString();
                            break;
                        }

                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (tagOfQuestion) {
            case "mcq":
                questionTag.setSelection(0);
                current_children_childnodes = current_question.getChildNodes();
                for (j = 0; j < current_children_childnodes.getLength(); j++) {
                    current_item = current_children_childnodes.item(j);
                    if (current_item.getNodeName().equalsIgnoreCase("text")) {
                        questionFullIfMcq = current_item.getTextContent().toString();
                    }
                    if (current_item.getNodeName().equalsIgnoreCase("optiona")) {
                        optionA = current_item.getTextContent().toString();
                    }
                    if (current_item.getNodeName().equalsIgnoreCase("optionb")) {
                        optionB = current_item.getTextContent().toString();
                    }
                    if (current_item.getNodeName().equalsIgnoreCase("optionc")) {
                        optionC = current_item.getTextContent().toString();
                    }
                    if (current_item.getNodeName().equalsIgnoreCase("optiond")) {
                        optionD = current_item.getTextContent().toString();
                    }
                }


                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("a", questionFullIfMcq);
                bundle.putString("b", optionA);
                bundle.putString("c", optionB);
                bundle.putString("d", optionC);
                bundle.putString("e", optionD);
                bundle.putString("questionnumber",questionNumberSelected+"");

                /*if (fragmentManager.findFragmentByTag("" + questionNumberSelected) == null) {
                    fragment_mcq = new Fragment_MCQ();
                    fragment_mcq.setArguments(bundle);
                    fragmentTransaction.replace(R.id.linearLayout, fragment_mcq, "" + questionNumberSelected).addToBackStack(null).commit();
                }
                else
                {
                    fragment_mcq = (Fragment_MCQ) fragmentManager.findFragmentByTag("" + questionNumberSelected);
                    fragmentTransaction.replace(R.id.linearLayout, fragment_mcq, "" + questionNumberSelected).addToBackStack(null).commit();
                }*/

                if (fragmentManager.findFragmentByTag("" + questionNumberSelected) == null) {
                    fragment_mcq_question = new FragmentMCQQuestion();
                    fragment_mcq_question.setArguments(bundle);
                    fragmentTransaction.replace(R.id.linearLayout, fragment_mcq_question, "" + questionNumberSelected).addToBackStack(null).commit();
                }
                else
                {
                    fragment_mcq_question = (FragmentMCQQuestion) fragmentManager.findFragmentByTag("" +
                            questionNumberSelected);
                    fragmentTransaction.replace(R.id.linearLayout, fragment_mcq_question, "" + questionNumberSelected).addToBackStack(null).commit();
                }

                break;


            //CASE EXPLAIN TEXT - SECOND FRAGMENT TO BE LOADED

            case "explain_text":
                questionTag.setSelection(1);
                current_children_childnodes = current_question.getChildNodes();
                for (j = 0; j < current_children_childnodes.getLength(); j++) {
                    current_item = current_children_childnodes.item(j);
                    if (current_item.getNodeName().equalsIgnoreCase("text")) {
                        explainQuestion = current_item.getTextContent();
                    }
                }

                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundleForExplainText = new Bundle();
                bundleForExplainText.putString("explainQuestion", explainQuestion);
                bundleForExplainText.putString("questionnumber",questionNumberSelected+"");

                if (fragmentManager.findFragmentByTag("" + questionNumberSelected) == null) {
                    frag_explain = new FragmentExplainTextQuestion();
                    frag_explain.setArguments(bundleForExplainText);
                    fragmentTransaction.replace(R.id.linearLayout, frag_explain, "" + questionNumberSelected).addToBackStack(null).commit();
                }
                else
                {
                    frag_explain = (FragmentExplainTextQuestion) fragmentManager.findFragmentByTag("" +
                            questionNumberSelected);
                    fragmentTransaction.replace(R.id.linearLayout, frag_explain, "" + questionNumberSelected).addToBackStack(null).commit();
                }



                break;

            case "draw":
                questionTag.setSelection(2);
                current_children_childnodes = current_question.getChildNodes() ;
                for(j=0;j<current_children_childnodes.getLength();j++)
                {
                    current_item = current_children_childnodes.item(j) ;
                    if(current_item.getNodeName().equalsIgnoreCase("text"))
                    {
                        drawQuestion = current_item.getTextContent().toString();
                    }
                }
                fragmentManager = getFragmentManager() ;
                fragmentTransaction = fragmentManager.beginTransaction() ;
                Bundle bundleForDraw = new Bundle() ;
                bundleForDraw.putString("drawQuestion", drawQuestion);
                bundleForDraw.putString("questionnumber",questionNumberSelected+"");

                if (fragmentManager.findFragmentByTag("" + questionNumberSelected) == null) {
                    frag_draw = new FragmentDrawQuestion();
                    frag_draw.setArguments(bundleForDraw);
                    fragmentTransaction.replace(R.id.linearLayout, frag_draw, "" + questionNumberSelected).addToBackStack(null).commit();
                }
                else
                {
                    frag_draw = (FragmentDrawQuestion) fragmentManager.findFragmentByTag("" +
                            questionNumberSelected);
                    fragmentTransaction.replace(R.id.linearLayout, frag_draw, "" + questionNumberSelected).addToBackStack(null).commit();
                }


                break ;

            case "label":
                questionTag.setSelection(3);
                current_children_childnodes = current_question.getChildNodes() ;
                for(j=0;j<current_children_childnodes.getLength();j++)
                {
                    current_item = current_children_childnodes.item(j) ;
                    if(current_item.getNodeName().equalsIgnoreCase("text"))
                    {
                        labelQuestion = current_item.getTextContent().toString();
                    }

                    if (current_item.getNodeName().equalsIgnoreCase("label"))
                    {
                        numberOfLabels = Integer.parseInt(current_item.getTextContent().toString());
                    }
                }



                fragmentManager = getFragmentManager() ;
                fragmentTransaction = fragmentManager.beginTransaction() ;
                Bundle bundleForLabel = new Bundle() ;
                bundleForLabel.putString("labelQuestion", labelQuestion);
                bundleForLabel.putInt("number_of_labels", numberOfLabels);
                bundleForLabel.putString("questionnumber",questionNumberSelected+"");

                if (fragmentManager.findFragmentByTag("" + questionNumberSelected) == null) {
                    fragment_label_question = new FragmentLabelQuestion();
                    fragment_label_question.setArguments(bundleForLabel);
                    fragmentTransaction.replace(R.id.linearLayout, fragment_label_question, "" + questionNumberSelected).addToBackStack(null).commit();
                }
                else
                {
                    fragment_label_question = (FragmentLabelQuestion) fragmentManager.findFragmentByTag("" +
                            questionNumberSelected);
                    fragmentTransaction.replace(R.id.linearLayout, fragment_label_question, "" + questionNumberSelected).addToBackStack(null).commit();

                }

                break ;



        } //switch closed

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_question:
                questionNumberSelected = questionNumberSelected + 1;
                if (questionNumberSelected <= numberOfQuestions)
                    displayQuestion(questionNumberSelected);
                break;

            case R.id.prev_question:
                questionNumberSelected = questionNumberSelected - 1;
                if (questionNumberSelected <= numberOfQuestions && questionNumberSelected >= 2)
                    displayQuestion(questionNumberSelected);
                break;

            case R.id.btnAddQs:
                //startActivity(new Intent(SeeQuestionPaper.this, AddQuestion.class));

                //load blank fragment with edit text and save question button, load according to
                // question type

                break;

            case R.id.btnDelQs:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder
                        .setTitle("Delete Question")
                        .setMessage("Are you sure?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Yes button clicked, do something
                                //DELETE QUESTION FROM XML AND UPDATE

                                //remove firstname
                                //dom parsing
                                /*if ("firstname".equals(node.getNodeName())) {
                                    staff.removeChild(node);
                                }*/

                                Toast.makeText(SeeQuestionPaper.this, "Question deleted",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null)						//Do nothing on no
                        .show();
                break;

            /*case R.id.DeleteQs:
                //first popup confirmation alert dialog
                //Put up the Yes/No message box
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder
                        .setTitle("Delete Question")
                        .setMessage("Are you sure?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Yes button clicked, do something
                                //DELETE QUESTION FROM XML AND UPDATE

                                Toast.makeText(SeeQuestionPaper.this, "Question deleted",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null)						//Do nothing on no
                        .show();

                break;

            case R.id.ModifyQs:
                break;

            case R.id.SaveQs:

                //UPDATE XML FILE

                Toast.makeText(getApplicationContext(), "Question saved", Toast.LENGTH_SHORT).show();
                break;*/



        }
    }
}
