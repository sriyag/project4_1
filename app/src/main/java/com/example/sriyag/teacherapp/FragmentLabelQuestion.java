package com.example.sriyag.teacherapp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileWriter;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by sriyag on 14/09/16.
 */
public class FragmentLabelQuestion extends Fragment implements View.OnClickListener,
        View.OnTouchListener {

    private ViewGroup relativeLayout;
    private int _xDelta;
    private int _yDelta;

    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();
    

    String labelQs, qsNum;
    int numberOfLabels;
    EditText etLabelQuestion;
    ImageView ivLabelImage;
    Button btnPlus, btnMinus, btnChooseImage, btnSaveQuestion_Label;
    TextView tvChosenImage, tvSaveStatus;

    private static final int FILE_SELECT_CODE = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_label_question, container, false);

        labelQs = getArguments().getString("labelQuestion");
        numberOfLabels = getArguments().getInt("number_of_labels");
        qsNum = getArguments().getString("questionnumber");

        relativeLayout = (ViewGroup) view.findViewById(R.id.root);
        scaleGestureDetector = new ScaleGestureDetector(getActivity(), new ScaleListener());

        /*tvSaveStatus = (TextView) getActivity().findViewById(R.id.tvSaveStatus);
        tvSaveStatus.setText("Unsaved");
*/

        etLabelQuestion = (EditText) view.findViewById(R.id.etLabelQuestion);
        ivLabelImage = (ImageView) view.findViewById(R.id.ivLabelImage);
        tvChosenImage = (TextView) view.findViewById(R.id.tvImgFile);

        btnPlus = (Button) view.findViewById(R.id.btnPlus);
        btnPlus.setOnClickListener(this);
        btnMinus = (Button) view.findViewById(R.id.btnMinus);
        btnMinus.setOnClickListener(this);
        btnChooseImage = (Button) view.findViewById(R.id.btnChooseImg);
        btnChooseImage.setOnClickListener(this);
        btnSaveQuestion_Label = (Button) view.findViewById(R.id.btnSaveQuestion_Label);
        btnSaveQuestion_Label.setOnClickListener(this);

        ivLabelImage.setImageResource(R.drawable.skeleton);

        return view;
    }

    public void onClick (View v) {

        switch (v.getId()) {

            case R.id.btnPlus:
                //add a dynamic edit text box and reposition
                //addEditTextView();
                try {
                    EditText et = new EditText(getActivity());
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(400, 150);
                    et.setHint("Label");
                    et.setText("Label");
                    et.setWidth(400);
                    et.setHeight(150);
                    et.setLayoutParams(layoutParams);
                    et.setOnTouchListener(this);
                    et.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "etSet", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "catch btnPlus: " + e.getMessage(), Toast
                            .LENGTH_SHORT).show();
                }

                break;

            case R.id.btnMinus:
                //remove an edit text box by clicking on it
                //removeEditTextView();
                //pop up alert dialog saying which label to delete?? confirm this!!
                break;

            case R.id.btnChooseImg:
                //load directory chooser and select and image file
                //Getting list of files from a directory stored in the internal storage
                try {
                    showFileChooser();

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "catch: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btnSaveQuestion_Label:

                        //document builder factory: save entire question in different tags in xml file
                        //first retrieve all the text content: question and options
                //tvSaveStatus.setText("Saved");

                try {
                    labelQs = etLabelQuestion.getText().toString();

                    //should be name of questionpaper: course_exam_questionpaper.xml
                    String filepath = Environment.getExternalStorageDirectory() +
                            "/datastorage_t1_questionpaper" + ".xml";
                    File file = new File(filepath);

                    if (file.exists()) {
                        try {
                            modifyXMLFile("label", labelQs);
//                            Toast.makeText(getActivity(), "modify", Toast.LENGTH_SHORT).show();


                        } catch (Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        insertIntoXMLFile("label", labelQs);
//                        Toast.makeText(getActivity(), "insert", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity().getBaseContext(), "What's happening?\n" + e
                            .getMessage(), Toast.LENGTH_LONG).show();
                }
                    break;
        }
    }


    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select an image file"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == -1) { //RESULT_OK
                    // Get the Uri of the selected file
                    String path = Environment.getExternalStorageDirectory() + "/";
                    Uri uri = data.getData();
                    tvChosenImage.setText(uri.toString());
                    String newImageFileName = tvChosenImage.getText().toString();

                    //Code for loading an image from the internal storage on to the image view
                    try {
                        /*Bitmap bitmap = BitmapFactory.decodeFile(newImageFileName);
                        ivLabelImage = (ImageView) getActivity().findViewById(R.id.ivLabelImage);
                        ivLabelImage.setImageBitmap(bitmap);*/
                        Bitmap bm = MediaStore.Images.Media.getBitmap(getActivity()
                                .getContentResolver(), uri);
                        ivLabelImage.setImageBitmap(bm);
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "image fail: " + e.getMessage(), Toast
                                .LENGTH_SHORT).show();
                    }

                    // Get the file path
                    try {
                        path = getPath(getActivity(), uri);
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "path: " + path + " " + e.getMessage(), Toast
                                .LENGTH_SHORT).show();
                    }
                    /*Toast.makeText(getActivity(), "File Path: " + path, Toast
                            .LENGTH_SHORT).show();*/
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
//                Toast.makeText(getActivity().getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
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


            //if(!f.exists())
            //{
                d.appendChild(root);
            //}

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


            if (root.getFirstChild().getTextContent().equals("mcq") || root.getFirstChild()
                    .getTextContent().equals("explain_text") || root.getFirstChild()
                    .getTextContent().equals("draw")) {

                //create new node
                Element main = doc.getDocumentElement();
                Element root3 = doc.createElement("question");
                Element child1_tag = doc.createElement("tag");
                Element child2_text = doc.createElement("text");

                Node n1 = doc.createTextNode(tag);
                Node n2 = doc.createTextNode(text);

                child1_tag.appendChild(n1);
                child2_text.appendChild(n2);

                root3.appendChild(child1_tag);
                root3.appendChild(child2_text);
                main.appendChild(root3);

            }

            else {

                //UPDATING NODES
                for (int i = 0; i < list.getLength(); i++) {

                    Node node = list.item(i);

                    if ("tag".equals(node.getNodeName())) {
                        node.setTextContent("label");
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

    } //modifyxml end


    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                view.setLayoutParams(layoutParams);
                break;
        }
        relativeLayout.invalidate();
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.
            SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
            matrix.setScale(scaleFactor, scaleFactor);
            ivLabelImage.setImageMatrix(matrix);
            return true;
        }
    }


}
