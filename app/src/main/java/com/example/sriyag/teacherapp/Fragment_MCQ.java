package com.example.sriyag.teacherapp;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by HP1 on 11-03-2016.
 */
public class Fragment_MCQ extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    TextView question ;
    RadioGroup radioGroup ;
    Button saveButton ;
    TextView question_number ;
    String qn ;
    String checkedAnswer ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mcq, container, false) ;
        String a = getArguments().getString("a") ;
        String b = getArguments().getString("b") ;
        String c = getArguments().getString("c") ;
        String d = getArguments().getString("d") ;
        String e = getArguments().getString("e") ;
        qn = getArguments().getString("questionnumber") ;
        question = (TextView)view.findViewById(R.id.question_textview) ;
        radioGroup = (RadioGroup)view.findViewById(R.id.radio_group) ;
        radioGroup.setOnCheckedChangeListener(this);
        question_number = (TextView)view.findViewById(R.id.question_number) ;
        saveButton = (Button)view.findViewById(R.id.save_button) ;
        saveButton.setOnClickListener(this);
        question.setText(a);
        question_number.setText(qn + ". ");
        for (int i = 0; i < radioGroup .getChildCount(); i++) {
            if(i==0)
            ((RadioButton) radioGroup.getChildAt(i)).setText(b);
            if(i==1)
                ((RadioButton) radioGroup.getChildAt(i)).setText(c);
            if(i==2)
                ((RadioButton) radioGroup.getChildAt(i)).setText(d);
            if(i==3)
                ((RadioButton) radioGroup.getChildAt(i)).setText(e);
        }

        return view ;
    }



    public void mcqData(String a,String b,String c,String d,String e)
    {

    }

    @Override
    public void onClick(View v) {
        String filepath = Environment.getExternalStorageDirectory() + "/" + "datastorage_t1_questionpaper" ;
        File file = new File(filepath);
        DocumentBuilder dbuilder = null;
        try {
            DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
            dbuilder = dbfactory.newDocumentBuilder();
            Document document = dbuilder.parse(file);
            Element element = document.getDocumentElement();
            NodeList questions_list = document.getElementsByTagName("question");   //tags with question
            Node particular_question = questions_list.item(Integer.parseInt(qn) - 1) ;
            Toast.makeText(getActivity(),qn,Toast.LENGTH_SHORT).show();
            NodeList attributes = particular_question.getChildNodes() ;
            for(int i=0;i<attributes.getLength();i++)
            {
                Node temp = attributes.item(i) ;
                if(temp.getNodeName().equalsIgnoreCase("answer"))
                {
                    if(checkedAnswer!=null && checkedAnswer.length()>0)
                    {
                        temp.setTextContent(checkedAnswer);
                        Toast.makeText(getActivity(),"writing"+checkedAnswer,Toast.LENGTH_SHORT).show();
                    }
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId){
            case R.id.option_a: checkedAnswer = "A" ;
                break;
            case R.id.option_b: checkedAnswer = "B" ;
                break;
            case R.id.option_c: checkedAnswer = "C" ;
                break;
            case R.id.option_d: checkedAnswer = "D" ;
                break;
        }
    }
}
