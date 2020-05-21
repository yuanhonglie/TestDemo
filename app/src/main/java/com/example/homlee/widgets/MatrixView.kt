package com.example.homlee.widgets

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator

const val SCALE_ANIMATOR_DURATION = 2000
const val CUBE_WIDTH = 100

class MatrixView(context: Context, attrs: AttributeSet?, defStyleAttr: Int): View(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context): this(context, null)

    private lateinit var bgBitmap: Bitmap
    private lateinit var cubeBitmap: Bitmap

    var displayMatrix: Matrix = Matrix()

    private val bmpPaint: Paint = Paint(Paint.FILTER_BITMAP_FLAG)


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bgBitmap = createBgBitmap(w, h)
        cubeBitmap = createCubeBitmap()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            if (bgBitmap != null) {
                canvas.drawBitmap(bgBitmap, 0f, 0f, bmpPaint)
            }

            if (cubeBitmap != null) {
                canvas.drawBitmap(cubeBitmap, displayMatrix, bmpPaint)
            }

        }
    }


    fun reset() {
        displayMatrix.reset()
        invalidate()
    }

    fun animateToMatrix(endMatrix: Matrix) {
        val scaleAnimator = ScaleAnimator(displayMatrix, endMatrix)
        scaleAnimator.start()
    }

    private fun createCubeBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(CUBE_WIDTH, CUBE_WIDTH, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(0xff00ff00.toInt())
        return bitmap
    }

    private fun createBgBitmap(width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.GRAY
        val canvas = Canvas(bitmap)

        var w = 0
        while (w < width) {
            canvas.drawLine(w.toFloat(), 0f, w.toFloat(), height.toFloat(), paint)
            w += CUBE_WIDTH
        }

        var h = 0
        while (h < height) {
            canvas.drawLine(0f, h.toFloat(), width.toFloat(), h.toFloat(), paint)
            h += CUBE_WIDTH
        }

        return bitmap
    }

    /**
     * 缩放动画
     *
     * 在给定时间内从一个矩阵的变化逐渐动画到另一个矩阵的变化
     */
    private inner class ScaleAnimator
    /**
     * 构建一个缩放动画
     *
     * 从一个矩阵变换到另外一个矩阵
     *
     * @param start 开始矩阵
     * @param end 结束矩阵
     * @param duration 动画时间
     */
    @JvmOverloads constructor(start: Matrix, end: Matrix, duration: Long = SCALE_ANIMATOR_DURATION.toLong()) : ValueAnimator(), ValueAnimator.AnimatorUpdateListener {

        /**
         * 开始矩阵
         */
        private val mStart = FloatArray(9)

        /**
         * 结束矩阵
         */
        private val mEnd = FloatArray(9)

        /**
         * 中间结果矩阵
         */
        private val mResult = FloatArray(9)

        init {
            setFloatValues(0f, 1f)
            setDuration(duration)
            interpolator = DecelerateInterpolator()
            addUpdateListener(this)
            start.getValues(mStart)
            end.getValues(mEnd)
        }

        override fun onAnimationUpdate(animation: ValueAnimator) {
            //获取动画进度
            val value = animation.animatedValue as Float
            //根据动画进度计算矩阵中间插值
            for (i in 0..8) {
                mResult[i] = mStart[i] + (mEnd[i] - mStart[i]) * value
            }
            //设置矩阵并重绘
            displayMatrix.setValues(mResult)
            invalidate()
        }
    }
    /**
     * 构建一个缩放动画
     *
     * 从一个矩阵变换到另外一个矩阵
     *
     * @param start 开始矩阵
     * @param end 结束矩阵
     */

}