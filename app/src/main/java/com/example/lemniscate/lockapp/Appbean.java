package com.example.lemniscate.lockapp;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by Lemniscate on 2017/4/8.
 */
public class Appbean {
    String name;
    Drawable src;
    boolean isCheck;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }


    public Drawable getSrc() {
        return src;
    }

    public void setSrc(Drawable src) {
        this.src = src;
    }
}
