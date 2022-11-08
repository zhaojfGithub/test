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
import androidx.core.content.ContextCompat
import com.zhao.myapplication.R

/**
 *创建时间： 2022/6/7
 *编   写：  zjf
 *页面功能:
 */
class LoadingView : View {

    private lateinit var paint: Paint
    private lateinit var rectF: RectF
    private lateinit var array: Array<RotateEntity>
    //private lateinit var drawArray: Array<RotateEntity>

    private val defaultStart = 0
    private val defaultSweepAngle = 30
    private var isDraw = false
    private var commonStart = 0
    private var commonAngle = 0
    private var isCirEnd = false

    constructor(context: Context) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        paint = Paint().apply {
            isAntiAlias = false
            color = ContextCompat.getColor(context, R.color.purple_700)
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 30F
        }
        rectF = RectF()
        /*//初始化数据
        sumArray = Array(2) {
            Array(6) {
                RotateEntity(it, 0F, 0F, 0F, 0F)
            }
        }
        drawArray = Array(6) { sumArray[0][it] }*/


        array = Array(4) {
            RotateEntity(it, 0, 0, 0, 0)
        }
        for (index in array.indices) {
            val entity = array[index]
            if (index == 0) {
                entity.start = defaultStart
            } else {
                val beforeEntity = array[index - 1]
                entity.start = beforeEntity.start + beforeEntity.sweepAngle + defaultSweepAngle
            }
            /*if (index == array.size - 1) {
                entity.sweepAngle = 0
            } else {*/
            entity.sweepAngle = defaultSweepAngle
            //}
            entity.useStart = entity.start
            entity.useSweepAngle = entity.sweepAngle
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        isDraw = true
        rectF.set(15F, 15F, width.toFloat() - 15, height.toFloat() - 15)
        val startEntity = array[0]
        val endEntity = array[array.size - 1]
        if (isCirEnd){
            if (endEntity.useStart + endEntity.useSweepAngle > startEntity.useStart) {
                commonStart = endEntity.useStart + endEntity.useSweepAngle
                commonAngle = 360 - endEntity.useStart + endEntity.useSweepAngle
                if (commonAngle < 180) {
                    commonStart = 0
                    commonAngle = 180 - commonAngle
                }
            } else {
                commonStart = startEntity.useStart + endEntity.useSweepAngle
                commonAngle = 360 - startEntity.useStart + endEntity.useSweepAngle
                if (commonAngle < 180) {
                    commonStart = 0
                    commonAngle = 180 - commonAngle
                }
            }
        }


        array.forEach {
            canvas.drawArc(rectF, it.useStart.toFloat(), it.useSweepAngle.toFloat(), false, paint)
            //Log.e("Loading${it.id}", "start = ${it.useStart},sweepAngle = ${it.useSweepAngle}")
        }
        isDraw = false

        //canvas.rotate()
    }

    fun start() {
        val valueAnimator: ValueAnimator = ValueAnimator.ofInt(0, 30)
        valueAnimator.addUpdateListener {
            if (isDraw) return@addUpdateListener
            val plan = it.animatedValue as Int
            //Log.e("LoadingView", plan.toString())
            for (index in array.indices) {
                val bean = array[index]
                /*if (index == array.size - 1) {
                    bean.useSweepAngle = bean.sweepAngle - plan
                }*//* else if (index == 0) {
                    bean.useStart = bean.start.plus(plan)
                    bean.useSweepAngle = bean.sweepAngle.minus(plan)
                } *///else {
                bean.useStart = bean.start.plus(plan)
                // }
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

            override fun onAnimationRepeat(animation: Animator?) {
                Log.e("Loading", "一个轮回")
                val bean = array[array.size - 1]
                array.forEach {
                    Log.e("Loading${it.id}", "start = ${it.start},sweepAngle = ${it.sweepAngle} useStart = ${it.useStart} useSweepAngle = ${it.useSweepAngle}" )
                }
                Log.e("Loading", "修改")
                for (index in array.size -1 downTo  0) {
                    Log.e("Loading", index.toString())
                    /*if (index - 1 > 0) {
                        array[index] = array[index - 1]
                    } else {
                        //等于0
                        bean.useStart = array[index + 1].useStart - 60
                        array[index] = bean
                    }*/

                    if (array[index].useStart >= 360F) {
                        array[index].useStart = array[index].useStart - 360
                    }
                    array[index].start = array[index].useStart
                    array[index].sweepAngle = array[index].useSweepAngle
                }
                array.forEach {
                    Log.e("Loading${it.id}", "start = ${it.start},sweepAngle = ${it.sweepAngle} useStart = ${it.useStart} useSweepAngle = ${it.useSweepAngle}" )
                }
                //animation?.end()
            }

        })
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.repeatCount = Animation.INFINITE
        valueAnimator.duration = 5000
        valueAnimator.start()
    }

    private data class RotateEntity(
        val id: Int,
        var start: Int,
        var useStart: Int,
        var sweepAngle: Int,
        var useSweepAngle: Int,
    )
}

