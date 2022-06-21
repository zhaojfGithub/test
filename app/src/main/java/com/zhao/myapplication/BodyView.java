package com.zhao.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PointFEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

/**
 * 创建时间： 2022/6/20
 * 编   写：  zjf
 * 页面功能:
 */
public class BodyView extends View {


    /**
     * 气泡默认状态-静止
     */
    private final int BUBBLE_STATE_DEFAULT = 0;

    /**
     * 气泡相连
     */
    private final int BUBBLE_STATE_CONNECT = 1;

    /**
     * 气泡分离
     */
    private final int BUBBLE_STATE_APART = 2;

    /**
     * 气泡消失
     */
    private final int BUBBLE_STATE_DISMISS = 3;

    /**
     * 气泡半径
     */
    private float mBubbleRadius;
    /**
     * 气泡颜色
     */
    private int mBubbleColor;
    /**
     * 气泡消息文字
     */
    private String mTextStr;
    /**
     * 气泡文字颜色
     */
    private int mTextColor;
    /**
     * 气泡文字大小
     */
    private float mTextSize;
    /**
     * 不动气泡的半径
     */
    private float mBubFixedRadius;
    /**
     * 可动气泡的半径
     */
    private float mBubMovableRadius;
    /**
     * 不动气泡的圆心
     */
    private PointF mBubFixedCenter;
    /**
     * 可动气泡的圆心
     */
    private PointF mBubMovableCenter;
    /**
     * 气泡的画笔
     */
    private Paint mBubblePaint;
    /**
     * 贝塞尔曲线的Path
     */
    private Path mBezierPath;

    private Paint mTextPaint;

    /**
     * 文本绘制区域
     */
    private Rect mTextRect;
    private Paint mBursPaint;
    /**
     * 爆炸绘制区域
     */
    private Rect mBurstRect;

    /**
     * 气泡状态标志
     */
    private int mBubbleState = BUBBLE_STATE_DEFAULT;

    /**
     * 两个气泡圆心距离
     */
    private float mDist;

    /**
     * 气泡相连状态最大圆心距离
     */
    private float mMaxDist;

    /**
     * 手指触摸偏移量
     */
    private final float MOVE_OFFSET;

    /**
     * 气泡爆炸的bitmap数组
     */
    private Bitmap[] mBurstBitmapsArray;

    /**
     * 是否正在执行气泡爆炸动画
     */
    private boolean mlsBurstAnimStart = false;

    /**
     * 当前气泡爆炸图片的index
     */
    private int mCurDrawableIndex;

    /**
     * 气泡爆炸的图片id数组
     */
    private int[] mBurstDrawablesArray = {R.mipmap.burst_1, R.mipmap.burst_2, R.mipmap.burst_3, R.mipmap.burst_4, R.mipmap.burst_5};

