package com.example.lemniscate.lockapp;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Lemniscate on 2017/4/8.
 */
public class TapListener extends GestureDetector.SimpleOnGestureListener {
    private OnDoubleTapListener mDoubleTapListener;

    public TapListener(OnDoubleTapListener onDoubleTapListener) {
        mDoubleTapListener = onDoubleTapListener;
    }

    public interface OnDoubleTapListener{
        void onDouble();
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        mDoubleTapListener.onDouble();
        return true;
    }
}
