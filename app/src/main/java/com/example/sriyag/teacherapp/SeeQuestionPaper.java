package com.example.sriyag.teacherapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by HP1 on 15-04-2016.
 */
public class SeeQuestionPaper extends Activity implements AdapterView.OnItemClickListener,
        View.OnClickListener, AdapterView.OnItemSelectedListener, AdapterView
                .OnItemLongClickListener, AdapterView.OnCreateContextMenuListener {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Intent intent;

    FragmentMCQQuestion fragment_mcq_question;
    FragmentExplainTextQuestion frag_explain;
    FragmentDrawQuestion frag_draw;
    FragmentLabelQuestion fragment_label_question;
    
    Button prevQuestion, nextQuestion, btnAddQs;
    Spinner questionTag;
    TextView title, qsNum, saveStatus;
    ListView questionNumberList;   //listview
    
    private ArrayList<String> questionNumber = new ArrayList<>();

    int numberOfQuestions;   //total no. of questions
    int numberOfLabels;
    int questionNumberSelected = 1;

    String subject, test, questionPaperFileName, tagOfQuestion;
    String mcqQuestion, optionA, optionB, optionC, optionD;
    String explainQuestion, drawQuestion, labelQuestion;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_question_paper);

        prevQuestion = (Button) findViewById(R.id.prev_question);
        prevQuestion.setVisibility(View.INVISIBLE);
        prevQuestion.setOnClickListener(this);

        nextQuestion = (Button) findViewById(R.id.next_question);
        nextQuestion.setOnClickListener(this);

        btnAddQs = (Button) findViewById(R.id.btnAddQs);
        btnAddQs.setOnClickListener(this);

        subject = getIntent().getExtras().getString("subject");
        intent = new Intent(SeeQuestionPaper.this, SelectQuestionPaper.class);
        intent.putExtra("subject", subject);
        test = getIntent().getExtras().getString("test");

        questionTag = (Spinner) findViewById(R.id.questionTag);
        qsNum = (TextView) findViewById(R.id.tvQsNum);
        qsNum.setText("Q1");

        saveStatus = (TextView) findViewById(R.id.tvSaveStatus);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tag, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        questionTag.setAdapter(adapter);
        questionTag.setOnItemSelectedListener(this);