    public BodyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BodyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BodyView, defStyleAttr, 0);
        mBubbleRadius = array.getDimension(R.styleable.BodyView_bubble_radius, mBubbleRadius);
        mBubbleColor = array.getColor(R.styleable.BodyView_bubble_color, Color.RED);
        mTextStr = array.getString(R.styleable.BodyView_bubble_text);
        mTextSize = array.getDimension(R.styleable.BodyView_bubble_textSize, mTextSize);
        mTextColor = array.getColor(R.styleable.BodyView_bubble_textColor, Color.WHITE);
        array.recycle();

        //两个圆大小一致
        mBubFixedRadius = mBubbleRadius;
        mBubMovableRadius = mBubFixedRadius;
        mMaxDist = 8 * mBubbleRadius;

        MOVE_OFFSET = mMaxDist / 4;

        //抗锯齿
        mBubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBubblePaint.setColor(mBubbleColor);
        mBubblePaint.setStyle(Paint.Style.FILL);
        mBezierPath = new Path();

        //文本画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextRect = new Rect();

        //爆炸画笔
        mBursPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBursPaint.setFilterBitmap(true);
        mBurstRect = new Rect();
        mBurstBitmapsArray = new Bitmap[mBurstDrawablesArray.length];
        for (int i = 0; i < mBurstBitmapsArray.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mBurstDrawablesArray[i]);
            mBurstBitmapsArray[i] = bitmap;
        }
    }


    /**
     * 测量View的大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 确定View的大小
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //不动气泡的圆心
        if (mBubFixedCenter == null) {
            mBubFixedCenter = new PointF(w / 2F, h / 2F);
        } else {
            mBubFixedCenter.set(w / 2F, h / 2F);
        }
        //可动气泡的圆心
        if (mBubMovableCenter == null) {
            mBubMovableCenter = new PointF(w / 2F, h / 2F);
        } else {
            mBubMovableCenter.set(w / 2F, h / 2F);
        }
    }

    /**
     * 确定View的布局
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //1，静止状态，一个气泡加消息数据

        //2, 连接状态，一个气泡加消息数据，贝塞尔曲线，本身位置上气泡，大小可变化

        //3，分离状态，一个气泡加消息数据

        //4，消失状态，爆炸效果

        //气泡连接状态
        if (mBubbleState == BUBBLE_STATE_CONNECT) {
            //绘制不动的气泡
            canvas.drawCircle(mBubFixedCenter.x, mBubFixedCenter.y, mBubFixedRadius, mBubblePaint);
            //绘制贝塞尔曲线
            //绘制坐标点
            int iAnchorX = (int) ((mBubFixedCenter.x + mBubMovableCenter.x) / 2);
            int iAnchorY = (int) ((mBubFixedCenter.y + mBubMovableCenter.y) / 2);

            float sinTheta = (mBubMovableCenter.y - mBubFixedCenter.y) / mDist;
            float cosTheta = (mBubMovableCenter.x - mBubFixedCenter.x) / mDist;

            //B
            float iBubMovableStartX = mBubMovableCenter.x + sinTheta * mBubMovableRadius;
            float iBubMovableStartY = mBubMovableCenter.y - cosTheta * mBubMovableRadius;

            //C
            float iBubMovableEndX = mBubMovableCenter.x - mBubMovableRadius * sinTheta;
            float iBubMovableEndY = mBubMovableCenter.y + mBubMovableRadius * cosTheta;

            //A
            float iBubFixedEndX = mBubFixedCenter.x + mBubFixedRadius * sinTheta;
            float iBubFixedEndY = mBubFixedCenter.y - mBubFixedRadius * cosTheta;

            //D
            float iBubFixedStartX = mBubFixedCenter.x - mBubFixedRadius * sinTheta;
            float iBubFixedStartY = mBubFixedCenter.y + mBubFixedRadius * cosTheta;

            mBezierPath.reset();
            //从C到D 以两者圆中心为控制点画贝塞尔曲线
            mBezierPath.moveTo(iBubFixedStartX, iBubFixedStartY);
            mBezierPath.quadTo(iAnchorX, iAnchorY, iBubMovableEndX, iBubMovableEndY);
            //从B到C 以两者圆中心为控制点画贝塞尔曲线
            mBezierPath.lineTo(iBubMovableStartX, iBubMovableStartY);
            mBezierPath.quadTo(iAnchorX, iAnchorY, iBubFixedEndX, iBubFixedEndY);
            mBezierPath.close();
            canvas.drawPath(mBezierPath, mBubblePaint);
        }

        //只要不是气泡消失都需要数字
        if (mBubbleState != BUBBLE_STATE_DISMISS) {
            canvas.drawCircle(mBubMovableCenter.x, mBubMovableCenter.y, mBubMovableRadius, mBubblePaint);
            mTextPaint.getTextBounds(mTextStr, 0, mTextStr.length(), mTextRect);
            canvas.drawText(mTextStr, mBubMovableCenter.x - mTextRect.width() / 2F, mBubMovableCenter.y + mTextRect.height() / 2F, mTextPaint);
        }

        if (mBubbleState == BUBBLE_STATE_DISMISS && mCurDrawableIndex < mBurstBitmapsArray.length) {
            mBurstRect.set(
                    (int) (mBubMovableCenter.x - mBubMovableRadius),
                    (int) (mBubMovableCenter.y - mBubMovableRadius),
                    (int) (mBubMovableCenter.x + mBubMovableRadius),
                    (int) (mBubMovableCenter.y + mBubMovableRadius)
            );
            canvas.drawBitmap(mBurstBitmapsArray[mCurDrawableIndex], null, mBurstRect, mBubblePaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //点击的时候
                if (mBubbleState != BUBBLE_STATE_DISMISS) {
                    //Math.hypot() 返回所有参数的平方和的平方根 平方相加再开根号
                    mDist = (float) Math.hypot(event.getX() - mBubFixedCenter.x, event.getY() - mBubFixedCenter.y);
                    if (mDist < mBubbleRadius + MOVE_OFFSET) {
                        mBubbleState = BUBBLE_STATE_CONNECT;
                    } else {
                        mBubbleState = BUBBLE_STATE_DEFAULT;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mBubbleState != BUBBLE_STATE_DEFAULT) {
                    mDist = (float) Math.hypot(event.getX() - mBubFixedCenter.x, event.getY() - mBubFixedCenter.y);
                    mBubMovableCenter.x = event.getX();
                    mBubMovableCenter.y = event.getY();
                    if (mBubbleState == BUBBLE_STATE_CONNECT) {
                        if (mDist < mMaxDist - MOVE_OFFSET) {
                            //当拖拽距离还在指定范围内，那么调整不动气泡的半径
                            mBubFixedRadius = mBubbleRadius - mDist / 8;
                        } else {
                            //超过了距离就改为飞离状态
                            mBubbleState = BUBBLE_STATE_APART;
                        }
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mBubbleState == BUBBLE_STATE_CONNECT) {
                    //橡皮筋动画效果
                    startBubbleRestAnim();
                } else if (mBubbleState == BUBBLE_STATE_APART) {
                    if (mDist < 2 * mBubbleRadius) {
                        startBubbleRestAnim();
                    } else {
                        startBubbleBurstAnim();
                    }
                }
                break;
        }
        performClick();
        return true;
    }

    //爆炸
    private void startBubbleBurstAnim() {
        mBubbleState = BUBBLE_STATE_DISMISS;
        ValueAnimator anim = ValueAnimator.ofInt(0, mBurstBitmapsArray.length);
        anim.setDuration(500);
        anim.setInterpolator(new LinearInterpolator());
        anim.addUpdateListener(animation -> {
            mCurDrawableIndex = (int) animation.getAnimatedValue();
            invalidate();
        });
        anim.start();
    }

    private void startBubbleRestAnim() {
        ValueAnimator anim = ValueAnimator.ofObject(new PointFEvaluator(), new PointF(mBubMovableCenter.x, mBubMovableCenter.y), new PointF(mBubFixedCenter.x, mBubFixedCenter.y));
        anim.setDuration(200);
        anim.setInterpolator(new OvershootInterpolator(5F));
        anim.addUpdateListener(animation -> {
            mBubMovableCenter = (PointF) animation.getAnimatedValue();
            invalidate();
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBubbleState = BUBBLE_STATE_DEFAULT;
            }
        });
        anim.start();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }
}
