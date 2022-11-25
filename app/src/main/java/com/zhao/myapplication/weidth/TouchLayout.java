package com.zhao.myapplication.weidth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 创建时间： 2022/11/24
 * 编   写：  zjf
 * 页面功能:
 */
public class TouchLayout extends FrameLayout {

    public TouchLayout(Context context) {
        this(context,null);
    }

    public TouchLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TouchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private static int LAYOUT_SHOW = 1;
    private static int LAYOUT_HIDE  = 2;
    private int nowState = LAYOUT_SHOW;
    private int moveHeight = 300;
    private Double slide = 0.5;


    /**
     *什么时候出发
     * 1.当页面为隐藏状态时，线上滑动 并当拦截
     * 2.当页面为展示状态时，向下滑动 并且内部没有可以消费滑动事件时候进行下滑
     * 当滑动进度大于@slide 时，松手过度到另一种状态
     *
     * 当onInterceptTouchEvent返回ture的时候，只要返回true
     *
     * viewGr
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