//        questionPaperFileName = subject.concat("_").concat(test).concat
        questionPaperFileName="datastorage_t1_questionpaper";
        numberOfQuestions = findNumberOfQuestions(questionPaperFileName);   //to get number
        // of questions

        title=(TextView)findViewById(R.id.titleTop);
        title.setText(subject.concat("-").concat(test));

        questionNumberList=(ListView)findViewById(R.id.questionNumberList);

        int x;
        for(x=1;x<=numberOfQuestions;x++) {
            questionNumber.add("Q" + x);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, questionNumber);
        questionNumberList.setAdapter(arrayAdapter);
        questionNumberList.setOnItemClickListener(this);    //displaying ListView done.
        questionNumberList.setLongClickable(true);
        questionNumberList.setOnItemLongClickListener(this);



        //ACTION BAR
        ActionBar ab = getActionBar();

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return numberOfQuestions;
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
                        mcqQuestion = current_item.getTextContent().toString();
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
                bundle.putString("a", mcqQuestion);
                bundle.putString("b", optionA);
                bundle.putString("c", optionB);
                bundle.putString("d", optionC);
                bundle.putString("e", optionD);
                bundle.putString("questionnumber",questionNumberSelected+"");

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

    } //display question method closed

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

        //ACTION TO BE PERFORMED WHEN LIST VIEW ITEM IS CLICKED: QUESTION NUMBER
        questionNumberList.setSelector(R.drawable.selected_item_color);

        if (questionNumberSelected == 1) { //if position is 0
            questionNumberSelected = position + 1;
            displayQuestion(questionNumberSelected); //spinner tag changed in this method
            qsNum.setText("Q" + questionNumberSelected);
            saveStatus.setText("Unsaved");
            prevQuestion.setVisibility(View.INVISIBLE);
            nextQuestion.setVisibility(View.VISIBLE);
//            view.setBackgroundColor(Color.GRAY);
            qsNum.setText("Q1"); //update text view question number: show!!
        }

        else {

//            view.setBackgroundColor(Color.WHITE);
            prevQuestion.setVisibility(View.VISIBLE);
            //saveStatus.setText("Unsaved");

            if (saveStatus.getText().toString().equals("Unsaved")) {
                //alert dialog pop up on choosing new question - only if text view is unsaved
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder
                        .setTitle("Another question selected")
                        .setMessage("Have you saved your current question?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Yes button clicked, do something
//                                view.setSelected(true);
                                questionNumberSelected = position + 1;
                                displayQuestion(questionNumberSelected); //spinner tag changed in this method
                                qsNum.setText("Q" + questionNumberSelected);

                                //do not show next button for the last question
                                int lastQs = numberOfQuestions;
                                if (questionNumberSelected == lastQs) {
                                    nextQuestion.setVisibility(View.INVISIBLE);
                                } else {
                                    nextQuestion.setVisibility(View.VISIBLE);
                                }


                            }
                        })
                        .setNegativeButton("No", null)                        //Do nothing on no
                        .show();
            } //if closed

            else { //text view shows status: Saved. Freely go to the next question and update
            // accordingly

                questionNumberSelected = position + 1;
                displayQuestion(questionNumberSelected); //spinner tag changed in this method
                qsNum.setText("Q" + questionNumberSelected);

                //do not show next button for the last question
                int lastQs = numberOfQuestions;
                if (questionNumberSelected == lastQs) {
                    nextQuestion.setVisibility(View.INVISIBLE);
                } else {
                    nextQuestion.setVisibility(View.VISIBLE);
                }

            }
        }

        //highlight current question
        //view.setBackgroundColor(Color.YELLOW);
    } //onItemClick closed

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_question: //this is id wise!!

                prevQuestion.setVisibility(View.VISIBLE);

                if (saveStatus.getText().toString().equals("Unsaved")) {
                    //alert dialog pop up on choosing new question - only if text view is unsaved
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder
                            .setTitle("Another question selected")
                            .setMessage("Have you saved your current question?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Yes button clicked, do something
//                                    questionNumberList.getItemAtPosition(questionNumberSelected-1);

                                    questionNumberSelected = questionNumberSelected + 1;
                                    questionNumberList.setSelected(true);

                                    //questionNumberList.setSelector(R.drawable
                                    // .selected_item_color);

                                    displayQuestion(questionNumberSelected); //spinner tag changed in this method
                                    qsNum.setText("Q" + questionNumberSelected);

                                    //do not show next button for the last question
                                    if (questionNumberSelected == numberOfQuestions) {
                                        nextQuestion.setVisibility(View.INVISIBLE);
                                    } else {
                                        nextQuestion.setVisibility(View.VISIBLE);
                                    }

                                }
                            })
                            .setNegativeButton("No", null)                        //Do nothing on no
                            .show();
                } //if closed

                else { //text view shows status: Saved. Freely go to the next question and update
                    // accordingly

                    questionNumberSelected = questionNumberSelected + 1;
                    questionNumberList.setSelected(true);

                    displayQuestion(questionNumberSelected); //spinner tag changed in this method
                    qsNum.setText("Q" + questionNumberSelected);

                    //do not show next button for the last question
                    if (questionNumberSelected == numberOfQuestions) {
                        nextQuestion.setVisibility(View.INVISIBLE);
                    } else {
                        nextQuestion.setVisibility(View.VISIBLE);
                    }

                }


                break;

            case R.id.prev_question: //TEST THEN MODIFY

                //listView.setSelector(R.drawable.listview_item_selection_effect);
                //listView.setItemChecked(3, true);
                questionNumberList.setSelector(R.drawable.selected_item_color);
                questionNumberList.setItemChecked(questionNumberSelected-1, true);


                nextQuestion.setVisibility(View.VISIBLE);
                questionNumberSelected = questionNumberSelected - 1;
                if (questionNumberSelected <= numberOfQuestions && questionNumberSelected >= 2)
                    displayQuestion(questionNumberSelected);
                break;

            case R.id.btnAddQs:
                //startActivity(new Intent(SeeQuestionPaper.this, AddQuestion.class));

                //load blank fragment with edit text and save question button, load according to
                // question type

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
    } //onclick method closed

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

               /*Toast.makeText(getApplicationContext(), "Tag: " + adapter.getItem(pos), Toast
                       .LENGTH_SHORT).show();*/

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

                /*Toast.makeText(getApplicationContext(), "eql?: " + tagOfQuestion.trim().equalsIgnoreCase
                        (questionTag.getSelectedItem().toString().trim()), Toast
                        .LENGTH_SHORT)
                        .show();*/


            if (!(tagOfQuestion.trim().equalsIgnoreCase
                    (questionTag.getSelectedItem().toString().trim()))) {
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SeeQuestionPaper.this);
                    builder
                            .setTitle("Different tag selected")
                            .setMessage("Are you sure you want to change question type?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Yes button clicked, do something
                                    //CHANGE TAG
                                    modifyXMLFile(questionTag.getSelectedItem().toString(), questionNumberSelected);
                                    try {
                                        displayQuestionAccordingToTag(questionTag.getSelectedItem()
                                                .toString());
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "change spinner " +
                                                "tag error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    //displayQuestion(questionNumberSelected); - display frag
                                    // acc2 NEW TAG!!!!!! make new method
                                }
                            })
                            .setNegativeButton("No", null)                        //Do nothing on no
                            .show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "spinner alert: " + e.getMessage(), Toast.LENGTH_LONG)
                            .show();
                } //reason for firing twice
