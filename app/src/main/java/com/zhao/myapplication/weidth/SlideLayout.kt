package com.zhao.myapplication.weidth

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout

/**
 *创建时间： 2022/10/26
 *编   写：  zjf
 *页面功能:
 */
class SlideLayout : LinearLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    /**
     * 这里简单写一下思路
     * 1.如果上滑，View处于完全展示，那么先给子View消费，无法消费后return掉
     * 2.如果下滑，View处于完全展示，那么给子View消费，再给layout消费，最后return
     */

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }
}