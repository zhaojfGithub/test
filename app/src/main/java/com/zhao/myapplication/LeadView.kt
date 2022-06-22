package com.zhao.myapplication

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 *创建时间： 2022/6/8
 *编   写：  zjf
 *页面功能:
 */
class LeadView : View {

    private var mPaint: Paint
    private var mLinPaint: Paint
    private var mBitmap: Bitmap

    constructor(context: Context) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mPaint = Paint().apply {
            style = Paint.Style.STROKE
            color = Color.BLACK
            strokeWidth = 4F
        }
        mLinPaint = Paint().apply {
            style = Paint.Style.STROKE
            color = Color.RED
            strokeWidth = 6F
        }

        val options = BitmapFactory.Options()
        options.inSampleSize = 4
        mBitmap = BitmapFactory.decodeResource(resources, R.mipmap.arrow, options)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(0F, height / 2F, width.toFloat(), height / 2F, mLinPaint)
        canvas.drawLine(width / 2F, 0F, width / 2F, height.toFloat(), mLinPaint)
        canvas.translate(width / 2F, height / 2F)

        mPath.reset()
        mPath.addCircle(0F, 0F, 200F, Path.Direction.CW)
        mPath.moveTo(200F, 200F)
        mPath.lineTo(200F, -200F)
        canvas.drawPath(mPath, mPaint)
        val pathMeasure = PathMeasure(mPath, false)
//        pathMeasure.getPosTan(pathMeasure.length * 5 / 6, pos, tan)
//        Log.e("TAG", pathMeasure.length.toString())
//        Log.e("TAG", "onDraw: pos[0]=" + pos[0] + ";pos[1]=" + pos[1])
//        Log.e("TAG", "onDraw: tan[0]=" + tan[0] + ";tan[1]=" + tan[1])
//        Log.e("TAG", Math.atan2(tan[0].toDouble(), tan[1].toDouble()).toString())
//        val degrees = Math.atan2(tan[0].toDouble(), tan[1].toDouble()) * 180 / Math.PI
//        Log.e("TAG", degrees.toString())
        mFloat += 0.01F
        if (mFloat >= 1) {
            mFloat = 0F
        }
        mMatrix.reset()
        //canvas.drawBitmap(mBitmap,null,RectF(0F,0F,200F,200F),mPaint)
        pathMeasure.getMatrix(pathMeasure.length * mFloat, mMatrix, PathMeasure.POSITION_MATRIX_FLAG or PathMeasure.TANGENT_MATRIX_FLAG)
        mMatrix.preTranslate(-mBitmap.width / 2F, -mBitmap.height / 2F)
        canvas.drawBitmap(mBitmap,mMatrix,mPaint)
        invalidate()
    }

    private var mFloat: Float = 0F
    private val mPath: Path by lazy { Path() }
    private val pos = floatArrayOf(0F, 0F)
    private val tan = floatArrayOf(0F, 0F)
    private val mMatrix: Matrix by lazy { Matrix() }


}