package com.zhao.myapplication;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import android.widget.Scroller;

import javax.crypto.spec.DESKeySpec;

/**
 * 创建时间： 2022/11/25
 * 编   写：  zjf
 * 页面功能:
 */
public class ScrollLayoutView extends View {

    private static final String TAG = "TestView";

    Paint mPaint;
    Scroller mScroller;
    int mSlop;
    float mLastPositX;
    float mLastPositY;
    VelocityTracker mVelocityTracker;
    static int MIN_FING_VELOCITY = 10;

    public ScrollLayoutView(Context context) {
        this(context, null);
    }

    public ScrollLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mScroller = new Scroller(context);
        mSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mSlop = px2dp(context, (float) mSlop);
        Log.e("ViewTest", String.valueOf(mSlop));
        setClickable(true);
    }

    public void startScrollBy(int dx, int dy) {
        mScroller.forceFinished(true);
        int startX = getScrollX();
        int startY = getScrollY();
        mScroller.startScroll(startX, startY, startX + dx, startY + dy, 1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            Log.d(TAG, "computeScroll: ");
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            if (mScroller.getCurrX() == getScrollX() && mScroller.getCurrY() == getScrollY()) {
                postInvalidate();
            }
        } else {
            Log.d(TAG, "computeScroll is over: ");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
        canvas.save();
        canvas.translate(100, 100);
        canvas.drawCircle(0, 0, 40f, mPaint);
        canvas.drawCircle(50, 50, 40f, mPaint);
        canvas.restore();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                restoreTouchPoint(event);
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) (event.getX() - mLastPositX);
                int deltaY = (int) (event.getY() - mLastPositY);
                if (Math.abs(deltaX) > mSlop || Math.abs(deltaY) > mSlop) {
                    scrollBy(-deltaX, -deltaY);
                    restoreTouchPoint(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000,2000);
                int xVelocity = (int) mVelocityTracker.getXVelocity();
                int yVelocity = (int) mVelocityTracker.getYVelocity();
                Log.d(TAG, "onTouchEvent: xVelocity:"+xVelocity+" yVelocity:"+yVelocity);
                if (Math.abs(xVelocity) > MIN_FING_VELOCITY || Math.abs(yVelocity) > MIN_FING_VELOCITY){
                    xVelocity = Math.abs(xVelocity) > Math.abs(yVelocity) ? -xVelocity : 0;
                    yVelocity = xVelocity == 0 ? -yVelocity : 0;
                    mScroller.fling(getScrollX(),getScrollY(),xVelocity,yVelocity,-1000,1000,-1000,2000);
                    invalidate();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void restoreTouchPoint(MotionEvent event) {
        mLastPositX = event.getX();
        mLastPositY = event.getY();
    }

    int px2dp(Context context,Float pxValue){
       float density = context.getResources().getDisplayMetrics().density;
       return (int) (pxValue / density + 0.5F);
    }
}
