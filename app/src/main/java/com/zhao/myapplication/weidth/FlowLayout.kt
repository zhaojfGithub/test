package com.zhao.myapplication.weidth

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup


/**
 *创建时间： 2022/10/21
 *编   写：  zjf
 *页面功能:
 */
class FlowLayout : ViewGroup {

    // 存储所有子View
    private val mAllChildViews: MutableList<MutableList<View>> = ArrayList()

    // 每一行的高度
    private val mLineHeight: MutableList<Int> = ArrayList()

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?) : this(context, null)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // TODO Auto-generated method stub
        // 父控件传进来的宽度和高度以及对应的测量模式
        val sizeWidth = MeasureSpec.getSize(widthMeasureSpec)
        val modeWidth = MeasureSpec.getMode(widthMeasureSpec)
        val sizeHeight = MeasureSpec.getSize(heightMeasureSpec)
        val modeHeight = MeasureSpec.getMode(heightMeasureSpec)
        // 如果当前ViewGroup的宽高为wrap_content的情况
        var width = 0 // 自己测量的宽度
        var height = 0 // 自己测量的高度
        var lineWidth = 0 // 每一行的宽度
        var lineHeight = 0 // 每一行的高度
        val childCount = childCount // 获取子view的个数
        for (i in 0 until childCount) {
            Log.e("FlowLayout","${i}")
            val child = getChildAt(i)
            // 测量子View的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            // 得到LayoutParams
            val lp = child.layoutParams as MarginLayoutParams
            // 得到子View占据的宽度
            val childWidth = (child.measuredWidth + lp.leftMargin + lp.rightMargin)
            // 得到子View占据的高度
            val childHeight = (child.measuredHeight + lp.topMargin + lp.bottomMargin)
            if (lineWidth + childWidth > sizeWidth) { // 需要进行换行+
                Log.e("FlowLayout","换行间隔${i}")
                width = Math.max(width, lineWidth) // 得到最大宽度 857 218 1075
                lineWidth = childWidth // 重置lineWidth
                height += lineHeight // 得到高度
                lineHeight = childHeight // 重置LineHeight
            } else { // 不需要进行换行
                lineWidth += childWidth // 叠加行宽
                lineHeight = Math.max(lineHeight, childHeight)
            }
            if (i == childCount - 1) { // 处理最后一个子View的情况
                width = Math.max(width, lineWidth)
                height += lineHeight
            }
        }
        // wrapcontent
        setMeasuredDimension(
            if (modeWidth == MeasureSpec.EXACTLY) sizeWidth else width,
            if (modeHeight == MeasureSpec.EXACTLY) sizeHeight else height)
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("FlowLayout",height.toString())
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // TODO Auto-generated method stub
        mAllChildViews.clear()
        mLineHeight.clear()
        val width = width // 获取当前ViewGroup宽度
        var lineWidth = 0
        var lineHeight = 0
        var lineViews: MutableList<View> = ArrayList() // 记录当前行的View
        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child
                .layoutParams as MarginLayoutParams
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight
            // 需要换行
            if (lineWidth + childWidth + lp.leftMargin + lp.rightMargin > width) {
                mLineHeight.add(lineHeight) // 记录lineHeight
                mAllChildViews.add(lineViews) // 记录当前行的Views
                // 重置 行的宽高
                lineWidth = 0
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin
                // 重置当前行的View集合；
                lineViews = ArrayList()
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin
            lineHeight = Math.max(
                lineHeight, childHeight + lp.topMargin
                        + lp.bottomMargin
            )
            lineViews.add(child)
        }
        // 处理最后一行
        mLineHeight.add(lineHeight)
        mAllChildViews.add(lineViews)
        // 设置子View的位置
        var left = 0
        var top = 0
        // 获取行数
        val lineCount = mAllChildViews.size
        for (i in 0 until lineCount) {
            // 当前行的views和高度
            lineViews = mAllChildViews[i]
            lineHeight = mLineHeight[i]
            for (j in lineViews.indices) {
                val child = lineViews[j]
                // 判断是否显示
                if (child.visibility == GONE) {
                    continue
                }
                val lp = child
                    .layoutParams as MarginLayoutParams
                val cLeft = left + lp.leftMargin
                val cTop = top + lp.topMargin
                val cRight = cLeft + child.measuredWidth
                val cBottom = cTop + child.measuredHeight
                // 进行子View进行布局
                child.layout(cLeft, cTop, cRight, cBottom)
                left += (child.measuredWidth + lp.leftMargin
                        + lp.rightMargin)
            }
            left = 0
            top += lineHeight
        }

    }

    /**
     * 与当前ViewGroup对应的LayoutParams
     */
    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams? {
        return MarginLayoutParams(context, attrs)
    }
}
