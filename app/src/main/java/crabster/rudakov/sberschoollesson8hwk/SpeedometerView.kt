//package crabster.rudakov.sberschoollesson8hwk
//
//import android.content.Context
//import android.graphics.*
//import android.util.AttributeSet
//import android.view.View
//import androidx.annotation.ColorInt
//
//class SpeedometerView(
//    context: Context,
//    attributeSet: AttributeSet?,
//) : View(context, attributeSet) {
//    private var progress: Int = 0
//
//    @ColorInt
//    private var backgroundArcColor: Int = Color.RED
//
//    @ColorInt
//    private var foregroundArcColor: Int = Color.GREEN
//
//    private var max: Int = 100
//
//    private var strokeWidth: Float = 0F
//
//    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
//        style = Paint.Style.STROKE
//    }
//    private val foregroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
//        style = Paint.Style.STROKE
//        shader = LinearGradient(0f, 0f, 650f, 650f, Color.GREEN, Color.RED, Shader.TileMode.CLAMP)
//    }
//    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
//        textSize = 90f
//    }
//
//    private val arcRect = RectF(200f, 200f, 700f, 700f)
//    private val textRect = Rect()
//
//    init {
//        val typedArray = context.theme.obtainStyledAttributes(
//            attributeSet,
//            R.styleable.SpeedometerView,
//            0,
//            0
//        )
//
//        try {
//            progress = typedArray.getInt(R.styleable.SpeedometerView_android_progress, 0)
//            backgroundArcColor =
//                typedArray.getColor(R.styleable.SpeedometerView_backgroundArcColor, Color.GRAY)
//            foregroundArcColor =
//                typedArray.getColor(R.styleable.SpeedometerView_foregroundArcColor, Color.GREEN)
//            max = typedArray.getInt(R.styleable.SpeedometerView_maxSpeed, 330)
//            strokeWidth = typedArray.getDimension(R.styleable.SpeedometerView_strokeWidth, 0F)
//        } finally {
//            typedArray.recycle()
//        }
//        configurePaints()
//    }
//
//    override fun onDraw(canvas: Canvas?) {
//        canvas?.apply {
//            drawARGB(80, 102, 204, 255)
//            translate(strokeWidth / 2, strokeWidth / 2)
//            drawCircle(arcRect.centerX(), arcRect.centerY(), arcRect.width() / 2, backgroundPaint)
//            drawArc(arcRect, -180f, 180f / max * progress, false, foregroundPaint)
//            val s = "$progress km/h"
//            textPaint.getTextBounds(s, 0, s.length, textRect)
//            drawText(
//                s,
//                arcRect.centerX() - textRect.centerX(),
//                arcRect.centerY() - textRect.centerY(),
//                textPaint
//            )
//        }
//    }
//
//    fun setProgress(progress: Int) {
//        this.progress = progress
//        invalidate()
//    }
//
//    private fun configurePaints() {
//        backgroundPaint.strokeWidth = strokeWidth
//        backgroundPaint.color = backgroundArcColor
//        foregroundPaint.strokeWidth = strokeWidth
//        foregroundPaint.color = foregroundArcColor
//        textPaint.color = foregroundArcColor
//    }
//
//}