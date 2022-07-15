package com.zhao.myapplication

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

/**
 *创建时间： 2022/6/8
 *编   写：  zjf
 *页面功能:
 */
class TestView : View {

    private lateinit var paint: Paint
    private lateinit var rectF: RectF
    private var plus = 0
    private var minus = 0
    private var position = 0

    private var cirLow = 0
    private var cirNow = 0

    private var angZero = 0
    private var angOne = 0
    private var angTwo = 0
    private var angThere = 0
    private var angFour = 0

    //用来存储画布的旋转角度
    private val circleArray: Array<Int> = Array(12) { 30 }

    //用来存储画圆弧的大小
    private val angleArray: Array<Int> = Array(12) { 30 }

    constructor(context: Context) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
        initData()
    }

    private fun initData() {
        //控制画布旋转的角度
        var initPosition = 0
        circleArray[0] = 0
        //circleArray[4] = 0
        angleArray[1] = 0
        angleArray[3] = 0
    }

    private fun init() {
        paint = Paint()
        rectF = RectF()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rectF.set(1F, 1F, width - 1F, height - 1F)
        settingPaint(Paint.Style.STROKE, R.color.purple_200, 1F, Paint.Cap.SQUARE)
        canvas.drawRect(rectF, paint)
        circleArray.forEachIndexed { index, i ->
            //先旋转在画
            canvas.rotate(i.toFloat(), width / 2F, height / 2F)
            settingPaint(Paint.Style.STROKE, R.color.purple_200, 15F, Paint.Cap.ROUND)
            canvas.drawArc(15F, 15F, width - 15F, height - 15F, 90F, angleArray[index].toFloat(), false, paint)
            //settingPaint(Paint.Style.FILL, R.color.purple_700, 5F, Paint.Cap.SQUARE)
            //canvas.drawText((index * 30).toString(), width / 2F, height - 100F, paint)

        }
    }

    fun start() {
        val valueAnimator: ValueAnimator = ValueAnimator.ofInt(0, 30)
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.repeatCount = Animation.INFINITE
        valueAnimator.duration = 5000
        valueAnimator.addUpdateListener {
            val plan: Int = it.animatedValue as Int
            if (plan == plus || plan == 0) return@addUpdateListener
            plus = plan
            minus = 30 - plus
            settingData(plus, minus)
            invalidate()
            //mLog("plan = $commonPlan")
        }
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {

            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {
                mLog(circleArray.toString())
                mLog(angleArray.toString())
                settingData(30,0)
                    animation?.end()
            }
        })

        valueAnimator.start()
    }

    private fun settingData(plus: Int, minus: Int) {
        cirLow = disposePosition(position)
        cirNow = disposePosition(position.plus(1))

        if (circleArray[cirLow] != plus) {
            circleArray[cirLow] = plus
        }
        if (circleArray[cirNow] != minus) {
            circleArray[cirNow] = minus
        }


        angZero = disposePosition(position - 1)
        angOne = disposePosition(position)
        angTwo = disposePosition(position + 1)
        angThere = disposePosition(position + 2)
        angFour = disposePosition(position + 3)

        if (angleArray[angZero] == 0) {
            if (angleArray[angZero] != plus){
                angleArray[angZero] = plus
            }

        }
        if (angleArray[angOne] != minus) {
            circleArray[cirNow] = minus
        }
        if (angleArray[angTwo] != plus) {
            angleArray[angTwo] = plus
        }
        if (angleArray[angThere] != minus) {
            angleArray[angThere] = minus
        }
        if (angleArray[angFour] != plus) {
            angleArray[angFour] = plus
        }
    }

    private fun settingPaint(sty: Paint.Style, @ColorRes cor: Int, stroke: Float, cap: Paint.Cap) {
        paint.apply {
            reset()
            isAntiAlias = false
            color = ContextCompat.getColor(context, cor)
            style = sty
            strokeCap = cap
            strokeWidth = stroke
        }
    }

    private fun mLog(msg: String) {
        Log.e("TestView", msg)
    }

    private fun disposePosition(i: Int): Int {
        return when {
            i >= 12 -> {
                i - 12
            }
            i < 0 -> {
                12 + i
            }
            else -> {
                i
            }
        }
    }

}