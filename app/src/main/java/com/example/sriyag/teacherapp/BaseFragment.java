package com.example.sriyag.teacherapp;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by sriyag on 18/11/16.
 */
public class BaseFragment implements TextWatcher {

    public enum QuesState {LOADED, EDITED, SAVED};

    private BaseFragment textWatcher = null;

    QuesState state;
    TextView labelForUser;

    BaseFragment(TextView tv){
        state = QuesState.EDITED;
        labelForUser = tv;
    }
    public void setState(QuesState st){
        this.state = st;
    }

    public QuesState getState(){
        return state;
    }
    public void afterTextChanged(Editable s) {
        state = QuesState.EDITED;
        labelForUser.setText("Unsaved"); //modified
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before,
                              int count) {
    }

    public void addListener (EditText et) {
        et.addTextChangedListener(textWatcher);
    }
    public void removeListener (EditText et) {
        et.removeTextChangedListener(textWatcher);
    }

    public void onDetach() {
        if(textWatcher.getState() == QuesState.EDITED){
            // PUT logic to prompt user on saving / discarding
            //pop up alert dialog
        }
    }

}
