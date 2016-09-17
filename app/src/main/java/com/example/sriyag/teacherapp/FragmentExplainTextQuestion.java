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
public class FragmentExplainTextQuestion extends Fragment {

    EditText etExplainQs;
    Button btnSaveQuestion_Explain;
    String question;
    RelativeLayout rlfragmcq;
    TextView tvSaveStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.setRetainInstance(true);

        View view = inflater.inflate(R.layout.fragment_explain_question, container, false);

        String explainQs = getArguments().getString("explainQuestion") ;
        String qsNum = getArguments().getString("questionnumber") ;

        /*tvSaveStatus = (TextView) getActivity().findViewById(R.id.tvSaveStatus);
        tvSaveStatus.setText("Unsaved");*/


        //initializing all views:
        etExplainQs = (EditText) view.findViewById(R.id.etExplainQuestion);
        btnSaveQuestion_Explain = (Button) view.findViewById(R.id.btnSaveQuestion_Explain);
        rlfragmcq = (RelativeLayout) view.findViewById(R.id.rlfragmcq);

        btnSaveQuestion_Explain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //document builder factory: save entire question in different tags in xml file
                //first retrieve all the text content: question and options
                question = etExplainQs.getText().toString();
//                tvSaveStatus.setText("Saved");

                //should be name of questionpaper: course_exam_questionpaper.xml
                String filepath = Environment.getExternalStorageDirectory() +
                        "/datastorage_t1_questionpaper" + ".xml";
                File file = new File(filepath);

                if (file.exists()) {
                    try {
                       modifyXMLFile("explain_text", question);


                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    insertIntoXMLFile("explain_text", question);
                }

            }
        });

        return view;
    }

    public void insertIntoXMLFile(String tag,  String text)
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


            Node n1 = d.createTextNode(tag);
            Node n2 = d.createTextNode(text);


            child1_tag.appendChild(n1);
            child2_text.appendChild(n2);


            root.appendChild(child1_tag);
            root.appendChild(child2_text);

            d.appendChild(root);

            /*if(!f.exists())
            {
                d.appendChild(root);
            }*/

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

    public void modifyXMLFile (String tag,  String text) {
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

            /*Toast.makeText(getActivity().getBaseContext(), "list.item(0): " + list.item(0)
                    .getNodeName(), Toast
                    .LENGTH_SHORT)
                    .show();*/ //prints tag, not mcq

            if (root.getFirstChild().getTextContent().equals("mcq")) {

                Toast.makeText(getActivity().getBaseContext(), "if block: tag mcq", Toast
                        .LENGTH_SHORT)
                        .show();

                /*

                            // append a new node to staff
                    Element age = doc.createElement("age");
                    age.appendChild(doc.createTextNode("28"));
                    staff.appendChild(age);




                    Element root = document.getDocumentElement();
            root.appendChild(document.createElement("first-name"));
            Will append first-name to the end of the DOM tree

            Inserting into the first position is a little more difficult...

            Element root = document.getDocumentElement();
            if (root.hasChildNodes()) {
                Node firstChild = root.getFirstChild();
                root.insertBefore(document.createElement("first-name"), firstChild);
            } else {
                // Append to as per previous example
            }
            UPDATE

            Eleement firstName = document.createElement("first-name");
            firstName.setTextContent("Henry");



                 */

                //create new node
                Element main = doc.getDocumentElement();
                Element root2 = doc.createElement("question");
                Element child1_tag = doc.createElement("tag");
                Element child2_text = doc.createElement("text");

                Node n1 = doc.createTextNode(tag);
                Node n2 = doc.createTextNode(text);

                child1_tag.appendChild(n1);
                child2_text.appendChild(n2);

                root2.appendChild(child1_tag);
                root2.appendChild(child2_text);
                main.appendChild(root2);

            }

            else {
                Toast.makeText(getActivity().getBaseContext(), "else block: explain_text", Toast
                        .LENGTH_SHORT)
                        .show();
                //UPDATING NODES
                for (int i = 0; i < list.getLength(); i++) {

                    Node node = list.item(i);

                    if ("tag".equals(node.getNodeName())) {
                        node.setTextContent("explain_text");
                    }

                    // get the text element, and update the value
                    if ("text".equals(node.getNodeName())) {
                        node.setTextContent(text);
                    }


                }
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
