package dora.widget

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextUtils
import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class Decoration(private val config: DecorationConfig, private val array: SparseArray<String>) :
    ItemDecoration() {

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val fontMetrics: Paint.FontMetrics
    private var onTitleIndexChangeListener: OnTitleIndexChangeListener? = null

    fun keyAt(position: Int): Int {
        return array.keyAt(position)
    }

    fun setOnTitleIndexChangeListener(listener: OnTitleIndexChangeListener?) {
        onTitleIndexChangeListener = listener
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            if (array.indexOfKey(position) >= 0) {
                val tmp = array[position]
                val xOffset = config.textXOffset
                val yOffset =
                    child.top - config.height / 2 - (fontMetrics.bottom + fontMetrics.top) / 2
                val firstVisibleView = parent.getChildAt(0)
                val secondVisibleView = parent.getChildAt(1)
                val secondVisibleViewIndex = parent.getChildAdapterPosition(secondVisibleView)
                var bgColor = Color.rgb(
                    config.unSelectBgColorR,
                    config.unSelectBgColorG,
                    config.unSelectBgColorB
                )
                var textColor = Color.rgb(
                    config.unSelectTextColorR,
                    config.unSelectTextColorG,
                    config.unSelectTextColorB
                )
                if (array.indexOfKey(secondVisibleViewIndex) >= 0 && secondVisibleViewIndex == position && firstVisibleView.bottom <= config.height) {
                    var currentTextR = config.unSelectTextColorR
                    var currentTextG = config.unSelectTextColorG
                    var currentTextB = config.unSelectTextColorB
                    var currentBgR = config.unSelectBgColorR
                    var currentBgG = config.unSelectBgColorG
                    var currentBgB = config.unSelectBgColorB
                    val endTextR = config.selectedTextColorR
                    val endTextG = config.selectedTextColorG
                    val endTextB = config.selectedTextColorB
                    val endBgR = config.selectedBgColorR
                    val endBgG = config.selectedBgColorG
                    val endBgB = config.selectedBgColorB
                    val percent = 1f * (config.height - firstVisibleView.bottom) / config.height
                    // text
                    currentTextR = (currentTextR + (endTextR - currentTextR) * percent).toInt()
                    currentTextG = (currentTextG + (endTextG - currentTextG) * percent).toInt()
                    currentTextB = (currentTextB + (endTextB - currentTextB) * percent).toInt()
                    // bg
                    currentBgR = (currentBgR + (endBgR - currentBgR) * percent).toInt()
                    currentBgG = (currentBgG + (endBgG - currentBgG) * percent).toInt()
                    currentBgB = (currentBgB + (endBgB - currentBgB) * percent).toInt()
                    bgColor = Color.rgb(currentBgR, currentBgG, currentBgB)
                    textColor = Color.rgb(currentTextR, currentTextG, currentTextB)
                }
                if (config.lineHeight > 0) {
                    paint.color = config.lineColor
                    c.drawRect(
                        0f,
                        (child.top - config.height).toFloat(),
                        child.right.toFloat(),
                        (child.top - config.height + config.lineHeight).toFloat(),
                        paint
                    )
                    paint.color = config.lineColor
                    c.drawRect(
                        0f,
                        (child.top - config.lineHeight).toFloat(),
                        child.right.toFloat(),
                        child.top.toFloat(),
                        paint
                    )
                }
                paint.color = bgColor
                c.drawRect(
                    0f,
                    (child.top - config.height + config.lineHeight).toFloat(),
                    child.right.toFloat(),
                    (child.top - config.lineHeight).toFloat(),
                    paint
                )
                paint.color = textColor
                c.drawText(tmp, xOffset, yOffset, paint)
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val firstVisibleView = parent.getChildAt(0)
        val firstVisibleViewIndex = parent.getChildAdapterPosition(firstVisibleView)
        val secondVisibleView = parent.getChildAt(1)
        val secondVisibleViewIndex = parent.getChildAdapterPosition(secondVisibleView)
        var tmp: String? = ""
        for (i in 0 until array.size()) {
            val position = array.keyAt(i)
            if (firstVisibleViewIndex >= position) {
                tmp = array[array.keyAt(i)]
                if (onTitleIndexChangeListener != null) {
                    onTitleIndexChangeListener!!.onTitleIndexChanged(i)
                }
            } else {
                break
            }
        }
        if (!TextUtils.isEmpty(tmp)) {
            var top = 0f
            var currentTextR = config.selectedTextColorR
            var currentTextG = config.selectedTextColorG
            var currentTextB = config.selectedTextColorB
            var currentBgR = config.selectedBgColorR
            var currentBgG = config.selectedBgColorG
            var currentBgB = config.selectedBgColorB
            if (array.indexOfKey(secondVisibleViewIndex) >= 0
                && firstVisibleView.bottom <= config.height
            ) {
                // 第一个可见的控件是该组最后一个控件
                top = (firstVisibleView.bottom - config.height).toFloat()
                // text
                val endTextR = config.unSelectTextColorR
                val endTextG = config.unSelectTextColorG
                val endTextB = config.unSelectTextColorB
                // bg
                val endBgR = config.unSelectBgColorR
                val endBgG = config.unSelectBgColorG
                val endBgB = config.unSelectBgColorB
                val percent = 1f * Math.abs(top) / config.height
                currentTextR = (currentTextR + (endTextR - currentTextR) * percent).toInt()
                currentTextG = (currentTextG + (endTextG - currentTextG) * percent).toInt()
                currentTextB = (currentTextB + (endTextB - currentTextB) * percent).toInt()
                currentBgR = (currentBgR + (endBgR - currentBgR) * percent).toInt()
                currentBgG = (currentBgG + (endBgG - currentBgG) * percent).toInt()
                currentBgB = (currentBgB + (endBgB - currentBgB) * percent).toInt()
            }
            if (config.lineHeight > 0) {
                paint.color = config.lineColor
                c.drawRect(0f, top, parent.width.toFloat(), top + config.lineHeight, paint)
                paint.color = config.lineColor
                c.drawRect(
                    0f,
                    top + config.height - config.lineHeight,
                    parent.width.toFloat(),
                    top + config.height,
                    paint
                )
            }
            paint.color = Color.rgb(currentBgR, currentBgG, currentBgB)
            c.drawRect(
                0f,
                top + config.lineHeight,
                parent.width.toFloat(),
                top + config.height - config.lineHeight,
                paint
            )
            val xOffset = config.textXOffset
            val yOffset = top + config.height / 2 - (fontMetrics.bottom + fontMetrics.top) / 2
            val color = Color.rgb(currentTextR, currentTextG, currentTextB)
            paint.color = color
            c.drawText(tmp!!, xOffset, yOffset, paint)
        } else {
            if (onTitleIndexChangeListener != null) {
                onTitleIndexChangeListener!!.onTitleIndexChanged(-1)
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val childAdapterPosition = parent.getChildAdapterPosition(view)
        if (array.indexOfKey(childAdapterPosition) >= 0) {
            outRect.top = config.height
        }
    }

    interface OnTitleIndexChangeListener {
        fun onTitleIndexChanged(index: Int)
    }

    init {
        paint.textSize = config.textSize
        fontMetrics = paint.fontMetrics
    }
}