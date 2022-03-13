package dora.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.SparseArray
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dora.widget.Decoration.OnTitleIndexChangeListener
import java.util.*
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.sqrt

class LetterIndicatorView @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    private var indicatorBgColor = 0
    private var indicatorItemWidth = 0f
    private var indicatorItemHeight = 0f
    private var indicatorTextSize = 0f
    private var indicatorSelectedTextColor = 0
    private var indicatorSelectedBgColor = 0
    private var indicatorSelectedBgRadius = 0f
    private var indicatorUnselectedTextColor = 0
    private var indicatorBubbleTextSize = 0f
    private var indicatorBubbleTextColor = 0
    private var indicatorBubbleBgColor = 0
    private var indicatorBubbleBgRadius = 0f
    private var indicatorBubbleMargin = 0f
    private var indicatorBubbleDrawable: Drawable? = null
    private var indicators: ArrayList<String> = arrayListOf()
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var textBounds = Rect()
    private var path = Path()
    private var bubbleCircleRect = RectF()
    private var isOnTouchMode = false
    private var indicatorBubbleCenterY = 0f
    private var onTouchIndex = 0
    private var outChangeIndex = 0
    private var onIndicatorIndexChangeListener: OnIndicatorIndexChangeListener? = null

    private fun initAttrs(attrs: AttributeSet?) {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metrics)
        indicatorBgColor = Color.TRANSPARENT
        indicatorItemHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, metrics)
        indicatorItemWidth = indicatorItemHeight
        indicatorTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14f, metrics)
        indicatorSelectedTextColor = Color.parseColor("#1b8fe6")
        indicatorSelectedBgColor = Color.TRANSPARENT
        indicatorSelectedBgRadius =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, metrics)
        indicatorUnselectedTextColor = Color.parseColor("#646464")
        indicatorBubbleTextSize =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, metrics)
        indicatorBubbleTextColor = Color.WHITE
        indicatorBubbleBgColor = Color.argb(0x30, 0x00, 0x00, 0x00)
        indicatorBubbleBgRadius =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, metrics)
        indicatorBubbleMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, metrics)
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.LetterIndicatorView)
            if (a.hasValue(R.styleable.LetterIndicatorView_dora_indicatorBgColor)) {
                indicatorBgColor = a.getColor(
                    R.styleable.LetterIndicatorView_dora_indicatorBgColor,
                    indicatorBgColor
                )
            }
            if (a.hasValue(R.styleable.LetterIndicatorView_dora_indicatorItemWidth)) {
                indicatorItemWidth = a.getDimension(
                    R.styleable.LetterIndicatorView_dora_indicatorItemWidth,
                    indicatorItemWidth
                )
            }
            if (a.hasValue(R.styleable.LetterIndicatorView_dora_indicatorItemHeight)) {
                indicatorItemHeight = a.getDimension(
                    R.styleable.LetterIndicatorView_dora_indicatorItemHeight,
                    indicatorItemHeight
                )
            }
            if (a.hasValue(R.styleable.LetterIndicatorView_dora_indicatorTextSize)) {
                indicatorTextSize = a.getDimension(
                    R.styleable.LetterIndicatorView_dora_indicatorTextSize,
                    indicatorTextSize
                )
            }
            if (a.hasValue(R.styleable.LetterIndicatorView_dora_indicatorSelectedTextColor)) {
                indicatorSelectedTextColor = a.getColor(
                    R.styleable.LetterIndicatorView_dora_indicatorSelectedTextColor,
                    indicatorSelectedTextColor
                )
            }
            if (a.hasValue(R.styleable.LetterIndicatorView_dora_indicatorBgColor)) {
                indicatorSelectedBgColor = a.getColor(
                    R.styleable.LetterIndicatorView_dora_indicatorBgColor,
                    indicatorSelectedBgColor
                )
            }
            if (a.hasValue(R.styleable.LetterIndicatorView_dora_indicatorSelectedBgRadius)) {
                indicatorSelectedBgRadius = a.getDimension(
                    R.styleable.LetterIndicatorView_dora_indicatorSelectedBgRadius,
                    indicatorSelectedBgRadius
                )
            }
            if (a.hasValue(R.styleable.LetterIndicatorView_dora_indicatorUnselectedTextColor)) {
                indicatorUnselectedTextColor = a.getColor(
                    R.styleable.LetterIndicatorView_dora_indicatorUnselectedTextColor,
                    indicatorUnselectedTextColor
                )
            }
            if (a.hasValue(R.styleable.LetterIndicatorView_dora_indicatorBubbleTextSize)) {
                indicatorBubbleTextSize = a.getDimension(
                    R.styleable.LetterIndicatorView_dora_indicatorBubbleTextSize,
                    indicatorBubbleTextSize
                )
            }
            if (a.hasValue(R.styleable.LetterIndicatorView_dora_indicatorBubbleBgColor)) {
                indicatorBubbleBgColor = a.getColor(
                    R.styleable.LetterIndicatorView_dora_indicatorBubbleBgColor,
                    indicatorBubbleBgColor
                )
            }
            if (a.hasValue(R.styleable.LetterIndicatorView_dora_indicatorBubbleTextColor)) {
                indicatorBubbleTextColor = a.getColor(
                    R.styleable.LetterIndicatorView_dora_indicatorBubbleTextColor,
                    indicatorBubbleTextColor
                )
            }
            if (a.hasValue(R.styleable.LetterIndicatorView_dora_indicatorBubbleBgRadius)) {
                indicatorBubbleBgRadius = a.getDimension(
                    R.styleable.LetterIndicatorView_dora_indicatorBubbleBgRadius,
                    indicatorBubbleBgRadius
                )
            }
            if (a.hasValue(R.styleable.LetterIndicatorView_dora_indicatorBubbleMargin)) {
                indicatorBubbleMargin = a.getDimension(
                    R.styleable.LetterIndicatorView_dora_indicatorBubbleMargin,
                    indicatorBubbleMargin
                )
            }
            if (a.hasValue(R.styleable.LetterIndicatorView_dora_indicatorBubbleDrawable)) {
                indicatorBubbleDrawable = a.getDrawable(R.styleable.LetterIndicatorView_dora_indicatorBubbleDrawable)
            }
            a.recycle()
        }
    }

    fun setIndicators(indicators: SparseArray<String>) {
        val letters: ArrayList<String> = ArrayList()
        for (i in 0 until indicators.size()) {
            letters.add(indicators.valueAt(i))
        }
        setIndicators(letters)
    }

    fun setIndicators(indicators: List<String>) {
        this.indicators.clear()
        this.indicators.addAll(indicators)
    }

    fun setOutChangeIndex(index: Int) {
        outChangeIndex = index
        if (!isOnTouchMode) {
            onTouchIndex = outChangeIndex
            invalidate()
        }
    }

    fun setOnIndicatorIndexChangeListener(listener: OnIndicatorIndexChangeListener?) {
        onIndicatorIndexChangeListener = listener
    }

    fun attachToRecyclerView(
        recyclerView: RecyclerView,
        config: DecorationConfig,
        array: SparseArray<String>
    ) {
        val decoration = Decoration(config, array)
        recyclerView.addItemDecoration(decoration)
        val llm = recyclerView.layoutManager as LinearLayoutManager?
        decoration.setOnTitleIndexChangeListener(object : OnTitleIndexChangeListener {
            override fun onTitleIndexChanged(index: Int) {
                setOutChangeIndex(index)
            }
        })
        setOnIndicatorIndexChangeListener(object : OnIndicatorIndexChangeListener {
            override fun onIndicatorIndexChanged(index: Int) {
                if (index >= 0) {
                    llm?.scrollToPositionWithOffset(decoration.keyAt(index), 0)
                } else if (index == -1) {
                    llm?.scrollToPositionWithOffset(0, 0)
                }
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                if (x >= width - indicatorItemWidth) {
                    isOnTouchMode = true
                }
                if (isOnTouchMode) {
                    val y = event.y
                    calculateOnTouchIndex(y)
                    onIndicatorIndexChangeListener?.onIndicatorIndexChanged(onTouchIndex)
                    invalidate()
                }
            }
            MotionEvent.ACTION_MOVE -> if (isOnTouchMode) {
                val y = event.y
                calculateOnTouchIndex(y)
                onIndicatorIndexChangeListener?.onIndicatorIndexChanged(onTouchIndex)
                invalidate()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                onTouchIndex = outChangeIndex
                invalidate()
                isOnTouchMode = false
            }
            else -> {
            }
        }
        return if (isOnTouchMode) {
            true
        } else super.onTouchEvent(event)
    }

    private fun calculateOnTouchIndex(y: Float) {
        val firstItemTop = (height - totalItemHeight) / 2
        onTouchIndex = floor(((y - firstItemTop) / indicatorItemHeight).toDouble()).toInt()
        if (onTouchIndex < 0) {
            onTouchIndex = -1
        }
        if (onTouchIndex >= indicators.size) {
            onTouchIndex = indicators.size - 1
        }
        outChangeIndex = onTouchIndex
        indicatorBubbleCenterY = y
        if (indicatorBubbleCenterY < firstItemTop) {
            indicatorBubbleCenterY = firstItemTop
        }
        val lastItemBottom = firstItemTop + totalItemHeight
        if (indicatorBubbleCenterY > lastItemBottom) {
            indicatorBubbleCenterY = lastItemBottom
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        if (widthMode == MeasureSpec.AT_MOST) {
            val width = sqrt(3.0) * indicatorBubbleBgRadius + indicatorBubbleBgRadius
            widthSize = floor(x = indicatorItemWidth + width + indicatorBubbleMargin + 0.5)
                .toInt()
        }
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = floor(x = totalItemHeight + 0.5).toInt()
        }
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onDraw(canvas: Canvas) {
        drawIndicatorBg(canvas)
        drawIndicator(canvas)
        drawBubbleIndicator(canvas)
    }

    private fun drawIndicatorBg(canvas: Canvas) {
        val left = width - indicatorItemWidth
        val top = 0f
        val right = width.toFloat()
        val bottom = height.toFloat()
        paint.color = indicatorBgColor
        canvas.drawRect(left, top, right, bottom, paint)
    }

    private fun drawIndicator(canvas: Canvas) {
        val firstItemTop = (height - totalItemHeight) / 2
        val left = width - indicatorItemWidth
        val right = width.toFloat()
        if (indicatorBubbleDrawable != null) {
            val bubbleLeft =
                (right - (indicatorItemWidth + indicatorBubbleDrawable!!.intrinsicWidth) / 2).toInt()
            val bubbleTop =
                (firstItemTop - (indicatorItemHeight + indicatorBubbleDrawable!!.intrinsicHeight) / 2).toInt()
            val bubbleRight =
                (right - (indicatorItemWidth - indicatorBubbleDrawable!!.intrinsicWidth) / 2).toInt()
            val bubbleBottom =
                (firstItemTop - (indicatorItemHeight - indicatorBubbleDrawable!!.intrinsicHeight) / 2).toInt()
            indicatorBubbleDrawable!!.setBounds(bubbleLeft, bubbleTop, bubbleRight, bubbleBottom)
            indicatorBubbleDrawable!!.draw(canvas)
        }
        for (i in indicators.indices) {
            val top = firstItemTop + i * indicatorItemHeight
            val bottom = top + indicatorItemHeight
            if (i == onTouchIndex) {
                paint.color = indicatorSelectedBgColor
                val centerX = (left + right) / 2
                val centerY = (top + bottom) / 2
                path.reset()
                path.addCircle(centerX, centerY, indicatorSelectedBgRadius, Path.Direction.CCW)
                canvas.drawPath(path, paint)
                paint.color = indicatorSelectedTextColor
            } else {
                paint.color = indicatorUnselectedTextColor
            }
            paint.textSize = indicatorTextSize
            val fontMetrics = paint.fontMetrics
            val title = indicators[i]
            paint.getTextBounds(title, 0, title.length, textBounds)
            val xOffset = left + (indicatorItemWidth - textBounds.width()) / 2
            val yOffset =
                top + indicatorItemHeight / 2 - fontMetrics.top / 2 - fontMetrics.bottom / 2
            canvas.drawText(title, xOffset, yOffset, paint)
        }
    }

    fun drawBubbleIndicator(canvas: Canvas) {
        if (isOnTouchMode && onTouchIndex >= 0) {
            val right = width - indicatorItemWidth - indicatorBubbleMargin
            val circleX = (right - sqrt(3.0) * indicatorBubbleBgRadius).toFloat()
            val circleY = indicatorBubbleCenterY
            bubbleCircleRect.left = circleX - indicatorBubbleBgRadius
            bubbleCircleRect.top = circleY - indicatorBubbleBgRadius
            bubbleCircleRect.right = circleX + indicatorBubbleBgRadius
            bubbleCircleRect.bottom = circleY + indicatorBubbleBgRadius
            path.reset()
            val degree = Math.toRadians(60.0)
            path.moveTo(right, circleY)
            path.lineTo(
                (circleX + cos(degree) * indicatorBubbleBgRadius).toFloat(),
                (circleY - sin(degree) * indicatorBubbleBgRadius).toFloat()
            )
            path.moveTo(right, circleY)
            path.lineTo(
                (circleX + cos(degree) * indicatorBubbleBgRadius).toFloat(),
                (circleY + sin(degree) * indicatorBubbleBgRadius).toFloat()
            )
            path.arcTo(bubbleCircleRect, 60f, 240f)
            paint.color = indicatorBubbleBgColor
            canvas.drawPath(path, paint)
            paint.color = indicatorBubbleTextColor
            paint.textSize = indicatorBubbleTextSize
            val fontMetrics = paint.fontMetrics
            val tmp = indicators[onTouchIndex]
            paint.getTextBounds(tmp, 0, tmp.length, textBounds)
            val xOffset = indicatorBubbleBgRadius - textBounds.width() / 2
            val yOffset = indicatorBubbleCenterY - fontMetrics.top / 2 - fontMetrics.bottom / 2
            canvas.drawText(tmp, xOffset, yOffset, paint)
        }
    }

    private val totalItemHeight: Float
        private get() {
            var total = 0f
            total += indicators.size * indicatorItemHeight
            return total
        }

    interface OnIndicatorIndexChangeListener {
        fun onIndicatorIndexChanged(index: Int)
    }

    init {
        initAttrs(attrs)
    }
}