package com.zhao.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 创建时间： 2022/6/23
 * 编   写：  zjf
 * 页面功能:
 */
public class SunView extends View {

    private final Paint mPaint;
    private final Paint mLinPaint;
    private final Path mPath;
    private final Matrix mMatrix = new Matrix();
    PathMeasure pathMeasure = new PathMeasure(null, false);
    private final int NUMBER = 12;
    private int now = 0;


    public SunView(Context context) {
        this(context, null);
    }

    public SunView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SunView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.YELLOW);
        mPaint.setStrokeWidth(10F);

        mLinPaint = new Paint();
        mLinPaint.setColor(Color.RED);
        mLinPaint.setStyle(Paint.Style.STROKE);
        mLinPaint.setStrokeWidth(6F);

        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        int height = getHeight();
        int width = getWidth();
        int r = 200;
        canvas.drawLine(0F, height / 2F, width, height / 2F, mLinPaint);
        canvas.drawLine(width / 2F, 0F, width / 2F, height, mLinPaint);
        canvas.translate(width / 2F, height / 2F);
        mPath.addCircle(0F, 0F, r, Path.Direction.CW);
        canvas.drawPath(mPath, mPaint);

        mPath.reset();
        for (int i = 0; i < 360; ) {
            double sin = Math.sin(Math.toRadians(i + now));
            double cos = Math.cos(Math.toRadians(i + now));
            float y = (float) (sin * (r + 50));
            float x = (float) (cos * (r + 50));
            mPath.addCircle(x, y, 30, Path.Direction.CW);
            i += 45;
        }
        canvas.drawPath(mPath, mLinPaint);

        now += 1;
        if (now >= 360) {
            now = 0;
        }
        //invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Log.e("SunView","ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.e("SunView","ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                //Log.e("SunView","ACTION_UP");
                break;
        }
        performClick();
        return true;

    }
}
