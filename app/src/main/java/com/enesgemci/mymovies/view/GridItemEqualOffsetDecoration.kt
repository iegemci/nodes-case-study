package com.enesgemci.mymovies.view

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * add equal spaces to grid layouts
 */
class GridItemEqualOffsetDecoration(private val itemOffset: Int) : RecyclerView.ItemDecoration() {

    constructor(context: Context, @DimenRes itemOffsetId: Int) : this(
        context.resources.getDimensionPixelSize(
            itemOffsetId
        )
    )

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        var itemCountAtRow = 1

        if (parent.layoutManager is GridLayoutManager) {
            itemCountAtRow = (parent.layoutManager as GridLayoutManager).spanCount
        }

        val position = parent.getChildAdapterPosition(view)
        var topSpacing = itemOffset
        var leftSpacing = itemOffset
        var rightSpacing = itemOffset

        if (position < itemCountAtRow) {
            topSpacing = 0
        }

        if (position % itemCountAtRow == 0) {
            // first item in the row
            leftSpacing = 0
            rightSpacing = itemOffset
        } else if (position % itemCountAtRow == itemCountAtRow - 1) {
            // last item in the row
            leftSpacing = itemOffset
            rightSpacing = 0
        }

        outRect.set(
            leftSpacing,
            topSpacing * 2,
            rightSpacing,
            0
        )
    }
}
