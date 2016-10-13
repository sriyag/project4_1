package com.example.sriyag.teacherapp;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.util.ArrayList;

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
        View.OnTouchListener, View.OnLongClickListener, View.OnCreateContextMenuListener {

    private int _xDelta;
    private int _yDelta;
    private int count = 0;

    private int lMgn = 0, tMgn = 0;

    private ArrayList<String> al = new ArrayList<String>();

    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();

    RelativeLayout rl; //root

    String labelQs, qsNum, coords, qpfilename, question;
    int numberOfLabels;
    EditText etLabelQuestion;
    ImageView ivLabelImage;
    Button btnChooseImage, btnSaveQuestion_Label, btnAddLocation;
    TextView tvChosenImage, tvSaveStatus, tvLabel;

    private static final int FILE_SELECT_CODE = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_label_question, container, false);

        labelQs = getArguments().getString("labelQuestion");
        numberOfLabels = getArguments().getInt("number_of_labels");
        qsNum = getArguments().getString("questionnumber");

        qpfilename = getArguments().getString("qpfilename");

        rl = (RelativeLayout) view.findViewById(R.id.root);
        scaleGestureDetector = new ScaleGestureDetector(getActivity(), new ScaleListener());


        etLabelQuestion = (EditText) view.findViewById(R.id.etLabelQuestion);
        ivLabelImage = (ImageView) view.findViewById(R.id.ivLabelImage);
        ivLabelImage.setOnClickListener(this);

        tvChosenImage = (TextView) view.findViewById(R.id.tvImgFile);
        tvSaveStatus = (TextView) getActivity().findViewById(R.id.tvSaveStatus);


        btnChooseImage = (Button) view.findViewById(R.id.btnChooseImg);
        btnChooseImage.setOnClickListener(this);
        btnSaveQuestion_Label = (Button) view.findViewById(R.id.btnSaveQuestion_Label);
        btnSaveQuestion_Label.setOnClickListener(this);
        btnAddLocation = (Button) view.findViewById(R.id.btnLocation);
        btnAddLocation.setOnClickListener(this);


        String filename = Environment.getExternalStorageDirectory() + "/" + qpfilename;
        File file2 = new File(filename);
        if (file2.exists()) {
            //load via DOM parser

            Node current_question = null;
            Node current_item = null;
            NodeList current_children_childnodes;
            String filepath = Environment.getExternalStorageDirectory() + "/" + qpfilename;
            File file = new File(filepath);
            DocumentBuilder dbuilder = null;
            DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
            int j = 0;
            try {
                dbuilder = dbfactory.newDocumentBuilder();
                Document document = dbuilder.parse(file);
                Element element = document.getDocumentElement();
                NodeList questions_list = document.getElementsByTagName("question");  //elements with tag question


                current_question = questions_list.item(10); //load question acc2 tag and qsNum -
                // add attribute!!


                current_children_childnodes = current_question.getChildNodes();
                for (j = 0; j < current_children_childnodes.getLength(); j++) {
                    current_item = current_children_childnodes.item(j);

                    if (current_item.getNodeName().equalsIgnoreCase("text")) {
                        question = current_item.getTextContent().toString();
                        etLabelQuestion.setText(question);
                    }


                }


            } catch (Exception exc) {
                Toast.makeText(getActivity(), "error in parsing mcq contents: " + exc.getMessage
                        (), Toast.LENGTH_SHORT).show();
            }
        }

        //ivLabelImage.setImageResource(R.drawable.skeleton);

        return view;
    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ivLabelImage:

                count++;
                //creation of dynamic edit text boxes
                tvLabel = new TextView(getActivity());
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(300, 100);
                lp.setMargins(50, 200, 200, 50); //l, t, r, b

                tvLabel.setText(String.valueOf(count));
                tvLabel.setLayoutParams(lp);
                tvLabel.setId(count); //not being used though - can use in xml file

                Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.label_arrow_dynamic,
                        null);
                try {
                    drawable.setBounds(0, 0, 120, 80);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "unable to set bounds", Toast.LENGTH_SHORT).show();
                }
                tvLabel.setCompoundDrawables(drawable, null, null, null);

                tvLabel.setOnTouchListener(this); //correct context
                tvLabel.setLongClickable(true);
                tvLabel.setOnLongClickListener(this);

                rl.addView(tvLabel);
                break;


            case R.id.btnLocation:

                if (count > 0) {

                    al.add(coords);
//                    Toast.makeText(getActivity(), "et[" + count + "]: " + coords, Toast.LENGTH_SHORT).show();
                    //make text view unsaved on click of this button
                    tvSaveStatus.setText("Unsaved");
                } else {
                    Toast.makeText(getActivity(), "No label found!", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btnChooseImg:
                //load directory chooser and select and image file
                //Getting list of files from a directory stored in the internal storage
                try {
                    showFileChooser();

                    //delete all the previous image's labels!!!!!


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
                            modifyXMLFile("label", labelQs, count, al);
//                            Toast.makeText(getActivity(), "modify", Toast.LENGTH_SHORT).show();


                        } catch (Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        insertIntoXMLFile("label", labelQs, count, al);
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
            String[] projection = {"_data"};
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
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public void insertIntoXMLFile(String tag, String text, int count, ArrayList<String> arrlist) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            /*String path = Environment.getExternalStorageDirectory() + "/" +
                    "final_question_paper.xml"; //append course and exam name
                    */

            String path = Environment.getExternalStorageDirectory() + "/" +
                    "datastorage_t1_questionpaper.xml"; //append course and exam name

            File f = new File(path);
            Document d = null;
            Element root;
            if (f.exists()) {
                f.delete();
            }
            d = builder.newDocument();

            root = d.createElement("question");
            Element child1_tag = d.createElement("tag");
            Element child2_text = d.createElement("text");
            Element child3_count = d.createElement("labelcount");

            Node n1 = d.createTextNode(tag);
            Node n2 = d.createTextNode(text);
            Node n3 = d.createTextNode(String.valueOf(count));

            child1_tag.appendChild(n1); //element_name.appendchild(node_name)
            child2_text.appendChild(n2);
            child3_count.appendChild(n3);

            root.appendChild(child1_tag);
            root.appendChild(child2_text);
            root.appendChild(child3_count);


            Element[] e = new Element[count];
            Node[] n = new Node[count];

            for (int z = 0; z < count; z++) {
                e[z] = d.createElement("location");
                e[z].setAttribute("id", String.valueOf(count)); // <location id="1">

                //string array needed for all edit text coords
                n[z] = d.createTextNode(arrlist.get(z)); //node: (left, top)
            }

            for (int z = 0; z < count; z++) {
                e[z].appendChild(n[z]);

                Toast.makeText(getActivity(), "e[z]: " + e[z] + "\nn[z]: " + n[z], Toast
                        .LENGTH_SHORT)
                        .show();

                root.appendChild(e[z]);
            }


            //if(!f.exists())
            //{
            d.appendChild(root);
            //}

            TransformerFactory tfactory = TransformerFactory.newInstance();
            Transformer t = tfactory.newTransformer();

            DOMSource source = new DOMSource(d);

            String filename = Environment.getExternalStorageDirectory() + "/" +
                    "datastorage_t1_questionpaper.xml";
            FileWriter writer = new FileWriter(filename);

            StreamResult result = new StreamResult(writer);

            t.transform(source, result);
            Toast.makeText(getActivity().getBaseContext(), "Added to question paper", Toast.LENGTH_SHORT)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Use this method when question paper file already exists

    public void modifyXMLFile(String tag, String text, int count, ArrayList<String> arrlist) {
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
                Element child3_count = doc.createElement("labelcount");

                Node n1 = doc.createTextNode(tag);
                Node n2 = doc.createTextNode(text);
                Node n3 = doc.createTextNode(String.valueOf(count));


                child1_tag.appendChild(n1);
                child2_text.appendChild(n2);
                child3_count.appendChild(n3);

                root3.appendChild(child1_tag);
                root3.appendChild(child2_text);
                root3.appendChild(child3_count);

                Element[] e = new Element[count];
                Node[] n = new Node[count];

                Toast.makeText(getActivity(), "e: " + e[0] + "\nn: " + n[0], Toast
                        .LENGTH_SHORT)
                        .show();

                for (int z = 0; z < count; z++) {
                    e[z] = doc.createElement("location");
                    e[z].setAttribute("id", String.valueOf(count)); // <location id="1">

                    //string array needed for all edit text coords
                    n[z] = doc.createTextNode(arrlist.get(z)); //node: (left, top)

                    Toast.makeText(getActivity(), "e[z]: " + e[z] + "\nn[z]: " + n[z], Toast
                            .LENGTH_SHORT)
                            .show();
                }

                for (int z = 0; z < count; z++) {
                    e[z].appendChild(n[z]);

                    Toast.makeText(getActivity(), "e[z]: " + e[z] + "\nn[z]: " + n[z], Toast
                            .LENGTH_SHORT)
                            .show();

                    root3.appendChild(e[z]);
                }

                main.appendChild(root3);

            } else {

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

                    if ("labelcount".equals(node.getNodeName())) {
                        node.setTextContent(String.valueOf(count));
                    }

                    /*if ("location".equals(node.getNodeName())) {
                        node.setTextContent(String.valueOf(al0));
                    }if ("rightmargin".equals(node.getNodeName())) {
                        node.setTextContent(String.valueOf(al1));
                    }if ("topmargin".equals(node.getNodeName())) {
                        node.setTextContent(String.valueOf(al2));
                    }if ("bottommargin".equals(node.getNodeName())) {
                        node.setTextContent(String.valueOf(al3));
                    }*/ //FIGURE THIS UPDATE OUT!!

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

//                Toast.makeText(getActivity(), "x,y: "+_xDelta + " " + _yDelta, Toast.LENGTH_SHORT).show();

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

                lMgn = layoutParams.leftMargin;
                tMgn = layoutParams.topMargin;

                coords = "(" + lMgn + "," + tMgn + ")";

                break;
        }
        rl.invalidate(); //CAN DELETE??
        return false; //returning true does not invoke long click
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

    @Override
    public boolean onLongClick(View v) {

        Toast.makeText(getActivity(), "Long press", Toast.LENGTH_SHORT).show();
        registerForContextMenu(tvLabel);
        //openContextMenu(tvLabel); //cannot resolve

        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo
            menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // Create your context menu here
        Toast.makeText(getActivity(), "not opened", Toast.LENGTH_SHORT).show(); //not showing
        menu.setHeaderTitle("Context Menu");
        menu.add(0, v.getId(), 0, "Delete Label");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Call your function to preform for buttons pressed in a context menu
        // can use item.getTitle() or similar to find out button pressed
        // item.getItemID() will return the v.getID() that we passed before
        final AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        switch (item.getItemId()) {

            case 1:

                //pop up alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder
                        .setTitle("Delete Label")
                        .setMessage("Are you sure?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Yes button clicked, do something
                                //DELETE QUESTION FROM XML AND UPDATE

//                                delete(acmi.id);
                                try {

                                    count--;
                                    tvLabel.setVisibility(View.GONE);
                                    //rl.removeView(tvLabel);
                                    Toast.makeText(getActivity(), "Label deleted", Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), "dyn tv: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("No", null)						//Do nothing on no
                        .show();

                return true;

            default:
                return super.onContextItemSelected(item);
        }

    } //on context item selected closed


}
