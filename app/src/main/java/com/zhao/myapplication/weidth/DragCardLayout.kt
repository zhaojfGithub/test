package com.zhao.myapplication.weidth

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.cardview.widget.CardView
import kotlin.math.abs

/**
 *创建时间： 2022/8/26
 *编   写：  zjf
 *页面功能: 可以进拖拽的一个Layout
 */
class DragCardLayout : CardView {

    //可以滑动的最大距离
    private var maxDistance = 400F

    private var initTop = 0F
    private var maxTop = 0F

    //当前控件需要移动的距离
    private var nowMoveDistance = 0F

    //上一个的rawY
    private var lastY = 0F

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        isClickable = true
        isLongClickable = true
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        initTop = top.toFloat()
        maxTop = initTop - maxDistance
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastY = event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                if (lastY > event.y && top.toFloat() in maxTop..initTop) {
                    nowMoveDistance = event.rawY - lastY
                    offsetTopAndBottom(nowMoveDistance.toInt())
                    lastY = event.rawY
                    return true
                } else if (lastY < event.y && top.toFloat() in maxTop..initTop) {
                    nowMoveDistance = event.rawY - lastY
                    offsetTopAndBottom(nowMoveDistance.toInt())
                    lastY = event.rawY
                    return true
                }
            }
            MotionEvent.ACTION_UP -> {
                startAnimation()
                return true
            }
        }
        return super.dispatchTouchEvent(event)
    }


    private fun startAnimation() {
        val diffMax = maxTop - top
        val diffInit = initTop - top
        val animationMove: Float = if (abs(diffInit) >= abs(diffMax)) {
            diffMax
        } else {
            diffInit
        }
        var animationLast = 0
        val animation = ObjectAnimator.ofFloat(0F, animationMove).setDuration(100)
        animation.addUpdateListener {
            val animatedValue = (it.animatedValue as Float).toInt()
            offsetTopAndBottom(animatedValue - animationLast)
            animationLast = animatedValue
        }
        animation.start()
    }


    fun setMaxDistance(number: Float) {
        this.maxDistance = number
    }
}