/*
                Toast.makeText(getApplicationContext(), "tag in spinner: " + questionTag
                        .getSelectedItem().toString(), Toast
                        .LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "tag in xml: " + tagOfQuestion, Toast.LENGTH_SHORT)
                        .show();*/

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void modifyXMLFile (String tag, int questionNumSel) {
        try {
            String filename = Environment.getExternalStorageDirectory() + "/" +
                    "datastorage_t1_questionpaper.xml";
            File file = new File(filename);

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(filename));


            // Get the root element
            Node root = doc.getFirstChild(); //question mcq

            // loop the child node
            NodeList list = root.getChildNodes();

            //UPDATING NODES
            for (int i = 0; i < list.getLength(); i++) {

                if (i == questionNumSel) {
                    Node node = list.item(i);
                    if ("tag".equals(node.getNodeName())) {
                        node.setTextContent(tag);
                    }
                }
            }


            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));

            transformer.transform(source, result);

            Toast.makeText(getApplicationContext(), "Added to question paper", Toast.LENGTH_SHORT)
                    .show();


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "catch block modifyXML: " + e
                            .getMessage(),
                    Toast.LENGTH_LONG).show();

        }

    } //modifyxml end

    //long press list view:
    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                   int pos, long id) {

        //pop up context menu
        registerForContextMenu(questionNumberList);
        openContextMenu(questionNumberList); //important to make the context menu visible!!

        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.delete_qs_menu, menu);


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        switch (item.getItemId()) {

            case R.id.delete_question_list_view:

                //pop up alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder
                        .setTitle("Delete Question")
                        .setMessage("Are you sure?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Yes button clicked, do something
                                //DELETE QUESTION FROM XML AND UPDATE

//                                delete(acmi.id);

                                Toast.makeText(SeeQuestionPaper.this, "Question deleted",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null)						//Do nothing on no
                        .show();

                return true;

            default:
                return super.onContextItemSelected(item);
        }

    } //on context item selected method closed

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

    public void displayQuestionAccordingToTag (String question_tag) {

        String questiontag = question_tag;

        switch (questiontag) {

            case "MCQ":

                fragmentManager = getFragmentManager() ;
                fragmentTransaction = fragmentManager.beginTransaction();

                BlankFragMCQ blankFragMCQ = new BlankFragMCQ(); //new instance of fragment
                fragmentTransaction.replace(R.id.linearLayout, blankFragMCQ).commit();

                break;

            case "Explain_Text":

                fragmentManager = getFragmentManager() ;
                fragmentTransaction = fragmentManager.beginTransaction();

                BlankFragExplainText blankFragExplainText = new BlankFragExplainText(); //new instance of fragment
                fragmentTransaction.replace(R.id.linearLayout, blankFragExplainText).commit();

                break;

            case "Draw":

                fragmentManager = getFragmentManager() ;
                fragmentTransaction = fragmentManager.beginTransaction();

                BlankFragDraw blankFragDraw = new BlankFragDraw(); //new instance of fragment
                fragmentTransaction.replace(R.id.linearLayout, blankFragDraw).commit();

                break;

            case "Label":

                fragmentManager = getFragmentManager() ;
                fragmentTransaction = fragmentManager.beginTransaction();

                BlankFragLabel blankFragLabel = new BlankFragLabel(); //new instance of fragment
                fragmentTransaction.replace(R.id.linearLayout, blankFragLabel).commit();

                break;
        }

    }
}
