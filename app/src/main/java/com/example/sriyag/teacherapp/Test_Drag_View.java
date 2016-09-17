package com.example.sriyag.teacherapp;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by sriyag on 15/09/16.
 */
public class Test_Drag_View extends Activity implements View.OnTouchListener {

    private ImageView mImageView;
    private EditText et1;
    private ViewGroup mRrootLayout;
    private int _xDelta;
    private int _yDelta;
    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();
    private Button b;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_drag_view);
        mRrootLayout = (ViewGroup) findViewById(R.id.root);
        mImageView = (ImageView) mRrootLayout.findViewById(R.id.imageView);
        et1 = (EditText) mRrootLayout.findViewById(R.id.et1);
        b = (Button) mRrootLayout.findViewById(R.id.btnPlus_Test);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, 150);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(500, 200);
        mImageView.setLayoutParams(layoutParams);
        mImageView.setOnTouchListener(this);
        et1.setLayoutParams(layoutParams2);
        et1.setOnTouchListener(this);

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EditText et = new EditText(getApplicationContext());
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(400, 150);
                    et.setHint("Label");
                    et.setText("Label");
                    et.setWidth(400);
                    et.setHeight(150);
                    et.setLayoutParams(layoutParams);
                    et.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            final int X = (int) event.getRawX();
                            final int Y = (int) event.getRawY();
                            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                                case MotionEvent.ACTION_DOWN:
                                    RelativeLayout.LayoutParams lParams = (RelativeLayout
                                            .LayoutParams) mRrootLayout.getLayoutParams();
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
                                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout
                                            .LayoutParams) mRrootLayout
                                            .getLayoutParams();
                                    layoutParams.leftMargin = X - _xDelta;
                                    layoutParams.topMargin = Y - _yDelta;
                                    layoutParams.rightMargin = -250;
                                    layoutParams.bottomMargin = -250;
                                    mRrootLayout.setLayoutParams(layoutParams);
                                    break;
                            }
                            mRrootLayout.invalidate();
                            return true;
                        }
                    });
                    et.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "etSet" + et.getText().toString() +
                                    et.findFocus(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "catch btnPlus: " + e.getMessage(), Toast
                            .LENGTH_SHORT).show();
                }

            }
        });

        //test zoom functionality along with scroll view !!!!!!!!!!!!!!!
    }

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
        mRrootLayout.invalidate();
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.
            SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
            matrix.setScale(scaleFactor, scaleFactor);
            mImageView.setImageMatrix(matrix);
            return true;
        }
    }
}
