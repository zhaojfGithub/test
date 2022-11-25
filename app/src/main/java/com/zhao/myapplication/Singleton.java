package com.zhao.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 创建时间： 2022/6/30
 * 编   写：  zjf
 * 页面功能:
 */
public class Singleton extends ScrollView {


    public Singleton(Context context) {
        super(context);
    }

    public Singleton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Singleton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Singleton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
