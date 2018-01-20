package ru.fantlab.android.ui.widgets.recyclerview

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import ru.fantlab.android.R
import ru.fantlab.android.helper.ViewHelper

internal class BottomPaddingDecoration : RecyclerView.ItemDecoration {

	private val bottomPadding: Int

	private constructor(bottomOffset: Int) {
		bottomPadding = bottomOffset
	}

	private constructor(context: Context) : this(ViewHelper.toPx(context, context.resources.getDimensionPixelSize(R.dimen.fab_spacing)))

	override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
		super.getItemOffsets(outRect, view, parent, state)
		val dataSize = state.itemCount
		val position = parent.getChildAdapterPosition(view)
		if (parent.layoutManager is LinearLayoutManager) {
			if (dataSize > 0 && position == dataSize - 1) {
				outRect.set(0, 0, 0, bottomPadding)
			} else {
				outRect.set(0, 0, 0, 0)
			}
		} else if (parent.layoutManager is StaggeredGridLayoutManager) {
			val grid = parent.layoutManager as StaggeredGridLayoutManager
			if (dataSize - position <= grid.spanCount) {
				outRect.set(0, 0, 0, bottomPadding)
			} else {
				outRect.set(0, 0, 0, 0)
			}
		} else if (parent.layoutManager is GridLayoutManager) {
			val grid = parent.layoutManager as GridLayoutManager
			if (dataSize - position <= grid.spanCount) {
				outRect.set(0, 0, 0, bottomPadding)
			} else {
				outRect.set(0, 0, 0, 0)
			}
		}
	}

	companion object {

		fun with(bottomPadding: Int): BottomPaddingDecoration {
			return BottomPaddingDecoration(bottomPadding)
		}

		fun with(context: Context): BottomPaddingDecoration {
			return BottomPaddingDecoration(context)
		}
	}
}