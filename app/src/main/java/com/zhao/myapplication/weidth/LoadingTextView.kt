package com.zhao.myapplication.weidth

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
import com.zhao.myapplication.R

/**
 *创建时间： 2022/6/9
 *编   写：  zjf
 *页面功能:
 */
class LoadingTextView : View {

    private lateinit var paint: Paint
    private lateinit var rectF: RectF
    private var rotate: Int = 0

    private val interval = 120

    private var isRefresh = false
    private var rotateWeight = 30

    private lateinit var arcArray: Array<RotateBean>


    constructor(context: Context) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }


    private fun init() {
        paint = Paint()
        rectF = RectF()
        arcArray = Array(2) {
            RotateBean(it, it * 2 * 30, it * 2 * 30, 30)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        rotateWeight = width / 6
        val half: Float = (rotateWeight / 2).toFloat()
        rectF.set(half + 10 , half + 10, width - half - 10, height - half -10)
    }

    /**
     * 思路  画两段圆弧，一个从 0-30 ， 一个从30 -0 ，然后其他
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        settingPaint(Paint.Style.STROKE, R.color.purple_700, 50F, Paint.Cap.ROUND)
        canvas.rotate(-rotate.toFloat(), width / 2F, height / 2F)
        Log.e("LoadingTextView", "先旋转${-rotate.toFloat()}")
        arcArray.forEach {
            canvas.drawArc(rectF, it.useStart.toFloat(), it.sweep.toFloat(), false, paint)
        }
        canvas.rotate((interval).toFloat(), width / 2F, height / 2F)
        canvas.drawArc(rectF, 0F, (360 - interval).toFloat(), false, paint)
    }

    fun start() {
        val valueAnimator = ValueAnimator.ofInt(0, 30)
        valueAnimator.duration = 300
        valueAnimator.repeatCount = Animation.INFINITE
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener { animator ->
            val plain: Int = animator.animatedValue as Int
            if (plain == 0) return@addUpdateListener
            arcArray.forEach {
                it.useStart = it.start + plain
            }
            invalidate()
        }
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {

            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator) {
                animation.pause()
                arcArray.forEach {
                    if (it.useStart != it.start + it.sweep) {
                        it.initData()
                        if (!isRefresh) {
                            isRefresh = !isRefresh
                        }
                    }
                }
                rotate += 30
                if (rotate == 360) {
                    rotate = 0
                }
                if (isRefresh) {
                    invalidate()
                    isRefresh = false
                }
                animation.resume()
            }

        })
        valueAnimator.start()
    }

    private fun settingPaint(sty: Paint.Style, @ColorRes cor: Int, stroke: Float, cap: Paint.Cap) {
        paint.apply {
            reset()
            isAntiAlias = false
            color = ContextCompat.getColor(context, cor)
            style = sty
            strokeCap = cap
            strokeWidth = stroke
            setShadowLayer(10F,10F,5F,ContextCompat.getColor(context, R.color.teal_700))
        }
    }

    private data class RotateBean(
        val id: Int,
        var start: Int,
        var useStart: Int,
        var sweep: Int,
    ) {
        fun initData() {
            useStart = start
        }

    }
}