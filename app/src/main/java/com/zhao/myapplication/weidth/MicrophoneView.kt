package com.zhao.myapplication.weidth

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.zhao.myapplication.R


/**
 *创建时间： 2022/9/20
 *编   写：  zjf
 *页面功能:
 */
@RequiresApi(Build.VERSION_CODES.M)
class MicrophoneView : View {

    private lateinit var paint: Paint

    private lateinit var bluePaint: Paint

    private val color: Int = R.color.teal_200

    private var number = 2

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initPaint()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initPaint() {
        paint = Paint().apply {
            style = Paint.Style.STROKE
            color = color
            isAntiAlias = true
            strokeWidth = 5f
        }
        bluePaint = Paint().apply {
            style = Paint.Style.STROKE
            color = resources.getColor(this@MicrophoneView.color, context.theme)
            isAntiAlias = true
            strokeWidth = 5f
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(0F, height / 2F, width.toFloat(), height / 2F, paint)
        canvas.drawLine(width / 2F, 0F, width / 2F, height.toFloat(), paint)
        val space = width / 2 / 5F
        val initx = height / 2F
        val inity = width / 2F
        if (number == 5) {
            number = 2
        }
        for (i in 2..number) {
            canvas.drawArc(initx - (space * i), inity - (space * i), initx + (space * i), inity + (space * i), 330F, 60F, false, paint)
        }
        number
    }

    private fun start(){
        val objectAnimator = ObjectAnimator.ofInt(1,8)
    }



}