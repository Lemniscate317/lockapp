package com.example.lemniscate.lockapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.io.Console;

/**
 * Created by Lemniscate on 2017/4/8.
 */
public class MyToolbar extends Toolbar implements TapListener.OnDoubleTapListener {
    private OnTwoTapListener mOnTwoTapListener;
    private GestureDetector mDetector;

    public MyToolbar(Context context) {
        this(context,null);
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.toolbarStyle);
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TapListener tapListener = new TapListener(this);
        mDetector = new GestureDetector(context, tapListener);
    }

    public interface OnTwoTapListener{
        void onTwoTap();
    }

    public void OnTwoTapListener(OnTwoTapListener listener) {
        mOnTwoTapListener = listener;
    }


    @Override
    public void onDouble() {
        mOnTwoTapListener.onTwoTap();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        mDetector.onTouchEvent(ev);
        return true;
    }
}
