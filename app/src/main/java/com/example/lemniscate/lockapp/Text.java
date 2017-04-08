package com.example.lemniscate.lockapp;

import android.widget.TextView;

/**
 * Created by Lemniscate on 2017/4/8.
 */
public class Text {
    String text;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Text(String text) {

        this.text = text;
    }
}
