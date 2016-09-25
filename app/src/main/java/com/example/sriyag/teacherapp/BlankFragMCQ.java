package com.example.sriyag.teacherapp;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by sriyag on 12/09/16.
 */
public class BlankFragMCQ extends Fragment {

    EditText etMCQQs, etOpt1, etOpt2, etOpt3, etOpt4;
    Button btnSaveQuestion;
    String question, optiona, optionb, optionc, optiond;
    RelativeLayout rlfragmcq;

    TextView tvSaveStatus;

    int click_count = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.setRetainInstance(true);

        View view = inflater.inflate(R.layout.blank_frag_mcq, container, false);

        tvSaveStatus = (TextView) getActivity().findViewById(R.id.tvSaveStatus);
        tvSaveStatus.setText("Unsaved");


        //initializing all views:
        etMCQQs = (EditText) view.findViewById(R.id.etMCQQuestion);
        etOpt1 = (EditText) view.findViewById(R.id.etOption1);
        etOpt2 = (EditText) view.findViewById(R.id.etOption2);
        etOpt3 = (EditText) view.findViewById(R.id.etOption3);
        etOpt4 = (EditText) view.findViewById(R.id.etOption4);
        //btnAddOption = (Button) view.findViewById(R.id.btnAddOption);
        btnSaveQuestion = (Button) view.findViewById(R.id.btnSaveQuestion);
        rlfragmcq = (RelativeLayout) view.findViewById(R.id.rlfragmcq);


        btnSaveQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //document builder factory: save entire question in different tags in xml file
                //first retrieve all the text content: question and options
                question = etMCQQs.getText().toString();
                optiona = etOpt1.getText().toString();
                optionb = etOpt2.getText().toString();
                optionc = etOpt3.getText().toString();
                optiond = etOpt4.getText().toString();
                //more options??
                //if click_count > 0, more options have been added

                tvSaveStatus.setText("Saved");

                //should be name of questionpaper: course_exam_questionpaper.xml
                String filepath = Environment.getExternalStorageDirectory() +
                        "/datastorage_t1_questionpaper" + ".xml";
                File file = new File(filepath);

                if (file.exists()) {
                    try {
                        modifyXMLFile("mcq", question, optiona, optionb, optionc, optiond);


                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    insertIntoXMLFile("mcq", question, optiona, optionb, optionc, optiond);
                }

            }
        });

        return view;
    }

    public void insertIntoXMLFile(String tag,  String text, String optiona, String optionb,
                                  String optionc, String optiond)
    {
        try
        {
            DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
            DocumentBuilder builder=factory.newDocumentBuilder();
            /*String path = Environment.getExternalStorageDirectory() + "/" +
                    "final_question_paper.xml"; //append course and exam name
                    */

            String path = Environment.getExternalStorageDirectory() + "/" +
                    "datastorage_t1_questionpaper.xml"; //append course and exam name

            File f = new File(path);
            Document d = null;
            Element root;
            if(f.exists())
            {
                f.delete();
            }
            d=builder.newDocument();

            root = d.createElement("question");
            Element child1_tag = d.createElement("tag");
            Element child2_text = d.createElement("text");

            Element option1 = d.createElement("optiona");
            Element option2 = d.createElement("optionb");
            Element option3 = d.createElement("optionc");
            Element option4 = d.createElement("optiond");

            Node n1 = d.createTextNode(tag);
            Node n2 = d.createTextNode(text);
            Node n3 = d.createTextNode(optiona);
            Node n4 = d.createTextNode(optionb);
            Node n5 = d.createTextNode(optionc);
            Node n6 = d.createTextNode(optiond);


            child1_tag.appendChild(n1);
            child2_text.appendChild(n2);
            option1.appendChild(n3);
            option2.appendChild(n4);
            option3.appendChild(n5);
            option4.appendChild(n6);

            root.appendChild(child1_tag);
            root.appendChild(child2_text);
            root.appendChild(option1);
            root.appendChild(option2);
            root.appendChild(option3);
            root.appendChild(option4);

            if(!f.exists())
            {
                d.appendChild(root);
            }

            TransformerFactory tfactory=TransformerFactory.newInstance();
            Transformer t=tfactory.newTransformer();

            DOMSource source=new DOMSource(d);

            String filename = Environment.getExternalStorageDirectory() + "/" +
                    "datastorage_t1_questionpaper.xml";
            FileWriter writer=new FileWriter(filename);

            StreamResult result=new StreamResult(writer);

            t.transform(source, result);
            Toast.makeText(getActivity().getBaseContext(), "Added to question paper", Toast.LENGTH_SHORT)
                    .show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    //Use this method when question paper file already exists

    public void modifyXMLFile (String tag,  String text, String optiona, String optionb,
                               String optionc, String optiond) {
        try {
            String filename = Environment.getExternalStorageDirectory() + "/" +
                    "datastorage_t1_questionpaper.xml";
            File file = new File(filename);

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(filename));


            // Get the root element
            Node root = doc.getFirstChild(); //question

            /*Node tag = doc.getElementsByTagName("tag").item(0);
            Node text = doc.getElementsByTagName("text").item(1);
            Node opta = doc.getElementsByTagName("optiona").item(2);
            Node optb = doc.getElementsByTagName("optionb").item(3);
            Node optc = doc.getElementsByTagName("optionc").item(4);
            Node optd = doc.getElementsByTagName("optiond").item(5);*/

            // update tag attribute
            /*NamedNodeMap attr = text.getAttributes();
            Node nodeAttr = attr.getNamedItem("id");
            nodeAttr.setTextContent("2");*/

            // append a new node to staff
           /* Element age = doc.createElement("age");
            age.appendChild(doc.createTextNode("28"));
            staff.appendChild(age);*/

            // loop the child node
            NodeList list = root.getChildNodes();

            //UPDATING NODES
            for (int i = 0; i < list.getLength(); i++) {

                Node node = list.item(i);

                if ("tag".equals(node.getNodeName())) {
                    node.setTextContent("mcq");
                }

                // get the text element, and update the value
                if ("text".equals(node.getNodeName())) {
                    node.setTextContent(text);
                }

                if ("optiona".equals(node.getNodeName())) {
                    node.setTextContent(optiona);
                }

                if ("optionb".equals(node.getNodeName())) {
                    node.setTextContent(optionb);
                }

                if ("optionc".equals(node.getNodeName())) {
                    node.setTextContent(optionc);
                }


                if ("optiond".equals(node.getNodeName())) {
                    node.setTextContent(optiond);
                }

                //remove firstname
                /*if ("firstname".equals(node.getNodeName())) {
                    staff.removeChild(node);
                }*/

            }
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));

            transformer.transform(source, result);

            Toast.makeText(getActivity().getBaseContext(), "Added to question paper", Toast.LENGTH_SHORT)
                    .show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getBaseContext(), "catch block modifyXML: " + e
                            .getMessage(),
                    Toast.LENGTH_LONG).show();

        }

    }
}
