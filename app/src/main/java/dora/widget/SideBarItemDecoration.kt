package dora.widget

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class SideBarItemDecoration(private val array: SparseArray<String>) : ItemDecoration() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val childAdapterPosition = parent.getChildAdapterPosition(child)
            val itemViewType = parent.adapter!!.getItemViewType(childAdapterPosition)
            if (itemViewType == BaseRecyclerAdapter.ITEM_TYPE
                    && array.indexOfKey(childAdapterPosition) < 0) {
                c.drawRect(DensityUtils.dp2px(parent.context, 64f).toFloat(), child.top.toFloat(), child.right.toFloat(), (child.top - 1).toFloat(), paint)
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val childAdapterPosition = parent.getChildAdapterPosition(view)
        val itemViewType = parent.adapter!!.getItemViewType(childAdapterPosition)
        if (itemViewType == BaseRecyclerAdapter.ITEM_TYPE
                && array.indexOfKey(childAdapterPosition) < 0) {
            outRect.top = 1
        }
    }

    init {
        paint.color = Color.parseColor("#ebebeb")
    }
}