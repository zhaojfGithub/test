package com.zhao.myapplication.weidth

import android.graphics.*
import android.graphics.drawable.Drawable
import kotlin.math.sin

/**
 *创建时间： 2022/6/17
 *编   写：  zjf
 *页面功能:
 */
class BallDrawable : Drawable() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#D2691E")
    }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 1f
        color = Color.BLACK
    }

    override fun draw(canvas: Canvas) {
        val radius = bounds.width().toFloat()
        canvas.drawCircle(
            bounds.width().toFloat() / 2,
            bounds.width().toFloat() / 2,
            radius, paint
        )
        canvas.drawLine(
            bounds.width().toFloat(),
            0F,
            bounds.height().toFloat(),
            bounds.width().toFloat(),
            linePaint
        )
        val path = Path()
        val sinValue = sin(Math.toRadians(45.0)).toFloat()
        //left
        path.moveTo(radius - sinValue * radius, radius - sinValue * radius)
        path.cubicTo(
            radius + sinValue * radius,
            radius - sinValue * radius,
            radius,
            radius,
            radius + sinValue * radius,
            radius + sinValue * radius
        )
        //right
        path.moveTo(radius + sinValue * radius, radius - sinValue * radius)
        path.cubicTo(
            radius + sinValue * radius,
            radius - sinValue * radius,
            radius,
            radius,
            radius + sinValue + radius,
            radius + sinValue + radius
        )
        canvas.drawPath(path,linePaint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return when (paint.alpha) {
            0xff -> PixelFormat.OPAQUE
            0x00 -> PixelFormat.TRANSPARENT
            else -> PixelFormat.TRANSLUCENT
        }
    }
}