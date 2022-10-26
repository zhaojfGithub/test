package com.zhao.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat

/**
 *创建时间： 2022/9/28
 *编   写：  zjf
 *页面功能:
 */
class VoiceBounceView : View {

    /**
     * 首先需要三个颜色的画笔
     */
    private lateinit var lightGrayPaint: Paint
    private lateinit var grayPaint: Paint
    private lateinit var bitmapPaint: Paint

    private val lightGrayColor = Color.rgb(171, 171, 171)
    private val grayColor = Color.rgb(191, 191, 191)

    private var iconStartBitmap: Bitmap

    private var onStartClick: (() -> Unit)? = null
    private var onEndClick: (() -> Unit)? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initPaint()
        val drawable = ContextCompat.getDrawable(context, R.mipmap.voice_start)!!
        iconStartBitmap = Bitmap.createBitmap(drawable.intrinsicWidth / 2, drawable.intrinsicHeight / 2, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(iconStartBitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
    }

    private fun initPaint() {
        lightGrayPaint = Paint().apply {
            style = Paint.Style.FILL
            color = lightGrayColor
            isAntiAlias = true
            strokeWidth = 2F
        }
        grayPaint = Paint().apply {
            style = Paint.Style.FILL
            color = grayColor
            isAntiAlias = true
            strokeWidth = 2F
        }
        bitmapPaint = Paint()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(MeasureSpec.EXACTLY, 400)
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(MeasureSpec.EXACTLY, 400)
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, 400)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawArc(0F - height / 3F, 0F, width + height / 3F, height.toFloat() + height, 210F, 333F, false, lightGrayPaint)
        canvas.drawArc(0F - height / 3F + 20F, 0F + 20F, width + height / 3F - 20F, height.toFloat() + height - 20F, 210F, 333F, false, grayPaint)
        canvas.drawBitmap(iconStartBitmap, width / 2F - iconStartBitmap.width / 2F, height / 2 - iconStartBitmap.height / 2F, bitmapPaint)
    }


    fun setStartOnClick(onStartClick: () -> Unit) {
        this.onStartClick = onStartClick
    }

    fun setEndOnClick(onEndClick: () -> Unit) {
        this.onEndClick = onEndClick
    }

}