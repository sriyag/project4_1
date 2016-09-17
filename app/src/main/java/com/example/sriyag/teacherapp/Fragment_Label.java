package com.example.sriyag.teacherapp;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by sriyag on 16/04/16.
 */
public class Fragment_Label extends Fragment {

    private Matrix matrix = new Matrix();
    private float scale = 1f;
    private ScaleGestureDetector SGD;

    private ImageView myImageView;
    private EditText et1;
    String qn;

    private final String imageInSD = Environment.getExternalStorageDirectory()
            + "/skeleton2.png";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_label, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String labQs = getArguments().getString("lab");
        int numberOfLabels = getArguments().getInt("number_of_labels");
        qn = getArguments().getString("questionnumber");

        LinearLayout linlay2 = (LinearLayout) getActivity().findViewById(R.id.linlay_inner);
        TextView tvLabel = (TextView) getActivity().findViewById(R.id.tvLabelQs);
        tvLabel.setText(qn + ". " + labQs);

        final ScrollView sv = (ScrollView) getActivity().findViewById(R.id.sv1);

        SGD = new ScaleGestureDetector(getActivity(), new ScaleListener());

        for (int i = 0; i<numberOfLabels; i++) {
            et1 = new EditText(getActivity());
            et1.setHeight(50);
            et1.setWidth(1000);
            et1.setId(i);
            int j = i+1;
            et1.setText(j + ". ");

            linlay2.addView(et1);

        }
        //Code for loading an image from the internal storage on to the image view
        Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
        myImageView = (ImageView) getActivity().findViewById(R.id.iv_label);
        myImageView.setImageBitmap(bitmap);

    }

    public boolean onTouchEvent(MotionEvent ev) {
        SGD.onTouchEvent(ev);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.

            SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
            scale = Math.max(0.1f, Math.min(scale, 5.0f));

            matrix.setScale(scale, scale);
            myImageView.setImageMatrix(matrix);
            return true;
        }
    }
}
