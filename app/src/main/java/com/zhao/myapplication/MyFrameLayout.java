package com.zhao.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 创建时间： 2022/6/29
 * 编   写：  zjf
 * 页面功能:
 */
public class MyFrameLayout extends FrameLayout {
    public MyFrameLayout(@NonNull Context context) {
        this(context,null);
    }

    public MyFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //Log.e("MyFrameLayout","ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.e("MyFrameLayout","ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                //Log.e("MyFrameLayout","ACTION_UP");
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
