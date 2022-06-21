package com.zhao.myapplication

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

/**
 *创建时间： 2022/6/17
 *编   写：  zjf
 *页面功能:
 */
class TestLayout : ViewGroup {

    constructor(context: Context) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }
}