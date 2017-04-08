package com.example.lemniscate.lockapp;

/**
 * Created by Lemniscate on 2017/4/8.
 */
public class AppbeanMessageEvent {
    int position;
    boolean isCheck;

    public AppbeanMessageEvent(int position, boolean isCheck) {
        this.position = position;
        this.isCheck = isCheck;
    }
}
